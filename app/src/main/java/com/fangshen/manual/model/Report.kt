package com.fangshen.manual.model

import org.json.JSONArray
import org.json.JSONObject

/**
 * .rp 报告文件的数据模型（见《报告文件(.rp)规范》）：
 *
 * {
 *   "head" : { "name", "date", "location", "character" : [{ "name", "note" }],
 *              "preface", "conclusion" },
 *   "body" : [ { "time", "scene", "text" } ]
 * }
 *
 * 解析尽量宽容：缺失字段一律降级为空串/空数组，绝不让阅读页崩溃。
 */
data class ReportHead(
    val name: String,
    val date: String,
    val location: String,
    val characters: List<ReportCharacter>,
    val preface: String,
    val conclusion: String,
)

data class ReportCharacter(
    val name: String,
    val note: String?,
)

data class ReportEpisode(
    val time: String,
    val scene: String,
    val text: String,
)

data class Report(
    val head: ReportHead,
    val body: List<ReportEpisode>,
    /** v4 片段格式；为空时阅读器降级渲染旧 body */
    val fragments: List<ReportFragment> = emptyList(),
)


/** 报告正文片段：场景或对话（v4 起新格式，与旧 body 并存） */
sealed interface ReportFragment {
    /** 场景片段：时间 + 地点 + 描述 */
    data class Scene(
        val time: String,
        val place: String,
        val description: String,
    ) : ReportFragment

    /** 对话片段：首报告人（head.name）的消息靠右，其余人靠左 */
    data class Dialog(
        val items: List<DialogItem>,
    ) : ReportFragment
}

/** 对话条目：一句话，或 QQ 系统提示式事件（居中、无卡片背景） */
sealed interface DialogItem {
    /** 说话人 + 内容 */
    data class Say(
        val name: String,
        val content: String,
    ) : DialogItem

    /** 居中的事件提示 */
    data class Event(
        val content: String,
    ) : DialogItem
}

/** 旧式 body 条目（time/scene/text）降级为场景片段 */
fun ReportEpisode.toSceneFragment(): ReportFragment.Scene =
    ReportFragment.Scene(time, scene, text)

/** 解析 "fragments" 数组；每个元素 {"type":"scene"|"dialog", ...} */
fun parseFragments(array: JSONArray): List<ReportFragment> = List(array.length()) { i ->
    val o = array.optJSONObject(i) ?: return@List ReportFragment.Scene("", "", "")
    when (o.optString("type", "scene")) {
        "dialog" -> ReportFragment.Dialog(
            items = o.optJSONArray("items")?.let { arr ->
                List(arr.length()) { j ->
                    val it = arr.optJSONObject(j) ?: return@List DialogItem.Event("")
                    when (it.optString("kind", "say")) {
                        "event" -> DialogItem.Event(it.optString("content", "").trim())
                        else -> DialogItem.Say(it.optString("name", "").trim(), it.optString("content", "").trim())
                    }
                }.filter { item ->
                    when (item) {
                        is DialogItem.Say -> item.name.isNotEmpty() || item.content.isNotEmpty()
                        is DialogItem.Event -> item.content.isNotEmpty()
                    }
                }
            } ?: emptyList(),
        )
        else -> ReportFragment.Scene(
            o.optString("time", "").trim(),
            o.optString("place", "").trim(),
            o.optString("description", "").trim(),
        )
    }
}.filter { frag ->
    when (frag) {
        is ReportFragment.Scene -> frag.time.isNotEmpty() || frag.place.isNotEmpty() || frag.description.isNotEmpty()
        is ReportFragment.Dialog -> frag.items.isNotEmpty()
    }
}

fun fragmentsToJson(fragments: List<ReportFragment>): JSONArray = JSONArray().apply {
    fragments.forEach { frag ->
        put(when (frag) {
            is ReportFragment.Scene -> JSONObject().apply {
                put("type", "scene")
                put("time", frag.time)
                put("place", frag.place)
                put("description", frag.description)
            }
            is ReportFragment.Dialog -> JSONObject().apply {
                put("type", "dialog")
                put("items", JSONArray().apply {
                    frag.items.forEach { item ->
                        put(when (item) {
                            is DialogItem.Say -> JSONObject().apply {
                                put("kind", "say")
                                put("name", item.name)
                                put("content", item.content)
                            }
                            is DialogItem.Event -> JSONObject().apply {
                                put("kind", "event")
                                put("content", item.content)
                            }
                        })
                    }
                })
            }
        })
    }
}

/** 报告正文的统一片段视图：新格式优先，旧 body 降级为场景片段 */
fun Report.flatFragments(): List<ReportFragment> =
    if (fragments.isNotEmpty()) fragments else body.map { it.toSceneFragment() }

/** 报告者备注是否需要渲染成蓝色可点样式 */
val ReportCharacter.hasNote: Boolean get() = !note.isNullOrBlank()

fun parseReport(json: String): Report {
    val o = JSONObject(json)
    val head = o.optJSONObject("head")
    fun str(key: String): String = when (head) {
        null -> ""
        else -> head.optString(key, "").trim()
    }
    val characters = head?.optJSONArray("character")?.toCharacterList() ?: emptyList()
    val body = o.optJSONArray("body")?.toEpisodeList() ?: emptyList()
    return Report(
        head = ReportHead(
            name = str("name"),
            date = str("date"),
            location = str("location"),
            characters = characters,
            preface = str("preface"),
            conclusion = str("conclusion"),
        ),
        body = body,
    )
}

private fun JSONArray.toCharacterList(): List<ReportCharacter> =
    List(length()) { i ->
        val c = optJSONObject(i) ?: return@List ReportCharacter("", null)
        val note = c.optString("note", "")
        ReportCharacter(
            name = c.optString("name", "").trim(),
            note = note.ifBlank { null },
        )
    }.filter { it.name.isNotEmpty() }

private fun JSONArray.toEpisodeList(): List<ReportEpisode> =
    List(length()) { i ->
        val e = optJSONObject(i) ?: return@List ReportEpisode("", "", "")
        ReportEpisode(
            time = e.optString("time", "").trim(),
            scene = e.optString("scene", "").trim(),
            text = e.optString("text", "").trim(),
        )
    }

/** 片段标题：时间+"："+地点；地点为空不追冒号；时间为空只写地点；两者皆空返回 null */
fun ReportEpisode.titleText(): String? = when {
    time.isNotEmpty() && scene.isNotEmpty() -> "$time：$scene"
    time.isNotEmpty() -> time
    scene.isNotEmpty() -> scene
    else -> null
}

/** 新建报告的模板（head 为空壳，便于用户直接按规范填写） */
fun reportTemplate(name: String, date: String): String = JSONObject().apply {
    put("head", JSONObject().apply {
        put("name", name)
        put("date", date)
        put("location", "")
        put("character", JSONArray())
        put("preface", "")
        put("conclusion", "")
    })
    put("body", JSONArray())
}.toString(4)

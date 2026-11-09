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
)

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

package com.fangshen.manual.model

import org.json.JSONArray
import org.json.JSONObject

/**
 * 应用设置（全部带默认值，持久化在 data.json 的 settings 节点）。
 */
data class AppSettings(
    /** 成瘾游标线常显（否则仅在高亮药物曲线时显示） */
    val showAddictionCursor: Boolean = false,
    /** 服药记录卡片显示精麻标识 */
    val showPsychotropicBadge: Boolean = false,
    /** 效应展示开关 */
    val effectDisplay: EffectDisplay = EffectDisplay(),
)

/** 效应条/词典的显示开关：按"急/慢 × 正/负/中立"分类，外加效应包 */
data class EffectDisplay(
    val showPacks: Boolean = true,
    val acutePositive: Boolean = true,
    val acuteNegative: Boolean = true,
    val acuteNeutral: Boolean = true,
    val chronicPositive: Boolean = true,
    val chronicNegative: Boolean = true,
    val chronicNeutral: Boolean = true,
)

/** 按显示开关过滤后的效应分类键 */
enum class EffectCategory(val label: String) {
    PACK("效应包"),
    ACUTE_POSITIVE("急性·正面"), ACUTE_NEGATIVE("急性·负面"), ACUTE_NEUTRAL("急性·中立"),
    CHRONIC_POSITIVE("慢性·正面"), CHRONIC_NEGATIVE("慢性·负面"), CHRONIC_NEUTRAL("慢性·中立"),
}

/** 判定一个效应（含急慢性）所属的展示分类；急+慢并触发时归急性 */
fun effectCategory(name: String, displayChronic: Boolean): EffectCategory? {
    val def = EffectCatalog.def(name) ?: return null
    val chronic = displayChronic || def.urgency == EffectUrgency.CHRONIC
    return when {
        EffectCatalog.isPackMember(name) -> EffectCategory.PACK
        def.valence == EffectValence.POSITIVE && chronic -> EffectCategory.CHRONIC_POSITIVE
        def.valence == EffectValence.NEGATIVE && chronic -> EffectCategory.CHRONIC_NEGATIVE
        def.valence == EffectValence.NEUTRAL && chronic -> EffectCategory.CHRONIC_NEUTRAL
        def.valence == EffectValence.POSITIVE -> EffectCategory.ACUTE_POSITIVE
        def.valence == EffectValence.NEGATIVE -> EffectCategory.ACUTE_NEGATIVE
        else -> EffectCategory.ACUTE_NEUTRAL
    }
}

/** 分类是否被设置允许显示 */
fun EffectCategory.enabledIn(d: EffectDisplay): Boolean = when (this) {
    EffectCategory.PACK -> d.showPacks
    EffectCategory.ACUTE_POSITIVE -> d.acutePositive
    EffectCategory.ACUTE_NEGATIVE -> d.acuteNegative
    EffectCategory.ACUTE_NEUTRAL -> d.acuteNeutral
    EffectCategory.CHRONIC_POSITIVE -> d.chronicPositive
    EffectCategory.CHRONIC_NEGATIVE -> d.chronicNegative
    EffectCategory.CHRONIC_NEUTRAL -> d.chronicNeutral
}

// —— 用户自定义内容（效应 / 效应包 / 联用配对）——

/** 自定义效应定义（与内置 EffectDef 同形态） */
data class CustomEffect(
    val name: String,
    val valence: EffectValence,
    val chronic: Boolean,
    val description: String,
    val inverseOf: String? = null,
) {
    fun toEffectDef(): EffectDef = EffectDef(
        name = name,
        urgency = if (chronic) EffectUrgency.CHRONIC else EffectUrgency.ACUTE,
        valence = valence,
        inverseOf = inverseOf,
        description = description,
    )
}

/** 自定义效应包 */
data class CustomPack(
    val name: String,
    val members: List<String>,
    val description: String = "",
)

/** 自定义联用预警配对：匹配串可以是药名或标签 */
data class CustomInteraction(
    val matcherA: String,
    val matcherB: String,
    val title: String,
    val message: String,
    val enabled: Boolean = true,
)

// —— JSON 序列化 ——

fun AppSettings.toJson(): JSONObject = JSONObject().apply {
    put("showAddictionCursor", showAddictionCursor)
    put("showPsychotropicBadge", showPsychotropicBadge)
    put("effectDisplay", effectDisplay.toJson())
}

fun parseSettings(o: JSONObject): AppSettings = AppSettings(
    showAddictionCursor = o.optBoolean("showAddictionCursor", false),
    showPsychotropicBadge = o.optBoolean("showPsychotropicBadge", false),
    effectDisplay = o.optJSONObject("effectDisplay")?.let { parseEffectDisplay(it) } ?: EffectDisplay(),
)

private fun EffectDisplay.toJson(): JSONObject = JSONObject().apply {
    put("showPacks", showPacks)
    put("acutePositive", acutePositive)
    put("acuteNegative", acuteNegative)
    put("acuteNeutral", acuteNeutral)
    put("chronicPositive", chronicPositive)
    put("chronicNegative", chronicNegative)
    put("chronicNeutral", chronicNeutral)
}

private fun parseEffectDisplay(o: JSONObject): EffectDisplay = EffectDisplay(
    showPacks = o.optBoolean("showPacks", true),
    acutePositive = o.optBoolean("acutePositive", true),
    acuteNegative = o.optBoolean("acuteNegative", true),
    acuteNeutral = o.optBoolean("acuteNeutral", true),
    chronicPositive = o.optBoolean("chronicPositive", true),
    chronicNegative = o.optBoolean("chronicNegative", true),
    chronicNeutral = o.optBoolean("chronicNeutral", true),
)

fun CustomEffect.toJson(): JSONObject = JSONObject().apply {
    put("name", name)
    put("valence", valence.name)
    put("chronic", chronic)
    put("description", description)
    inverseOf?.let { put("inverseOf", it) }
}

fun parseCustomEffect(o: JSONObject): CustomEffect = CustomEffect(
    name = o.optString("name"),
    valence = runCatching { EffectValence.valueOf(o.optString("valence")) }.getOrDefault(EffectValence.NEUTRAL),
    chronic = o.optBoolean("chronic", false),
    description = o.optString("description"),
    inverseOf = o.optString("inverseOf").ifBlank { null },
)

fun CustomPack.toJson(): JSONObject = JSONObject().apply {
    put("name", name)
    put("members", JSONArray(members))
    put("description", description)
}

fun parseCustomPack(o: JSONObject): CustomPack = CustomPack(
    name = o.optString("name"),
    members = o.optJSONArray("members")?.let { arr -> List(arr.length()) { arr.optString(it) } } ?: emptyList(),
    description = o.optString("description"),
)

fun CustomInteraction.toJson(): JSONObject = JSONObject().apply {
    put("matcherA", matcherA)
    put("matcherB", matcherB)
    put("title", title)
    put("message", message)
    put("enabled", enabled)
}

fun parseCustomInteraction(o: JSONObject): CustomInteraction = CustomInteraction(
    matcherA = o.optString("matcherA"),
    matcherB = o.optString("matcherB"),
    title = o.optString("title"),
    message = o.optString("message"),
    enabled = o.optBoolean("enabled", true),
)

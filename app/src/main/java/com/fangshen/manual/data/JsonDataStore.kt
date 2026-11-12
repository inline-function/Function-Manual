package com.fangshen.manual.data

import android.content.Context
import com.fangshen.manual.model.AlertRule
import com.fangshen.manual.model.AppSettings
import com.fangshen.manual.model.CustomEffect
import com.fangshen.manual.model.CustomInteraction
import com.fangshen.manual.model.CustomPack
import com.fangshen.manual.model.parseCustomEffect
import com.fangshen.manual.model.parseCustomInteraction
import com.fangshen.manual.model.parseCustomPack
import com.fangshen.manual.model.parseSettings
import com.fangshen.manual.model.toJson
import com.fangshen.manual.data.entity.CustomDrugEntity
import com.fangshen.manual.data.entity.CustomDrugNeuroEntity
import com.fangshen.manual.data.entity.CustomDrugRuleEntity
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.data.entity.ProfileEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** 导出数据文件中的自定义药物：实体 + 关联的效应线与神经递质权重 */
data class DrugJson(
    val entity: CustomDrugEntity,
    val rules: List<CustomDrugRuleEntity>,
    val neuros: List<CustomDrugNeuroEntity>,
)

/**
 * 应用的全部数据：服药记录、个人画像、自定义药物。
 * 持久化为 JSON 文件，存放在应用外部私有目录（无需存储权限，卸载即清除）。
 */
data class JsonData(
    val entries: List<EntryEntity> = emptyList(),
    val profile: List<ProfileEntity> = emptyList(),
    val drugs: List<DrugJson> = emptyList(),
    val alerts: List<AlertRule> = emptyList(),
    // —— v4 用户内容 ——
    val customEffects: List<CustomEffect> = emptyList(),
    val customPacks: List<CustomPack> = emptyList(),
    val customInteractions: List<CustomInteraction> = emptyList(),
    val settings: AppSettings = AppSettings(),
)

/**
 * JSON 数据存储：单一文件 [FILE_NAME]，位于 getExternalFilesDir/fangshen/。
 * 读写均在调用方协程中完成；[state] 变更即通知所有观察者。
 */
class JsonDataStore(context: Context) {

    private val dir = (context.getExternalFilesDir(null) ?: context.filesDir)
        .let { File(it, "fangshen") }
        .apply { mkdirs() }

    private val file = File(dir, FILE_NAME)

    private val _state = MutableStateFlow(load())

    /** 当前数据快照（同时是唯一的变更通知源） */
    val state: StateFlow<JsonData> = _state.asStateFlow()

    fun snapshot(): JsonData = _state.value

    /** 读取 JSON 文件；文件不存在或解析失败时返回空数据 */
    private fun load(): JsonData {
        if (!file.exists()) return JsonData()
        return runCatching {
            val text = file.readText()
            if (text.isBlank()) return JsonData()
            parseJson(JSONObject(text))
        }.getOrDefault(JsonData())
    }

    /** 原子写入并更新观察者 */
    fun save(data: JsonData) {
        val tmp = File(dir, FILE_NAME + ".tmp")
        tmp.writeText(toJsonString(data))
        if (tmp.renameTo(file)) {
            _state.value = data
        } else {
            tmp.delete()
        }
    }

    /** 整份数据的 JSON 文本（含导出元信息，用于分享与复制） */
    fun exportJsonString(): String {
        val data = _state.value
        val root = JSONObject()
        root.put("schemaVersion", 1)
        root.put("exportedAt", System.currentTimeMillis())
        root.put("app", "com.fangshen.manual")
        root.put("note", "芳沈手册数据导出：服药记录 / 个人画像 / 自定义药物")
        root.put("entries", JSONArray(data.entries.map { it.toJson() }))
        root.put("profile", JSONArray(data.profile.map { it.toJson() }))
        root.put("customDrugs", JSONArray(data.drugs.map { it.toJson() }))
        root.put("alerts", JSONArray(data.alerts.map { it.toJson() }))
        root.put("customEffects", JSONArray(data.customEffects.map { it.toJson() }))
        root.put("customPacks", JSONArray(data.customPacks.map { it.toJson() }))
        root.put("customInteractions", JSONArray(data.customInteractions.map { it.toJson() }))
        root.put("settings", data.settings.toJson())
        return root.toString(2)
    }

    /** 把导出文件写入外部私有目录的 [exportDir]，并返回文件 */
    fun writeExportCopy(): File {
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date(System.currentTimeMillis()))
        val target = File(dir, "export/fangshen_$stamp.json")
        target.parentFile?.mkdirs()
        target.writeText(exportJsonString())
        return target
    }

    /** 首次升级：从旧的 Room 数据库迁移到 JSON（文件已存在则跳过） */
    suspend fun migrateFromRoomIfNeeded(db: AppDatabase) {
        if (file.exists()) return
        val entries = db.entryDao().allOnce()
        val profile = db.profileDao().allOnce()
        val drugs = db.customDrugDao().allOnce().map { entity ->
            DrugJson(
                entity = entity,
                rules = db.customDrugRuleDao().allOnce().filter { it.drugId == entity.id },
                neuros = db.customDrugNeuroDao().allOnce().filter { it.drugId == entity.id },
            )
        }
        save(JsonData(entries = entries, profile = profile, drugs = drugs))
    }

    private fun parseJson(root: JSONObject): JsonData = JsonData(
        entries = root.optJSONArray("entries")?.let { arr ->
            List(arr.length()) { i -> parseEntry(arr.getJSONObject(i)) }
        } ?: emptyList(),
        profile = root.optJSONArray("profile")?.let { arr ->
            List(arr.length()) { i -> parseProfile(arr.getJSONObject(i)) }
        } ?: emptyList(),
        drugs = root.optJSONArray("customDrugs")?.let { arr ->
            List(arr.length()) { i -> parseDrug(arr.getJSONObject(i)) }
        } ?: emptyList(),
        alerts = root.optJSONArray("alerts")?.let { arr ->
            List(arr.length()) { i -> parseAlert(arr.getJSONObject(i)) }
        } ?: emptyList(),
        customEffects = root.optJSONArray("customEffects")?.let { arr ->
            List(arr.length()) { i -> parseCustomEffect(arr.getJSONObject(i)) }
        } ?: emptyList(),
        customPacks = root.optJSONArray("customPacks")?.let { arr ->
            List(arr.length()) { i -> parseCustomPack(arr.getJSONObject(i)) }
        } ?: emptyList(),
        customInteractions = root.optJSONArray("customInteractions")?.let { arr ->
            List(arr.length()) { i -> parseCustomInteraction(arr.getJSONObject(i)) }
        } ?: emptyList(),
        settings = root.optJSONObject("settings")?.let { parseSettings(it) } ?: AppSettings(),
    )

    private fun toJsonString(data: JsonData): String {
        val root = JSONObject()
        root.put("schemaVersion", 1)
        root.put("updatedAt", System.currentTimeMillis())
        root.put("entries", JSONArray(data.entries.map { it.toJson() }))
        root.put("profile", JSONArray(data.profile.map { it.toJson() }))
        root.put("customDrugs", JSONArray(data.drugs.map { it.toJson() }))
        root.put("alerts", JSONArray(data.alerts.map { it.toJson() }))
        root.put("customEffects", JSONArray(data.customEffects.map { it.toJson() }))
        root.put("customPacks", JSONArray(data.customPacks.map { it.toJson() }))
        root.put("customInteractions", JSONArray(data.customInteractions.map { it.toJson() }))
        root.put("settings", data.settings.toJson())
        return root.toString(2)
    }

    companion object {
        private const val FILE_NAME = "data.json"
    }
}

private fun AlertRule.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("name", name)
    put("enabled", enabled)
    put("drug", drug)
    withDrug?.let { put("withDrug", it) } ?: put("withDrug", JSONObject.NULL)
    withProfile?.let { put("withProfile", it) } ?: put("withProfile", JSONObject.NULL)
    put("effect", effect)
    put("weight", weight)
}

private fun parseAlert(o: JSONObject): AlertRule = AlertRule(
    id = o.optLong("id"),
    name = o.optString("name"),
    enabled = o.optBoolean("enabled", true),
    drug = o.optString("drug"),
    withDrug = o.optString("withDrug").takeIf { !it.isNullOrEmpty() && o.opt("withDrug") != JSONObject.NULL },
    withProfile = o.optString("withProfile").takeIf { !it.isNullOrEmpty() && o.opt("withProfile") != JSONObject.NULL },
    effect = o.optString("effect"),
    weight = o.optInt("weight"),
)

private fun EntryEntity.toJson(): JSONObject = JSONObject().apply {
    put("id", id)
    put("type", type)
    put("title", title)
    content?.let { put("content", it) }
    drugId?.let { put("drugId", it) }
    doseAmount?.let { put("doseAmount", it) }
    doseUnit?.let { put("doseUnit", it) }
    route?.let { put("route", it) }
    attachmentUri?.let { put("attachmentUri", it) }
    durationMs?.let { put("durationMs", it) }
    put("eventTime", eventTime)
    put("notify", notify)
    put("createdAt", createdAt)
    put("updatedAt", updatedAt)
}

private fun parseEntry(o: JSONObject): EntryEntity = EntryEntity(
    id = o.optLong("id"),
    type = o.optString("type", "NOTE"),
    title = o.optString("title", ""),
    content = o.optString("content", "").ifEmpty { null },
    drugId = o.optString("drugId", "").ifEmpty { null },
    doseAmount = o.optString("doseAmount", "").ifEmpty { null },
    doseUnit = o.optString("doseUnit", "").ifEmpty { null },
    route = o.optString("route", "").ifEmpty { null },
    attachmentUri = o.optString("attachmentUri", "").ifEmpty { null },
    durationMs = if (o.has("durationMs")) o.getLong("durationMs") else null,
    eventTime = o.optLong("eventTime"),
    notify = o.optBoolean("notify", false),
    createdAt = o.optLong("createdAt"),
    updatedAt = o.optLong("updatedAt"),
)

private fun ProfileEntity.toJson(): JSONObject = JSONObject().apply {
    put("key", key)
    put("valueText", valueText)
    put("updatedAt", updatedAt)
}

private fun parseProfile(o: JSONObject): ProfileEntity = ProfileEntity(
    key = o.optString("key"),
    valueText = o.optString("valueText"),
    updatedAt = o.optLong("updatedAt"),
)

private fun DrugJson.toJson(): JSONObject = JSONObject().apply {
    val e = entity
    put("id", e.id)
    put("name", e.name)
    put("colorArgb", e.colorArgb)
    e.tabletDoseMg?.let { put("tabletDoseMg", it) }
    put("doseUnit", e.doseUnit)
    put("managementClass", e.managementClass)
    e.subjectiveTags?.let { put("subjectiveTags", it) }
    e.effectTags?.let { put("effectTags", it) }
    put("moleculeTag", e.moleculeTag)
    put("halfLifeHours", e.halfLifeHours)
    put("bioavailabilityPct", e.bioavailabilityPct)
    put("route", e.route)
    e.tmaxHours?.let { put("tmaxHours", it) }
    e.substrates?.let { put("substrates", it) }
    e.inhibitors?.let { put("inhibitors", it) }
    e.inducers?.let { put("inducers", it) }
    put("createdAt", e.createdAt)
    e.description?.let { put("description", it) }
    e.routes?.let { put("routes", it) }
    put("addictive", e.addictive)
    put("addictionRatePerHour", e.addictionRatePerHour)
    e.toleranceGroup?.let { put("toleranceGroup", it) }
    put("rules", JSONArray(rules.map { r ->
        JSONObject().apply {
            put("id", r.id)
            put("drugId", r.drugId)
            put("threshold", r.threshold)
            put("effectName", r.effectName)
            put("weight", r.weight)
        }
    }))
    put("neuros", JSONArray(neuros.map { n ->
        JSONObject().apply {
            put("id", n.id)
            put("drugId", n.drugId)
            put("neurotransmitter", n.neurotransmitter)
            put("delta", n.delta)
        }
    }))
}

private fun parseDrug(o: JSONObject): DrugJson = DrugJson(
    entity = CustomDrugEntity(
        id = o.optLong("id"),
        name = o.optString("name"),
        colorArgb = o.optLong("colorArgb"),
        tabletDoseMg = o.optString("tabletDoseMg", "").ifEmpty { null },
        doseUnit = o.optString("doseUnit", "MG"),
        managementClass = o.optString("managementClass", "RX"),
        subjectiveTags = o.optString("subjectiveTags", "").ifEmpty { null },
        effectTags = o.optString("effectTags", "").ifEmpty { null },
        moleculeTag = o.optString("moleculeTag", ""),
        halfLifeHours = o.optDouble("halfLifeHours", 12.0),
        bioavailabilityPct = o.optInt("bioavailabilityPct", 100),
        route = o.optString("route", "ORAL"),
        tmaxHours = o.optDouble("tmaxHours", Double.NaN).let { if (it.isNaN()) null else it },
        substrates = o.optString("substrates", "").ifEmpty { null },
        inhibitors = o.optString("inhibitors", "").ifEmpty { null },
        inducers = o.optString("inducers", "").ifEmpty { null },
        createdAt = o.optLong("createdAt"),
        description = o.optString("description", "").ifEmpty { null },
        routes = o.optString("routes", "").ifEmpty { null },
        addictive = o.optBoolean("addictive", false),
        addictionRatePerHour = o.optDouble("addictionRatePerHour", 0.0),
        toleranceGroup = o.optString("toleranceGroup", "").ifEmpty { null },
    ),
    rules = o.optJSONArray("rules")?.let { arr ->
        List(arr.length()) { i ->
            val r = arr.getJSONObject(i)
            CustomDrugRuleEntity(
                id = r.optLong("id"),
                drugId = r.optLong("drugId"),
                threshold = r.optString("threshold", "0"),
                effectName = r.optString("effectName"),
                weight = r.optInt("weight", 1),
            )
        }
    } ?: emptyList(),
    neuros = o.optJSONArray("neuros")?.let { arr ->
        List(arr.length()) { i ->
            val n = arr.getJSONObject(i)
            CustomDrugNeuroEntity(
                id = n.optLong("id"),
                drugId = n.optLong("drugId"),
                neurotransmitter = n.optString("neurotransmitter"),
                delta = n.optInt("delta", 0),
            )
        }
    } ?: emptyList(),
)

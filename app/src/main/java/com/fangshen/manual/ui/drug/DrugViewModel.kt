package com.fangshen.manual.ui.drug

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import androidx.lifecycle.ViewModel
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.data.entity.CustomDrugEntity
import com.fangshen.manual.data.entity.CustomDrugNeuroEntity
import com.fangshen.manual.data.entity.CustomDrugRuleEntity
import com.fangshen.manual.model.DrugCatalog
import com.fangshen.manual.model.DrugInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import com.fangshen.manual.model.parseActiveMetabolites
import com.fangshen.manual.model.parseAttributes

data class DrugListUiState(
    val drugs: List<DrugInfo> = emptyList(),
    val query: String = "",
) {
    val visible: List<DrugInfo> get() = if (query.isBlank()) drugs else
        drugs.filter {
            it.name.contains(query, ignoreCase = true) ||
                it.subjectiveTags.any { tag -> tag.contains(query, ignoreCase = true) } ||
                it.effectTags.any { tag -> tag.contains(query, ignoreCase = true) } ||
                it.moleculeTag.contains(query, ignoreCase = true)
        }
}

class DrugListViewModel(
    application: Application,
    drugs: kotlinx.coroutines.flow.Flow<List<DrugInfo>>,
) : AndroidViewModel(application) {

    private val query = MutableStateFlow("")

    val uiState = combine(drugs, query) { drugList, q ->
        DrugListUiState(drugs = drugList, query = q)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DrugListUiState(),
    )

    fun setQuery(q: String) { query.value = q }

    /** 把药物资料导出为 JSON 并分享 */
    fun shareDrugJson(drug: DrugInfo) {
        val app = getApplication<FangShenApplication>()
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val json = drugToJson(drug).toString(2)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                com.fangshen.manual.util.FileShare.shareJson(
                    app, "${drug.name}.json", json, "分享药物",
                )
            }
        }
    }

    fun deleteDrug(drug: DrugInfo, onDone: () -> Unit) {
        if (!drug.isBuiltin) {
            viewModelScope.launch {
                val app = getApplication<FangShenApplication>()
                val customId = drug.id.removePrefix("custom:").toLong()
                app.drugRepository.customEntity(customId)?.let { entity ->
                    app.drugRepository.deleteDrug(entity)
                }
                onDone()
            }
        }
    }

    companion object {
        fun provideFactory(app: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val a = app as FangShenApplication
                return DrugListViewModel(app, a.drugRepository.drugs) as T
            }
            }
    }
}

/** 药物 → 可分享的 JSON */
private fun drugToJson(drug: DrugInfo) = org.json.JSONObject().apply {
    put("name", drug.name)
    put("builtin", drug.isBuiltin)
    drug.tabletDose?.let { put("tabletDoseMg", it.stripTrailingZeros().toPlainString()) }
    put("doseUnit", drug.doseUnit.name)
    put("managementClass", drug.managementClass.name)
    put("subjectiveTags", org.json.JSONArray(drug.subjectiveTags))
    put("effectTags", org.json.JSONArray(drug.effectTags))
    put("moleculeTag", drug.moleculeTag)
    put("halfLifeHours", drug.halfLifeHours)
    put("bioavailabilityPct", drug.bioavailabilityPct)
    put("route", drug.route.name)
    drug.tmaxHours?.let { put("tmaxHours", it) }
    put("substrates", org.json.JSONArray(drug.substrates))
    put("inhibitors", org.json.JSONArray(drug.inhibitors))
    put("inducers", org.json.JSONArray(drug.inducers))
    put("rules", org.json.JSONArray(drug.rules.map { r ->
        org.json.JSONObject().apply {
            put("threshold", r.threshold.stripTrailingZeros().toPlainString())
            put("effectName", r.effectName)
            put("weight", r.weight)
        }
    }))
    put("neuros", org.json.JSONArray(drug.neuros.map { n ->
        org.json.JSONObject().apply {
            put("neurotransmitter", n.neurotransmitter)
            put("delta", n.delta)
        }
    }))
}

class DrugDetailViewModel(
    application: Application,
    private val drugId: String,
    drugs: kotlinx.coroutines.flow.Flow<List<DrugInfo>>,
) : AndroidViewModel(application) {

    val uiState = drugs
        .map { list -> list.firstOrNull { it.id == drugId } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = null,
        )

    /** 与近期（7 天内）在服药物的联用预警，仅保留涉及本药者 */
    val interactions: StateFlow<List<com.fangshen.manual.engine.InteractionEngine.Warning>> =
        combine(uiState, getApplication<FangShenApplication>().entryRepository.entries) { drug, entries ->
            val weekAgo = System.currentTimeMillis() - 7L * 86_400_000L
            val other = entries.filter {
                it.type == "DOSE" && it.drugId != null && it.drugId != drugId && it.eventTime >= weekAgo
            }
            if (drug == null || other.isEmpty()) emptyList() else {
                val ids = (other.mapNotNull { it.drugId } + drugId).toSet()
                val all = drugs.first()
                val involved = all.filter { it.id in ids }
                com.fangshen.manual.engine.InteractionEngine.check(involved)
                    .filter { it.drugName == drug.name || it.otherDrugName == drug.name }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList(),
        )

    /** v3.1：涉及本药的联用知识（内置机制配对 + 用户自定义配对，不依赖当前是否在服） */
    val knowledge: StateFlow<List<com.fangshen.manual.engine.InteractionEngine.DrugKnowledge>> =
        combine(
            uiState,
            getApplication<FangShenApplication>().contentRepository.state,
        ) { drug, data ->
            if (drug == null) emptyList()
            else com.fangshen.manual.engine.InteractionEngine.knowledgeFor(drug, data.customInteractions)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList(),
        )

    fun delete(onDone: () -> Unit) {
        val drug = uiState.value ?: return
        if (drug.isBuiltin) return
        viewModelScope.launch {
            val app = getApplication<FangShenApplication>()
            val customId = drug.id.removePrefix("custom:").toLong()
            app.drugRepository.customEntity(customId)?.let { entity ->
                app.drugRepository.deleteDrug(entity)
            }
            onDone()
        }
    }

    companion object {
        fun provideFactory(app: Application, drugId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val a = app as FangShenApplication
                    return DrugDetailViewModel(app, drugId, a.drugRepository.drugs) as T
                }
                }
    }
}

/** 药物编辑表单状态 */
data class DrugEditForm(
    val id: Long = 0L,
    val name: String = "",
    val colorArgb: Long = 0xFF6750A4,
    val tabletDose: String = "",
    val doseUnit: String = "MG",
    val managementClass: String = "RX",
    val subjectiveTags: List<String> = emptyList(),
    val effectTags: List<String> = emptyList(),
    val moleculeTag: String = "",
    val halfLifeHours: String = "",
    val bioavailabilityPct: String = "100",
    val route: String = "ORAL",
    val tmaxHours: String = "",
    val substrates: List<String> = emptyList(),
    val inhibitors: List<String> = emptyList(),
    val inducers: List<String> = emptyList(),
    val rules: List<DoseRuleRow> = emptyList(),
    val neuros: List<NeuroRow> = emptyList(),
    // v4 扩展
    val description: String = "",
    val routes: List<String> = emptyList(),
    val addictive: Boolean = false,
    val addictionRatePerHour: String = "",
    val toleranceGroup: String = "",
    // v3.1：戒断反应描述；v3.1：可共享耐药组的候选药（药名 → 组名）
    val withdrawalNote: String = "",
    val shareCandidates: List<Pair<String, String>> = emptyList(),
    // v5：活性代谢物（引用内置键或 custom:id）
    val metabolites: List<MetaboliteRow> = emptyList(),
    // v5：展示属性（纯展示）
    val attributes: List<AttributeRow> = emptyList(),
    /** 候选引用列表：key → 显示名 */
    val candidates: List<Pair<String, String>> = emptyList(),
    val error: String? = null,
)

/** 展示属性编辑行：属性名 + 区间两端（-1..5） */
data class AttributeRow(
    val name: String = "",
    val from: Int = 0,
    val to: Int = 3,
)

/** 活性代谢物编辑行：key 引用、转化分数 fm%、效价、形成延迟（小时） */
data class MetaboliteRow(
    val key: String = "",
    val fmPct: String = "100",
    val potency: String = "1",
    val delayHours: String = "",
)

data class DoseRuleRow(
    val threshold: String = "",
    val effectName: String = "",
    val weight: String = "1",
)

data class NeuroRow(
    val neurotransmitter: String = "血清素",
    val delta: String = "1",
)

class DrugEditViewModel(
    application: Application,
    private val drugId: String,
) : AndroidViewModel(application) {

    var form = MutableStateFlow(DrugEditForm())
        private set

    init {
        viewModelScope.launch {
            val app = getApplication<FangShenApplication>()
            val list = app.drugRepository.drugs.first()
            val selfId = if (drugId.startsWith("custom:")) drugId else "builtin:" + drugId
            form.value = form.value.copy(
                candidates = list.map { d ->
                    ((if (d.isBuiltin) d.builtinKey else d.id) ?: d.id) to d.name
                },
                // 可共享耐药组候选：已有耐药组的其他药（药名 → 组名），排除自己
                shareCandidates = list
                    .filter { it.id != selfId && !it.toleranceGroup.isNullOrBlank() }
                    .map { it.name to it.toleranceGroup!! }
                    .distinctBy { it.first },
            )
        }
        if (drugId != "new") {
            viewModelScope.launch {
                val app = getApplication<FangShenApplication>()
                if (drugId.startsWith("custom:")) {
                    val customId = drugId.removePrefix("custom:").toLongOrNull() ?: return@launch
                    val entity = app.drugRepository.customEntity(customId) ?: return@launch
                    val rules = app.drugRepository.loadRules(customId)
                    val neuros = app.drugRepository.loadNeuros(customId)
                    form.value = DrugEditForm(
                        id = entity.id,
                        name = entity.name,
                        colorArgb = entity.colorArgb,
                        tabletDose = entity.tabletDoseMg ?: "",
                        doseUnit = entity.doseUnit,
                        managementClass = entity.managementClass,
                        subjectiveTags = entity.subjectiveTags?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
                        effectTags = entity.effectTags?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
                        moleculeTag = entity.moleculeTag,
                        halfLifeHours = entity.halfLifeHours.toString(),
                        bioavailabilityPct = entity.bioavailabilityPct.toString(),
                        route = entity.route,
                        tmaxHours = entity.tmaxHours?.toString() ?: "",
                        substrates = entity.substrates?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
                        inhibitors = entity.inhibitors?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
                        inducers = entity.inducers?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
                        rules = rules.map { DoseRuleRow(it.threshold, it.effectName, it.weight.toString()) },
                        neuros = neuros.map { NeuroRow(it.neurotransmitter, it.delta.toString()) },
                        description = entity.description.orEmpty(),
                        routes = entity.routes?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
                        addictive = entity.addictive,
                        addictionRatePerHour = entity.addictionRatePerHour.toString(),
                        toleranceGroup = entity.toleranceGroup.orEmpty(),
                        withdrawalNote = entity.withdrawalNote.orEmpty(),
                        metabolites = parseActiveMetabolites(entity.activeMetabolites).map {
                            MetaboliteRow(
                                it.key,
                                it.fmPct.toString(),
                                it.potency.toString(),
                                it.formationDelayHours?.toString() ?: "",
                            )
                        },
                        attributes = parseAttributes(entity.attributes).map {
                            AttributeRow(it.name, it.from, it.to)
                        },
                    )
                }
            }
        }
    }

    fun update(transform: (DrugEditForm) -> DrugEditForm) {
        form.value = transform(form.value)
    }

    fun save(onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
        val f = form.value
        val name = f.name.trim()
        when {
            name.isBlank() -> { onError("请填写药物名称"); return }
            f.halfLifeHours.toDoubleOrNull()?.let { it <= 0 } != false -> {
                onError("消除半衰期必须为大于 0 的数字"); return
            }
            f.bioavailabilityPct.toIntOrNull()?.let { it !in 0..100 } != false -> {
                onError("生物利用度须为 0–100 的整数"); return
            }
            else -> {
                val badRule = f.rules.firstOrNull { row ->
                    row.effectName.isBlank() ||
                        row.threshold.trim().toBigDecimalOrNull()?.let { it < java.math.BigDecimal.ZERO } != true ||
                        row.weight.toIntOrNull()?.let { it == 0 } != true
                }
                if (badRule != null) { onError("效应线存在无效数值：阈值须≥0、权重须非 0"); return }
            }
        }
        viewModelScope.launch {
            val app = getApplication<FangShenApplication>()
            val entity = CustomDrugEntity(
                id = f.id,
                name = name,
                colorArgb = f.colorArgb,
                tabletDoseMg = f.tabletDose.trim().toBigDecimalOrNull()?.stripTrailingZeros()?.toPlainString(),
                doseUnit = f.doseUnit,
                managementClass = f.managementClass,
                subjectiveTags = f.subjectiveTags.joinToString(","),
                effectTags = f.effectTags.joinToString(","),
                moleculeTag = f.moleculeTag.trim(),
                halfLifeHours = f.halfLifeHours.toDouble()!!,
                bioavailabilityPct = f.bioavailabilityPct.toInt(),
                route = f.route,
                tmaxHours = f.tmaxHours.toDoubleOrNull(),
                substrates = f.substrates.joinToString(","),
                inhibitors = f.inhibitors.joinToString(","),
                inducers = f.inducers.joinToString(","),
                createdAt = System.currentTimeMillis(),
                description = f.description.trim().ifEmpty { null },
                routes = f.routes.joinToString(","),
                addictive = f.addictive,
                addictionRatePerHour = f.addictionRatePerHour.toDoubleOrNull() ?: 0.0,
                toleranceGroup = f.toleranceGroup.trim().ifEmpty { null },
                withdrawalNote = f.withdrawalNote.trim().ifEmpty { null },
                activeMetabolites = f.metabolites
                    .filter { it.key.isNotBlank() && it.fmPct.toIntOrNull() != null }
                    .joinToString(";") { row ->
                        listOf(
                            row.key.trim(),
                            (row.fmPct.toIntOrNull() ?: 100).toString(),
                            (row.potency.toDoubleOrNull() ?: 1.0).toString(),
                            row.delayHours.trim(),
                        ).filter { it.isNotEmpty() }.joinToString(":")
                    }
                    .ifEmpty { null },
                attributes = f.attributes
                    .filter { it.name.isNotBlank() }
                    .joinToString(";") { row ->
                        row.name.trim() + ":" + minOf(row.from, row.to).coerceIn(-1, 5) +
                            ":" + maxOf(row.from, row.to).coerceIn(-1, 5)
                    }
                    .ifEmpty { null },
            )
            val rules = f.rules
                .filter {
                    it.effectName.isNotBlank() &&
                        it.threshold.trim().toBigDecimalOrNull() != null &&
                        it.weight.toIntOrNull() != null
                }
                .map {
                    CustomDrugRuleEntity(
                        0, 0,
                        it.threshold.trim().toBigDecimalOrNull()!!.stripTrailingZeros().toPlainString(),
                        it.effectName.trim(),
                        it.weight.toInt(),
                    )
                }
            val neuros = f.neuros
                .filter { it.neurotransmitter.isNotBlank() && it.delta.toIntOrNull() != null }
                .map { CustomDrugNeuroEntity(0, 0, it.neurotransmitter.trim(), it.delta.toInt()) }
            val newId = app.drugRepository.saveDrug(entity, rules, neuros)
            onSuccess(newId)
        }
    }

    companion object {
        fun provideFactory(app: Application, drugId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DrugEditViewModel(app, drugId) as T
    }
    }
}

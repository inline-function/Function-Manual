package com.fangshen.manual.model

import java.math.BigDecimal

/**
 * 内置药物目录（领域参考数据）。
 *
 * 注意：这些条目的血药浓度与效应推算均为大幅简化的示意模型，数据不完整且个体差异大，
 * 置信度很低，仅作参考之用，主要价值在于记录服药行为本身。
 */
/** 药物条目构造器：药库分片中使用 */
internal fun d(
        key: String,
        name: String,
        colorArgb: Long,
        tabletDose: BigDecimal?,
        doseUnit: DoseUnit,
        managementClass: ManagementClass,
        subjectiveTags: List<String>,
        effectTags: List<String>,
        moleculeTag: String,
        halfLifeHours: Double,
        bioavailabilityPct: Int,
        route: Route,
        tmaxHours: Double? = null,
        substrates: List<String> = emptyList(),
        inhibitors: List<String> = emptyList(),
        inducers: List<String> = emptyList(),
        rules: List<DoseEffectRule> = emptyList(),
        neuros: List<NeuroDelta> = emptyList(),
        nonlinearity: MichaelisMenten? = null,
        autoInduction: AutoInduction? = null,
        saturableAbsorption: SaturableAbsorption? = null,
        activeMetabolites: List<ActiveMetabolite> = emptyList(),
        vdLiters: Double? = null,
        hidden: Boolean = false,
        description: String? = null,
        routes: List<Route> = emptyList(),
        addictive: Boolean = false,
        addictionRatePerHour: Double = 0.0,
        toleranceGroup: String? = null,
    ) = DrugInfo(
        id = "builtin:$key", name = name, colorArgb = colorArgb,
        tabletDose = tabletDose, doseUnit = doseUnit, managementClass = managementClass,
        subjectiveTags = subjectiveTags, effectTags = effectTags, moleculeTag = moleculeTag,
        halfLifeHours = halfLifeHours, bioavailabilityPct = bioavailabilityPct, route = route,
        tmaxHours = tmaxHours, substrates = substrates, inhibitors = inhibitors, inducers = inducers,
        rules = rules, neuros = neuros, isBuiltin = true,
        nonlinearity = nonlinearity, autoInduction = autoInduction,
        saturableAbsorption = saturableAbsorption, activeMetabolites = activeMetabolites,
        vdLiters = vdLiters, hiddenFromCatalog = hidden,
        description = description, routes = routes, addictive = addictive,
        addictionRatePerHour = addictionRatePerHour, toleranceGroup = toleranceGroup,
    )

object DrugCatalog {

    /** 全部内置药物（按分片拼接，保持原始顺序） */
    val all: List<DrugInfo> = listOf(
        DrugCatalogPart1.entries,
        DrugCatalogPart2.entries,
        DrugCatalogPart3.entries,
        DrugCatalogPart4.entries,
    ).flatten()

    private val byKey = all.associateBy { it.builtinKey }

    fun byBuiltinKey(key: String): DrugInfo? = byKey[key]
}

package com.fangshen.manual.model

import com.fangshen.manual.data.entity.CustomDrugEntity
import com.fangshen.manual.data.entity.CustomDrugNeuroEntity
import com.fangshen.manual.data.entity.CustomDrugRuleEntity
import java.math.BigDecimal

/**
 * 效应线：服用剂量超过 [threshold] 时，对 [effectName] 施加 [weight] 的权重改变。
 */
data class DoseEffectRule(
    val threshold: BigDecimal,
    val effectName: String,
    val weight: Int,
)

/** 神经递质权重改变 */
data class NeuroDelta(
    val neurotransmitter: String,
    val delta: Int,
)

/**
 * 统一药物模型：内置药物（领域知识）与用户自定义药物（Room）共用同一形态。
 * [id] 形如 "builtin:fluvoxamine"（内置）或 "custom:12"（数据库主键）。
 * 所有剂量字段一律使用 BigDecimal，避免浮点误差。
 */
data class DrugInfo(
    val id: String,
    val name: String,
    val colorArgb: Long,
    val tabletDose: BigDecimal?,
    val doseUnit: DoseUnit,
    val managementClass: ManagementClass,
    val subjectiveTags: List<String>,
    val effectTags: List<String>,
    val moleculeTag: String,
    val halfLifeHours: Double,
    val bioavailabilityPct: Int,
    val route: Route,
    val tmaxHours: Double?,
    val substrates: List<String>,
    val inhibitors: List<String>,
    val inducers: List<String>,
    val rules: List<DoseEffectRule>,
    val neuros: List<NeuroDelta>,
    val isBuiltin: Boolean,
) {
    val builtinKey: String? get() = if (isBuiltin) id.removePrefix("builtin:") else null
}

fun CustomDrugEntity.toDrugInfo(
    rules: List<CustomDrugRuleEntity>,
    neuros: List<CustomDrugNeuroEntity>,
): DrugInfo = DrugInfo(
    id = "custom:$id",
    name = name,
    colorArgb = colorArgb,
    tabletDose = tabletDoseMg?.toBigDecimalOrNull(),
    doseUnit = DoseUnit.fromName(doseUnit),
    managementClass = ManagementClass.fromName(managementClass),
    subjectiveTags = splitCsv(subjectiveTags),
    effectTags = splitCsv(effectTags),
    moleculeTag = moleculeTag,
    halfLifeHours = halfLifeHours.toDouble(),
    bioavailabilityPct = bioavailabilityPct,
    route = Route.fromName(route),
    tmaxHours = tmaxHours?.toDouble(),
    substrates = splitCsv(substrates),
    inhibitors = splitCsv(inhibitors),
    inducers = splitCsv(inducers),
    rules = rules.map { DoseEffectRule(it.threshold.toBigDecimal(), it.effectName, it.weight) },
    neuros = neuros.map { NeuroDelta(it.neurotransmitter, it.delta) },
    isBuiltin = false,
)

fun splitCsv(s: String?): List<String> =
    s?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()

fun joinCsv(list: List<String>): String = list.joinToString(",")

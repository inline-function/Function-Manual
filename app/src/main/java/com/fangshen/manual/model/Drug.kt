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

/** 非线性消除：Michaelis–Menten 容量限制（dC/dt = -Vmax·C/(Km+C)），如乙醇、苯妥英 */
data class MichaelisMenten(
    val vmaxMgPerLPerHour: Double,   // 最大消除速率（mg/L/h，按体内表观浓度计）
    val kmMgPerL: Double,             // 半速率浓度（mg/L），低于它退回近似线性
)

/**
 * 自诱导：消除速率随连续用药天数逐渐加快（如卡马西平 t½ 35h→15h）。
 * [maxFactor] 为 1+λ：ke_effective = ke·(1+λ·(1-e^(-days/[daysToSteady])))。
 */
data class AutoInduction(
    val daysToSteady: Double,        // 时间常数 τ（天），大部分诱导在此完成
    val maxFactor: Double,           // 诱导完成后的消除倍率（1.0 = 不诱导）
)

/**
 * 吸收饱和：剂量越大生物利用度越低（如加巴喷丁 300mg→60%、1600mg→20%）。
 * F(D) = fMaxPct · d50Mg / (d50Mg + D)。
 */
data class SaturableAbsorption(
    val d50Mg: Double,               // 生物利用度降到一半时的剂量
    val fMaxPct: Int,                // 低剂量极限生物利用度
)

/**
 * 活性代谢物：母药消除后生成、仍具药理活性的物种（如去甲氟西汀、去甲西泮）。
 * [key] 引用药库内的另一条目（通常是 hiddenFromCatalog=true 的隐藏条目；
 * 也可引用普通条目，如海洛因→吗啡）。运行时把等效剂量作为该条目的"伪服药"
 * 注入效应与曲线，其自身的 t½ 与效应规则生效。
 */
data class ActiveMetabolite(
    val key: String,                 // 引用条目的 builtin key
    val fmPct: Int,                  // 转化分数 fm（占母药剂量的百分比）
    val potency: Double = 1.0,       // 相对效价（等效剂量 = 母药剂量·fm·potency）
    val formationDelayHours: Double? = null,  // 形成延迟，默认 0.25×母药 t½
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
    // —— v3 药代扩展（默认回退线性 Bateman，保持旧数据兼容）——
    val nonlinearity: MichaelisMenten? = null,
    val autoInduction: AutoInduction? = null,
    val saturableAbsorption: SaturableAbsorption? = null,
    val activeMetabolites: List<ActiveMetabolite> = emptyList(),
    val vdLiters: Double? = null,               // 表观分布容积覆盖（默认 50L）
    val hiddenFromCatalog: Boolean = false,     // 隐藏条目：活性代谢物等，不在药库/服药列表展示
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

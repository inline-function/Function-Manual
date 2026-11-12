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
    /** 该规则贡献的效应按慢性呈现（默认急性）；同效应急慢并触发只显示急性、权重叠加 */
    val isChronic: Boolean = false,
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
    // —— v4 药物卡片/成瘾扩展 ——
    /** 一段简介（药品说明），药物详情页展示 */
    val description: String? = null,
    /** 支持的给药途径；短按默认首个，长按可自选 */
    val routes: List<Route> = emptyList(),
    /** 声明成瘾性：按小时速度移动成瘾游标线 */
    val addictive: Boolean = false,
    val addictionRatePerHour: Double = 0.0,
    /** 交叉耐药组：同组药物共享同一条成瘾游标线 */
    val toleranceGroup: String? = null,
    // —— v5 展示属性（不影响效应与药代引擎）——
    val attributes: List<DrugAttribute> = emptyList(),
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
    description = description,
    routes = routes?.let { r -> splitCsv(r).mapNotNull { Route.fromNameOrNull(it) } } ?: emptyList(),
    addictive = addictive,
    addictionRatePerHour = addictionRatePerHour,
    toleranceGroup = toleranceGroup,
    activeMetabolites = parseActiveMetabolites(activeMetabolites),
    attributes = parseAttributes(attributes),
)

/** 解析自定义药物的活性代谢物声明：分号分组 "key:fmPct:potency:delayHours"（后两项可省略） */
fun parseActiveMetabolites(s: String?): List<ActiveMetabolite> =
    s?.split(";")?.mapNotNull { item ->
        val p = item.split(":").map { it.trim() }.filter { it.isNotEmpty() }
        if (p.isEmpty()) return@mapNotNull null
        val key = p[0]
        val fm = p.getOrNull(1)?.toIntOrNull() ?: return@mapNotNull null
        ActiveMetabolite(
            key = key,
            fmPct = fm.coerceIn(0, 200),
            potency = p.getOrNull(2)?.toDoubleOrNull() ?: 1.0,
            formationDelayHours = p.getOrNull(3)?.toDoubleOrNull(),
        )
    } ?: emptyList()

/** 代谢物引用解析为条目 id：custom:id 原样，内置键加 builtin: 前缀 */
fun ActiveMetabolite.drugId(): String = if (key.startsWith("custom:")) key else "builtin:" + key

/**
 * 药物展示属性的七个等级：负 / 无 / 正极轻 / 正轻度 / 正中度 / 正重度 / 正极重。
 * 区间两端均为该序号值（-1..5），属性条按区间着色：负段红色，无及正段蓝色。
 */
val ATTRIBUTE_LEVELS: List<String> = listOf("负", "无", "正极轻", "正轻度", "正中度", "正重度", "正极重")

fun attributeLevelLabel(value: Int): String =
    ATTRIBUTE_LEVELS.getOrElse(value + 1) { "无" }

/** 药物展示属性：名称 + 两点闭区间（纯展示，不参与效应/药代引擎） */
data class DrugAttribute(val name: String, val from: Int, val to: Int)

/** 解析自定义药物属性声明：分号分组 "name:from:to" */
fun parseAttributes(text: String?): List<DrugAttribute> =
    text?.split(";")?.mapNotNull { item ->
        val p = item.split(":").map { it.trim() }.filter { it.isNotEmpty() }
        if (p.isEmpty()) return@mapNotNull null
        val from = p.getOrNull(1)?.toIntOrNull() ?: return@mapNotNull null
        val to = p.getOrNull(2)?.toIntOrNull() ?: return@mapNotNull null
        if (from !in -1..5 || to !in -1..5) return@mapNotNull null
        DrugAttribute(p[0], minOf(from, to), maxOf(from, to))
    } ?: emptyList()

fun Route.Companion.fromNameOrNull(name: String): Route? = runCatching { fromName(name) }.getOrNull()

fun splitCsv(s: String?): List<String> =
    s?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()

fun joinCsv(list: List<String>): String = list.joinToString(",")

/** 实际支持的给药途径：未声明时回退为单一路由（短按默认） */
fun DrugInfo.effectiveRoutes(): List<Route> = if (routes.isEmpty()) listOf(route) else routes

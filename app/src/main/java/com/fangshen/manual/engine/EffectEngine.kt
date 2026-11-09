package com.fangshen.manual.engine

import com.fangshen.manual.model.AlertRule
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.EffectCatalog
import com.fangshen.manual.model.EffectDef
import com.fangshen.manual.model.EffectSeverity
import com.fangshen.manual.model.matches
import kotlin.math.abs

/** 单次服药剂量；amount 用 BigDecimal 精确表示 */
data class DrugDoseInfo(val drug: DrugInfo, val amount: java.math.BigDecimal, val time: Long)

/**
 * 效应权重引擎。
 *
 * 剂量生效规则：
 * - 距现在一个半衰期内的剂量：求和；
 * - 距现在一至五个半衰期内的剂量：取最大值；
 * - 超过五个半衰期：不再计入。
 *
 * 效应线规则：若生效剂量严格大于阈值，则把权重加到对应效应上。
 * 同一效应无论是被哪条规则触发，都允许权重相加。效应之间可能存在反效应，
 * 计算时把两个互反效应的权重互相抵消。效应包（如血清素综合征）是效应的集合，
 * 单独计算权重：若触发某个效应包，它下面的效应也会被绘制出来。
 */
object EffectEngine {

    /** 一个效应组（效应包或独立效应）的计算结果 */
    data class EffectGroup(
        val name: String,
        val weight: Int,
        val severity: EffectSeverity,
        val isPack: Boolean,
        val def: EffectDef?,
        val members: List<EffectGroup>, // 仅效应包有成员
        val sources: List<String>,      // 贡献来源（药物名）
        /** 各来源药物贡献的权重（药物名 -> 权重） */
        val sourceWeights: Map<String, Int> = emptyMap(),
        /** 是否属于效应包的成员、因而不单独展示 */
        val absorbedByPack: Boolean = false,
    )

    data class Result(
        val groups: List<EffectGroup>,
        /** 当前生效（有效剂量大于 0）的药物 */
        val activeDrugs: List<DrugInfo>,
    ) {
        fun weightOf(name: String): Int = groups.firstOrNull { it.name == name }?.weight ?: 0
    }

    /**
     * 计算 [doses] 在 [now] 时刻的效应状态。
     * [ageYears] 为个人画像中的年龄（暂未实现，可传 null）。
     * [alertRules] 为预警规则（联用/画像），[profileFlags] 为当前画像命中的
     * 类别与条目名集合（由个人画像中的选择得出）。
     */
    fun compute(
        doses: List<DrugDoseInfo>,
        now: Long,
        ageYears: Int? = null,
        alertRules: List<AlertRule> = emptyList(),
        profileFlags: Set<String> = emptySet(),
    ): Result {
        val weights = mutableMapOf<String, Int>()
        val sources = mutableMapOf<String, MutableSet<String>>()
        val weightBySource = mutableMapOf<String, MutableMap<String, Int>>()

        fun add(effectName: String, weight: Int, source: String) {
            if (weight == 0) return
            weights[effectName] = (weights[effectName] ?: 0) + weight
            sources.getOrPut(effectName) { mutableSetOf() }.add(source)
            val perDrug = weightBySource.getOrPut(effectName) { mutableMapOf() }
            perDrug[source] = (perDrug[source] ?: 0) + weight
        }

        // 1. 按药物归集剂量并计算生效剂量
        val byDrug = doses.groupBy { it.drug }
        data class DrugDose(val amount: java.math.BigDecimal, val time: Long)
        val effectiveAmount = mutableMapOf<DrugInfo, java.math.BigDecimal>()
        val activeDrugs = mutableListOf<DrugInfo>()
        val ZERO = java.math.BigDecimal.ZERO
        for ((drug, list) in byDrug) {
            val halfLifeMs = (drug.halfLifeHours.coerceAtLeast(0.1) * 3_600_000.0).toLong()
            val recentSum = list.filter {
                it.amount > ZERO && it.time <= now && now - it.time <= halfLifeMs
            }.sumOf { it.amount }
            val midMax = list.filter {
                it.amount > ZERO && it.time <= now &&
                    now - it.time in (halfLifeMs + 1)..(halfLifeMs * 5)
            }.maxOfOrNull { it.amount }
            val amount = if (recentSum > ZERO) recentSum else (midMax ?: ZERO)
            if (amount > ZERO) {
                effectiveAmount[drug] = amount
                activeDrugs += drug
            }
        }

        // 2. 效应线规则
        for ((drug, amount) in effectiveAmount) {
            for (rule in drug.rules) {
                if (amount > rule.threshold) add(rule.effectName, rule.weight, drug.name)
            }
        }

        // 3. 预警规则：联用药物或特定画像下额外增加权重
        if (alertRules.isNotEmpty()) {
            for (rule in alertRules) {
                if (!rule.enabled) continue
                if (activeDrugs.none { it.matches(rule.drug) }) continue
                val triggered = when {
                    rule.isProfileRule -> rule.withProfile!! in profileFlags
                    !rule.withDrug.isNullOrEmpty() -> activeDrugs.any { it.matches(rule.withDrug!!) }
                    else -> false
                }
                if (triggered) add(rule.effect, rule.weight, "预警·" + rule.name)
            }
        }

        // 4. 个人画像规则（占位）：舍曲林 + 年龄 ≤ 24 岁 → 自杀冲动 +1
        if (ageYears != null && ageYears <= 24) {
            val sertralineActive = effectiveAmount.keys.any {
                it.builtinKey == "sertraline"
            }
            if (sertralineActive) add("自杀冲动", 1, "个人画像")
        }

        // 5. 反效应抵消（对两个互反效应中较小的那个权重，两两相消）
        val cancelled = weights.toMutableMap()
        val names = cancelled.keys.toList()
        for (name in names) {
            val inverse = EffectCatalog.def(name)?.inverseOf
            if (inverse != null && cancelled.containsKey(inverse)) {
                val a = cancelled[name] ?: 0
                val b = cancelled[inverse] ?: 0
                val m = minOf(a, b)
                if (m > 0) {
                    cancelled[name] = a - m
                    cancelled[inverse] = b - m
                }
            }
        }

        // 6. 效应包权重与其成员
        val memberTotals = mutableMapOf<String, Int>()
        val memberSources = mutableMapOf<String, MutableSet<String>>()
        val memberSourceWeights = mutableMapOf<String, MutableMap<String, Int>>()
        val activePacks = mutableMapOf<String, Int>()
        for ((pack, members) in EffectCatalog.PACKS) {
            val packWeight = cancelled[pack] ?: 0
            if (packWeight > 0) activePacks[pack] = packWeight
            for (member in members) {
                val direct = cancelled[member] ?: 0
                val total = direct + packWeight
                if (total > 0 || packWeight > 0) {
                    memberTotals[member] = total
                    memberSources.getOrPut(member) { mutableSetOf() }
                    val merged = memberSourceWeights.getOrPut(member) { mutableMapOf() }
                    weightBySource[member]?.let { merged.putAll(it) }
                    weightBySource[pack]?.forEach { (drug, w) -> merged[drug] = (merged[drug] ?: 0) + w }
                    sources[member]?.let { memberSources[member]!!.addAll(it) }
                    sources[pack]?.let { memberSources[member]!!.addAll(it) }
                }
            }
        }

        // 7. 组装展示结果
        val groups = mutableListOf<EffectGroup>()
        val memberNamesShownInPack = mutableSetOf<String>()

        for ((pack, members) in EffectCatalog.PACKS) {
            val packWeight = activePacks[pack] ?: continue
            val memberGroups = members.mapNotNull { member ->
                val w = memberTotals[member] ?: return@mapNotNull null
                if (w <= 0) return@mapNotNull null
                memberNamesShownInPack += member
                EffectGroup(
                    name = member,
                    weight = w,
                    severity = EffectSeverity.fromWeight(w),
                    isPack = false,
                    def = EffectCatalog.def(member),
                    members = emptyList(),
                    sources = (memberSources[member] ?: emptySet()).toList(),
                    sourceWeights = memberSourceWeights[member] ?: emptyMap(),
                )
            }
            groups += EffectGroup(
                name = pack,
                weight = packWeight,
                severity = EffectSeverity.fromWeight(packWeight),
                isPack = true,
                def = EffectCatalog.def(pack),
                members = memberGroups,
                sources = (sources[pack] ?: emptySet()).toList(),
                sourceWeights = weightBySource[pack] ?: emptyMap(),
            )
        }

        for ((name, weight) in cancelled) {
            if (weight <= 0) continue
            val def = EffectCatalog.def(name)
            if (def != null && def.hidden) continue
            if (EffectCatalog.PACKS.containsKey(name)) continue // 效应包已作为组
            if (name in memberNamesShownInPack) continue        // 已归入活跃效应包
            groups += EffectGroup(
                name = name,
                weight = weight,
                severity = EffectSeverity.fromWeight(weight),
                isPack = false, // 独立效应：不属于任何活跃效应包的组
                def = def,
                members = emptyList(),
                sources = (sources[name] ?: emptySet()).toList(),
                sourceWeights = weightBySource[name] ?: emptyMap(),
            )
        }

        return Result(groups = groups, activeDrugs = activeDrugs)
    }
}

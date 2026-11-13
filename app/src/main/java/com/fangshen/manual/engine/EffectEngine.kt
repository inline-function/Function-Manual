package com.fangshen.manual.engine

import com.fangshen.manual.model.AlertRule
import com.fangshen.manual.model.EffectUrgency
import com.fangshen.manual.model.Route
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.EffectCatalog
import com.fangshen.manual.model.EffectDef
import com.fangshen.manual.model.EffectSeverity
import com.fangshen.manual.model.matches
import kotlin.math.abs

/** 单次服药剂量；amount 用 BigDecimal 精确表示 */
data class DrugDoseInfo(
    val drug: DrugInfo,
    val amount: java.math.BigDecimal,
    val time: Long,
    /** 该次给药实际途径（浓度与游标线按此计算） */
    val route: Route? = null,
)

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

    /** 药物剂量单位到内部 mg 的换算 */
    private fun mgPerUnit(drug: DrugInfo): Double = when (drug.doseUnit) {
        com.fangshen.manual.model.DoseUnit.MG -> 1.0
        com.fangshen.manual.model.DoseUnit.G -> 1000.0
        com.fangshen.manual.model.DoseUnit.MCG -> 0.001
    }

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
        /** v5.5：来源药物的半衰期（小时，药名 -> t½），供详情抽屉显示叠加/消退时间 */
        val sourceHalfLives: Map<String, Double> = emptyMap(),
        /** v5.5：来源药物的最后一次服药时刻（epoch 毫秒），供详情抽屉显示距完整半衰期的剩余时间 */
        val sourceLastDose: Map<String, Long> = emptyMap(),
        /** 是否属于效应包的成员、因而不单独展示 */
        val absorbedByPack: Boolean = false,
        /** 展示用急慢性：同效应急慢都触发时只显示急性（权重已叠加） */
        val displayChronic: Boolean = false,
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
        profileSex: String? = null,
        profileWeightKg: Int? = null,
    ): Result {
        val weights = mutableMapOf<String, Int>()
        val sources = mutableMapOf<String, MutableSet<String>>()
        val weightBySource = mutableMapOf<String, MutableMap<String, Int>>()
        // 该效应至少有一条急性规则触发（用于急/慢性去重展示）
        val hasAcute = mutableSetOf<String>()
        // v5.5：药名 -> (半衰期小时, 最近一次服药时刻)，供详情抽屉的时间提示
        val halfLifeByName = mutableMapOf<String, Double>()
        val lastDoseByName = mutableMapOf<String, Long>()
        for (d0 in doses) {
            halfLifeByName[d0.drug.name] = d0.drug.halfLifeHours
            val prev = lastDoseByName[d0.drug.name] ?: 0L
            if (d0.time > prev) lastDoseByName[d0.drug.name] = d0.time
        }
        // 成瘾游标状态（只算声明了成瘾性的药）
        val addictStates = AddictionEngine.statesAtNow(
            drugs = doses.map { it.drug }.distinct(),
            doses = doses.map { d ->
                DoseRecord(
                    drugId = d.drug.id,
                    amountMg = d.amount.toDouble() * mgPerUnit(d.drug),
                    route = d.route,
                    time = d.time,
                )
            },
            now = now,
        )

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
            var amount = if (recentSum > ZERO) recentSum else (midMax ?: ZERO)
            if (amount > ZERO) {
                // 成瘾游标调制（仅连续用药达门槛者）：换算剂量 = 原剂量 × |A−C| / A；
                // 浓度贴游标（耐受）→ 趋近 0；浓度远高于游标（未适应、首用敏感）→ 放大，但封顶 2 倍
                val st = addictStates[drug.id]
                if (st != null && st.cursor > 1e-9) {
                    val raw = kotlin.math.abs(st.concentration - st.cursor) / st.cursor
                    val ratio = if (st.concentration > st.cursor) minOf(raw, 2.0) else raw
                    amount = amount.multiply(java.math.BigDecimal.valueOf(ratio))
                    if (st.concentration < 0.5 * st.cursor) {
                        // 明显低于游标线：戒断反应加权
                        val w = (((st.cursor - st.concentration) / st.cursor) * 3.0).toInt().coerceAtLeast(1)
                        add("戒断", w, drug.name)
                    }
                }
                if (amount > ZERO) {
                    effectiveAmount[drug] = amount
                    activeDrugs += drug
                }
                // 戒断状态下药物仍视为"生效"（用于联用预警），即使换算剂量归零
                if (amount <= ZERO && st != null) activeDrugs += drug
            }
        }

        // 2. 效应线规则（阈值按换算后的剂量判断）
        for ((drug, amount) in effectiveAmount) {
            for (rule in drug.rules) {
                if (amount > rule.threshold) {
                    add(rule.effectName, rule.weight, drug.name)
                    if (!rule.isChronic) hasAcute += rule.effectName
                }
            }
        }

        // 3. 预警规则：联用药物或特定画像下额外增加权重
        if (alertRules.isNotEmpty()) {
            for (rule in alertRules) {
                if (!rule.enabled) continue
                if (activeDrugs.none { it.matches(rule.drug) }) continue
                // v5 画像条件先过滤
                if (rule.conditionSex != null && profileSex != rule.conditionSex) continue
                if (rule.conditionWeightMin != null &&
                    (profileWeightKg == null || profileWeightKg < rule.conditionWeightMin)) continue
                if (rule.conditionWeightMax != null &&
                    (profileWeightKg == null || profileWeightKg > rule.conditionWeightMax)) continue
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
                    sourceHalfLives = ((memberSources[member] ?: emptySet()).toList()).associate { it to (halfLifeByName[it] ?: 0.0) },
                    sourceLastDose = ((memberSources[member] ?: emptySet()).toList()).associate { it to (lastDoseByName[it] ?: 0L) },
                    sourceWeights = memberSourceWeights[member] ?: emptyMap(),
                )
            }
            groups += EffectGroup(
                name = pack,
                weight = packWeight,
                severity = EffectSeverity.fromWeight(packWeight),
                isPack = true,
                displayChronic = pack !in hasAcute && (EffectCatalog.def(pack)?.urgency == EffectUrgency.CHRONIC),
                def = EffectCatalog.def(pack),
                members = memberGroups,
                sources = (sources[pack] ?: emptySet()).toList(),
                sourceHalfLives = ((sources[pack] ?: emptySet()).toList()).associate { it to (halfLifeByName[it] ?: 0.0) },
                sourceLastDose = ((sources[pack] ?: emptySet()).toList()).associate { it to (lastDoseByName[it] ?: 0L) },
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
                displayChronic = name !in hasAcute && (def?.urgency == EffectUrgency.CHRONIC),
                def = def,
                members = emptyList(),
                sources = (sources[name] ?: emptySet()).toList(),
                sourceHalfLives = ((sources[name] ?: emptySet()).toList()).associate { it to (halfLifeByName[it] ?: 0.0) },
                sourceLastDose = ((sources[name] ?: emptySet()).toList()).associate { it to (lastDoseByName[it] ?: 0L) },
                sourceWeights = weightBySource[name] ?: emptyMap(),
            )
        }

        return Result(groups = groups, activeDrugs = activeDrugs)
    }
}

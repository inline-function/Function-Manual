package com.fangshen.manual.engine

import com.fangshen.manual.model.DrugInfo

/**
 * 相互作用检查引擎。
 *
 * 检查某个精神活性物质是否与其他当前生效的物质存在相互作用：
 * - 酶抑制：使共用代谢酶的其他物质血药浓度升高；
 * - 酶诱导：使其降低；
 * - 共用底物：其他物质共用代谢酶，血药浓度上下浮动、方向不明；
 * - 药效学协同：两药机制叠加产生酶学无法解释的危险（如 MAOI 与 5-HT 能药物）；
 * - 负荷叠加：同类机制药物过多，累积风险上升（如多种中枢抑制药）。
 */
object InteractionEngine {

    enum class Kind(val label: String) {
        INHIBITION("抑制"),
        INDUCTION("诱导"),
        SUBSTRATE_COMPETITION("竞争代谢酶"),
        PHARMACODYNAMIC("药效学协同"),
        DRUG_BURDEN("负荷叠加"),
        CAUTIOUS("自定义预警"),
    }

    data class Warning(
        val drugName: String,
        val otherDrugName: String,
        val enzyme: String,
        val kind: Kind,
        val message: String,
    )

    // —— 机制标签集合（药物效应标签 / 主观标签）——
    private val MAOI_TAGS = setOf("单胺氧化酶抑制剂")
    private val SEROTONERGIC_TAGS = setOf(
        "选择性血清素再摄取抑制剂", "血清素再摄取抑制剂",
        "血清素和正肾素再摄取抑制剂", "血清素受体激动剂",
        "血清素前体", "单胺氧化酶抑制剂", "血清素能镇痛药",
        "血清素释放剂",  // MDMA、甲卡西酮、苯丙胺类等
    )
    private val SSRI_LIKE_TAGS = setOf(
        "选择性血清素再摄取抑制剂", "血清素再摄取抑制剂",
        "血清素和正肾素再摄取抑制剂",
    )
    private val OPIOID_TAGS = setOf("阿片类受体激动剂")
    private val GABA_TAGS = setOf("GABA受体激动剂", "GHB受体激动剂")
    private val STIMULANT_TAGS = setOf("中枢兴奋剂")
    private val NSAID_TAGS = setOf("非甾体抗炎药")

    /** 利尿剂标签（降压药族中的噻嗪类等，降低锂清除率） */
    private val DIURETIC_TAGS = setOf("利尿剂")

    /** 双硫仑样反应药：头孢哌酮等含 MTT 侧链头孢、甲硝唑等，抑制乙醛脱氢酶 */
    private val DISULFIRAM_TAGS = setOf("双硫仑样反应药")
    private val LITHIUM_TAGS = setOf("锂盐")
    private val ANTICHOLINERGIC_TAGS = setOf("抗胆碱能剂")
    private val CNS_DEPRESSANT_TAGS = GABA_TAGS + OPIOID_TAGS + setOf("乙醇", "镇静剂")
    private val ETHANOL_TAGS = setOf("乙醇")
    private val APAP_TAGS = setOf("对乙酰氨基酚类")

    /** 用户自定义配对（匹配串可为药名或标签） */
    private var customPairs: List<com.fangshen.manual.model.CustomInteraction> = emptyList()

    fun setCustomPairs(pairs: List<com.fangshen.manual.model.CustomInteraction>) {
        customPairs = pairs
    }

    private fun matchesCustom(drug: DrugInfo, matcher: String): Boolean {
        val m = matcher.trim()
        if (m.isEmpty()) return false
        return drug.name == m || drug.name.contains(m) || (drug.subjectiveTags + drug.effectTags).any {
            it == m || it.contains(m)
        }
    }

    /** 药效学危险配对：一侧具备 aTags 中任一标签，另一侧具备 bTags 中任一标签 */
    data class MechanismPair(
        val aTags: Set<String>,
        val bTags: Set<String>,
        val title: String,
        val message: (String, String) -> String,
    ) {
        val firstSide: String get() = aTags.first()
        val secondSide: String get() = bTags.first()
    }

    val MECHANISM_PAIRS = listOf(
        MechanismPair(
            MAOI_TAGS, SEROTONERGIC_TAGS, "5-羟色胺毒性",
            { a, b -> "$a 与 $b 联用：单胺氧化酶抑制剂阻断 5-羟色胺降解，再叠加 5-HT 能药物，" +
                "可引发 5-羟色胺综合征（发热、肌阵挛、谵妄、出汗、心动过速），属内科急症，必须停药就医。" },
        ),
        MechanismPair(
            OPIOID_TAGS, GABA_TAGS, "协同性呼吸抑制",
            { a, b -> "$a 与 $b 联用：阿片类与 GABA 能药物（苯二氮卓类、唑吡坦类等）抑制不同的中枢环节，" +
                "可产生协同性呼吸抑制，联用时应减量并密切观察呼吸。" },
        ),
        MechanismPair(
            STIMULANT_TAGS, MAOI_TAGS, "高血压危象",
            { a, b -> "$a 与 $b 联用：单胺氧化酶抑制剂使去甲肾上腺素无法降解，拟交感/中枢兴奋药可促发" +
                "高血压危象（剧烈头痛、血压飙升），属急症。" },
        ),
        MechanismPair(
            NSAID_TAGS + DIURETIC_TAGS, LITHIUM_TAGS, "锂盐蓄积",
            { a, b -> "$a 与 $b 联用：非甾体抗炎药或噻嗪类利尿剂降低锂的肾清除率，可使血锂升至中毒水平，" +
                "联用期间应监测血锂与肾功能。" },
        ),
        MechanismPair(
            DISULFIRAM_TAGS, ETHANOL_TAGS, "双硫仑样反应",
            { a, b -> "$a 期间饮酒：乙醛脱氢酶被抑制，乙醛蓄积导致面部潮红、恶心呕吐、心动过速" +
                "与低血压，严重时危及生命。停药后酶活性恢复需数天，期间仍须禁酒（含藿香正气水等含醇制剂）。" },
        ),
        MechanismPair(
            setOf("血清素能镇痛药"), SSRI_LIKE_TAGS, "5-羟色胺能与癫痫风险",
            { a, b -> "$a 与 $b 联用：曲马多/奈福泮类本身抑制 5-羟色胺再摄取，与抗抑郁药叠加可增加" +
                "5-羟色胺综合征与癫痫发作风险。" },
        ),
        MechanismPair(
            ETHANOL_TAGS, APAP_TAGS, "肝损伤",
            { a, b -> "$a 与 $b 联用：乙醇诱导肝药酶并耗竭谷胱甘肽，与对乙酰氨基酚联用可加重肝损伤，" +
                "饮酒后应避免服用高剂量对乙酰氨基酚。" },
        ),
    )

    /** 内置联用预警条目（预警页面向用户展示）：标题 + 普适措辞说明 */
    val BUILTIN_INTERACTIONS: List<Pair<String, String>> = MECHANISM_PAIRS.map { pair ->
        pair.title to pair.message(pair.firstSide, pair.secondSide)
    }

    /** 同类机制药物过多时的负荷叠加警告 */
    private data class BurdenRule(
        val tags: Set<String>,
        val threshold: Int,
        val title: String,
        val message: (names: String) -> String,
    )

    private val BURDEN_RULES = listOf(
        BurdenRule(
            SEROTONERGIC_TAGS, 2, "5-羟色胺能负荷",
            { names -> "当前同时生效的 5-羟色胺能药物有：$names。多药叠加可显著增加 5-羟色胺综合征风险，" +
                "如出现发热、肌阵挛、出汗、谵妄请立即就医。" },
        ),
        BurdenRule(
            ANTICHOLINERGIC_TAGS, 2, "抗胆碱能负荷",
            { names -> "当前同时生效的抗胆碱能药物有：$names。叠加增加谵妄与抗胆碱能综合征风险，" +
                "老年人尤须警惕，长期还影响认知。" },
        ),
        BurdenRule(
            CNS_DEPRESSANT_TAGS, 3, "中枢抑制负荷",
            { names -> "当前同时生效的中枢抑制性药物有：$names。镇静、跌倒与呼吸抑制风险叠加，" +
                "夜间起床与高处活动需特别小心。" },
        ),
    )

    private fun DrugInfo.tagSet(): Set<String> = (subjectiveTags + effectTags).toSet()

    /**
     * 检查 [drugs]（当前生效的药物）之间的相互作用，返回若干个警告。
     */
    fun check(drugs: List<DrugInfo>): List<Warning> {
        val warnings = mutableListOf<Warning>()
        val seen = mutableSetOf<Pair<String, String>>()
        for (drug in drugs) {
            for (other in drugs) {
                if (drug.id == other.id) continue
                val pairKey = if (drug.name < other.name) drug.name to other.name else other.name to drug.name
                // 抑制剂/诱导剂与底物方向明确，重复检查用 (drug, other) 作键
                val directionalKey = drug.name to other.name
                // 用户自定义配对
                for (cp in customPairs) {
                    if (!cp.enabled) continue
                    val aHit = matchesCustom(drug, cp.matcherA) && matchesCustom(other, cp.matcherB)
                    val bHit = matchesCustom(drug, cp.matcherB) && matchesCustom(other, cp.matcherA)
                    if (!(aHit || bHit)) continue
                    if (pairKey in seen || directionalKey in seen) continue
                    warnings += Warning(
                        drugName = drug.name,
                        otherDrugName = other.name,
                        enzyme = "自定义",
                        kind = Kind.CAUTIOUS,
                        message = cp.message.ifBlank { "两药联用存在用户标记的风险。" },
                    )
                    seen += pairKey
                }
                if (directionalKey in seen) continue

                for (enzyme in drug.inhibitors.intersect(other.substrates.toSet())) {
                    warnings += Warning(
                        drugName = drug.name,
                        otherDrugName = other.name,
                        enzyme = enzyme,
                        kind = Kind.INHIBITION,
                        message = "${drug.name} 抑制 ${enzyme}，${other.name} 经该酶代谢，" +
                            "合用可能使 ${other.name} 的血药浓度升高。",
                    )
                    seen += directionalKey
                }
                for (enzyme in drug.inducers.intersect(other.substrates.toSet())) {
                    warnings += Warning(
                        drugName = drug.name,
                        otherDrugName = other.name,
                        enzyme = enzyme,
                        kind = Kind.INDUCTION,
                        message = "${drug.name} 诱导 ${enzyme}，${other.name} 经该酶代谢，" +
                            "合用可能使 ${other.name} 的血药浓度降低。",
                    )
                    seen += directionalKey
                }
                val shared = drug.substrates.intersect(other.substrates.toSet())
                if (pairKey !in seen && shared.isNotEmpty() && directionalKey !in seen) {
                    warnings += Warning(
                        drugName = drug.name,
                        otherDrugName = other.name,
                        enzyme = shared.first(),
                        kind = Kind.SUBSTRATE_COMPETITION,
                        message = "${drug.name} 与 ${other.name} 共用代谢酶（${shared.first()}" +
                            (if (shared.size > 1) " 等" else "") + "），合用可能使血药浓度上下浮动，方向不明。",
                    )
                    seen += pairKey
                }
            }
        }

        // 药效学危险配对（两个方向都检查）
        val pairSeen = mutableSetOf<Pair<String, String>>()
        for (drug in drugs) {
            for (other in drugs) {
                if (drug.id == other.id) continue
                val key = if (drug.name < other.name) drug.name to other.name else other.name to drug.name
                if (key in pairSeen) continue
                for (pair in MECHANISM_PAIRS) {
                    val drugTags = drug.tagSet()
                    val otherTags = other.tagSet()
                    val forward = drugTags.any { it in pair.aTags } && otherTags.any { it in pair.bTags }
                    val reverse = drugTags.any { it in pair.bTags } && otherTags.any { it in pair.aTags }
                    if (!forward && !reverse) continue
                    warnings += Warning(
                        drugName = drug.name,
                        otherDrugName = other.name,
                        enzyme = pair.title,
                        kind = Kind.PHARMACODYNAMIC,
                        message = pair.message(drug.name, other.name),
                    )
                    pairSeen += key
                }
            }
        }

        // 同类机制负荷叠加
        for (rule in BURDEN_RULES) {
            val matched = drugs.filter { it.tagSet().any { tag -> tag in rule.tags } }
            if (matched.size >= rule.threshold) {
                warnings += Warning(
                    drugName = matched.joinToString("、") { it.name },
                    otherDrugName = "",
                    enzyme = rule.title,
                    kind = Kind.DRUG_BURDEN,
                    message = rule.message(matched.joinToString("、") { it.name }),
                )
            }
        }

        return warnings
    }
}
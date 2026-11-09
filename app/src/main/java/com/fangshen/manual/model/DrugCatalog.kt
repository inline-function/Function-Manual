package com.fangshen.manual.model

import java.math.BigDecimal

/**
 * 内置药物目录（领域参考数据）。
 *
 * 注意：这些条目的血药浓度与效应推算均为大幅简化的示意模型，数据不完整且个体差异大，
 * 置信度很低，仅作参考之用，主要价值在于记录服药行为本身。
 */
object DrugCatalog {

    private fun d(
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
    ) = DrugInfo(
        id = "builtin:$key", name = name, colorArgb = colorArgb,
        tabletDose = tabletDose, doseUnit = doseUnit, managementClass = managementClass,
        subjectiveTags = subjectiveTags, effectTags = effectTags, moleculeTag = moleculeTag,
        halfLifeHours = halfLifeHours, bioavailabilityPct = bioavailabilityPct, route = route,
        tmaxHours = tmaxHours, substrates = substrates, inhibitors = inhibitors, inducers = inducers,
        rules = rules, neuros = neuros, isBuiltin = true,
    )

    private val fluvoxamine = d(
        key = "fluvoxamine", name = "氟伏沙明", colorArgb = 0xFFC9A227, tabletDose = 50.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂", "抗强迫剂"),
        effectTags = listOf("选择性血清素再摄取抑制剂", "血清素再摄取抑制剂"),
        moleculeTag = "", halfLifeHours = 15.0, bioavailabilityPct = 53, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("CYP2D6", "CYP1A2"),
        rules = listOf(
            DoseEffectRule(50.0.toBigDecimal(), "愉悦", 2),
            DoseEffectRule(50.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(100.0.toBigDecimal(), "释然", 3),
            DoseEffectRule(300.0.toBigDecimal(), "血清素综合征", 1),
            DoseEffectRule(1000.0.toBigDecimal(), "血清素综合征", 1),
            DoseEffectRule(1500.0.toBigDecimal(), "血清素综合征", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.SEROTONIN, 1)),
    )

    private val sertraline = d(
        key = "sertraline", name = "舍曲林", colorArgb = 0xFFD9A62E, tabletDose = 50.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂"),
        effectTags = listOf("选择性血清素再摄取抑制剂"),
        moleculeTag = "", halfLifeHours = 26.0, bioavailabilityPct = 44, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2D6", "CYP2B6", "CYP2C19"),
        inhibitors = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(50.0.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(100.0.toBigDecimal(), "释然", 2),
            DoseEffectRule(200.0.toBigDecimal(), "血清素综合征", 1),
            DoseEffectRule(400.0.toBigDecimal(), "血清素综合征", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.SEROTONIN, 1)),
    )

    private val paroxetine = d(
        key = "paroxetine", name = "帕罗西汀", colorArgb = 0xFFB8860B, tabletDose = 20.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂"),
        effectTags = listOf("选择性血清素再摄取抑制剂"),
        moleculeTag = "", halfLifeHours = 21.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 5.0,
        substrates = listOf("CYP2D6"),
        inhibitors = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(10.0.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(10.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(30.0.toBigDecimal(), "释然", 2),
            DoseEffectRule(60.0.toBigDecimal(), "血清素综合征", 1),
            DoseEffectRule(120.0.toBigDecimal(), "血清素综合征", 1),
        ),
        // 帕罗西汀可显著增加血清素的权重
        neuros = listOf(NeuroDelta(Neurotransmitters.SEROTONIN, 2)),
    )

    private val alprazolam = d(
        key = "alprazolam", name = "阿普唑仑", colorArgb = 0xFF1565C0, tabletDose = 0.4.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("抗焦虑剂", "镇静剂", "记忆抑制剂"),
        effectTags = listOf("苯二氮卓类受体激动剂", "GABA受体激动剂"),
        moleculeTag = "苯二氮卓类", halfLifeHours = 11.0, bioavailabilityPct = 80, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(0.4.toBigDecimal(), "镇静", 2),
            DoseEffectRule(0.4.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(1.0.toBigDecimal(), "释然", 2),
            DoseEffectRule(2.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(4.0.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GABA, 1)),
    )

    private val ketamine = d(
        key = "ketamine", name = "氯胺酮", colorArgb = 0xFF00838F, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("解离剂", "抗抑郁剂"),
        effectTags = listOf("NMDA受体拮抗剂"),
        moleculeTag = "芳基环己胺类", halfLifeHours = 2.5, bioavailabilityPct = 50,
        route = Route.INSUFFLATION, tmaxHours = 1.0,
        substrates = listOf("CYP2B6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(30.0.toBigDecimal(), "解离", 1),
            DoseEffectRule(30.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(100.0.toBigDecimal(), "解离", 2),
            DoseEffectRule(100.0.toBigDecimal(), "镇静", 2),
            DoseEffectRule(200.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(500.0.toBigDecimal(), "呼吸抑制", 1),
        ),
        // 氯胺酮可减少谷氨酸的权重
        neuros = listOf(NeuroDelta(Neurotransmitters.GLUTAMATE, -2)),
    )

    private val trihexyphenidyl = d(
        key = "trihexyphenidyl", name = "苯海索", colorArgb = 0xFFE65100, tabletDose = 2.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("谵妄剂"),
        effectTags = listOf("抗胆碱能剂"),
        moleculeTag = "", halfLifeHours = 3.5, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(2.0.toBigDecimal(), "口干", 1),
            DoseEffectRule(6.0.toBigDecimal(), "便秘", 1),
            DoseEffectRule(6.0.toBigDecimal(), "视物模糊", 1),
            DoseEffectRule(10.0.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(10.0.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(15.0.toBigDecimal(), "抗胆碱能综合征", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -2)),
    )

    private val venlafaxine = d(
        key = "venlafaxine", name = "文拉法辛", colorArgb = 0xFF6A1B9A, tabletDose = 75.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂"),
        moleculeTag = "", halfLifeHours = 5.0, bioavailabilityPct = 45, route = Route.ORAL,
        tmaxHours = 2.5,
        substrates = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(37.5.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(75.0.toBigDecimal(), "释然", 1),
            DoseEffectRule(150.0.toBigDecimal(), "血清素综合征", 1),
            DoseEffectRule(225.0.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(225.0.toBigDecimal(), "高血压", 1),
            DoseEffectRule(300.0.toBigDecimal(), "血清素综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 2),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
        ),
    )

    private val morphine = d(
        key = "morphine", name = "吗啡", colorArgb = 0xFFB71C1C, tabletDose = 10.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.NARCOTIC,
        subjectiveTags = listOf("镇痛剂", "梦境增强剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "阿片类", halfLifeHours = 2.0, bioavailabilityPct = 25, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("UGT2B7", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(10.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(10.0.toBigDecimal(), "镇静", 2),
            DoseEffectRule(10.0.toBigDecimal(), "恶心", 1),
            DoseEffectRule(10.0.toBigDecimal(), "便秘", 1),
            DoseEffectRule(60.0.toBigDecimal(), "瞳孔缩小", 1),
            DoseEffectRule(60.0.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(60.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(120.0.toBigDecimal(), "阿片类中毒", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.ENDORPHIN, 2)),
    )

    private val lsd = d(
        key = "lsd", name = "麦角二乙胺", colorArgb = 0xFF651FFF, tabletDose = null,
        doseUnit = DoseUnit.MCG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("迷幻剂"),
        effectTags = listOf("血清素受体激动剂"),
        moleculeTag = "麦角林类", halfLifeHours = 5.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(20.0.toBigDecimal(), "解离", 1),
            DoseEffectRule(20.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(100.0.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(100.0.toBigDecimal(), "出汗", 1),
            DoseEffectRule(200.0.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.SEROTONIN, 2)),
    )

    private val caffeine = d(
        key = "caffeine", name = "咖啡因", colorArgb = 0xFF795548, tabletDose = 0.1.toBigDecimal(),
        doseUnit = DoseUnit.G, managementClass = ManagementClass.SPECIAL,
        subjectiveTags = listOf("中枢兴奋剂"),
        effectTags = emptyList(),
        moleculeTag = "黄嘌呤类", halfLifeHours = 5.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP1A2"),
        rules = listOf(
            DoseEffectRule(0.05.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(0.1.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(0.2.toBigDecimal(), "失眠", 1),
            DoseEffectRule(0.2.toBigDecimal(), "焦虑", 1),
        ),
    )

    private val fluoxetine = d(
        key = "fluoxetine", name = "氟西汀", colorArgb = 0xFFD4A017, tabletDose = 20.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂", "抗强迫剂"),
        effectTags = listOf("选择性血清素再摄取抑制剂"),
        moleculeTag = "三环酮类", halfLifeHours = 96.0, bioavailabilityPct = 72, route = Route.ORAL,
        tmaxHours = 8.0,
        substrates = listOf("CYP2D6", "CYP2C19", "CYP3A4"),
        inhibitors = listOf("CYP2D6", "CYP2C19", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(20.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(20.toBigDecimal(), "释然", 1),
            DoseEffectRule(40.toBigDecimal(), "释然", 2),
            DoseEffectRule(60.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(80.toBigDecimal(), "血清素综合征", 1),
            DoseEffectRule(120.toBigDecimal(), "血清素综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val citalopram = d(
        key = "citalopram", name = "西酞普兰", colorArgb = 0xFFC49D24, tabletDose = 20.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂"),
        effectTags = listOf("选择性血清素再摄取抑制剂"),
        moleculeTag = "双环类", halfLifeHours = 35.0, bioavailabilityPct = 80, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2C19", "CYP2D6", "CYP3A4"),
        inhibitors = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(20.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(20.toBigDecimal(), "释然", 1),
            DoseEffectRule(40.toBigDecimal(), "释然", 2),
            DoseEffectRule(60.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(80.toBigDecimal(), "血清素综合征", 1),
            DoseEffectRule(120.toBigDecimal(), "血清素综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val escitalopram = d(
        key = "escitalopram", name = "艾司西酞普兰", colorArgb = 0xFFDDA535, tabletDose = 10.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂", "抗强迫剂"),
        effectTags = listOf("选择性血清素再摄取抑制剂"),
        moleculeTag = "双环类", halfLifeHours = 27.0, bioavailabilityPct = 80, route = Route.ORAL,
        tmaxHours = 5.0,
        substrates = listOf("CYP2C19", "CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(10.toBigDecimal(), "释然", 1),
            DoseEffectRule(20.toBigDecimal(), "释然", 2),
            DoseEffectRule(30.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(40.toBigDecimal(), "血清素综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val duloxetine = d(
        key = "duloxetine", name = "度洛西汀", colorArgb = 0xFF456B8A, tabletDose = 30.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂"),
        moleculeTag = "", halfLifeHours = 12.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 6.0,
        substrates = listOf("CYP2D6", "CYP1A2", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(30.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(30.toBigDecimal(), "释然", 1),
            DoseEffectRule(60.toBigDecimal(), "释然", 2),
            DoseEffectRule(60.toBigDecimal(), "镇静", 1),
            DoseEffectRule(120.toBigDecimal(), "出汗", 1),
            DoseEffectRule(120.toBigDecimal(), "血清素综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
        ),
    )

    private val bupropion = d(
        key = "bupropion", name = "安非他酮", colorArgb = 0xFF4E7A9E, tabletDose = 150.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "中枢兴奋剂"),
        effectTags = listOf("去甲肾上腺素与多巴胺再摄取抑制剂"),
        moleculeTag = "氨基酮类", halfLifeHours = 21.0, bioavailabilityPct = 85, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("CYP2B6", "CYP2D6"),
        inhibitors = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(150.toBigDecimal(), "专注", 1),
            DoseEffectRule(150.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(300.toBigDecimal(), "欣快", 1),
            DoseEffectRule(300.toBigDecimal(), "失眠", 1),
            DoseEffectRule(450.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(450.toBigDecimal(), "分心", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
        ),
    )

    private val mirtazapine = d(
        key = "mirtazapine", name = "米氮平", colorArgb = 0xFF3E6A88, tabletDose = 15.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "镇静剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂"),
        moleculeTag = "哌嗪氮䓬类", halfLifeHours = 30.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP1A2", "CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(15.toBigDecimal(), "镇静", 2),
            DoseEffectRule(15.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(30.toBigDecimal(), "释然", 2),
            DoseEffectRule(30.toBigDecimal(), "镇静", 2),
            DoseEffectRule(45.toBigDecimal(), "释然", 2),
            DoseEffectRule(45.toBigDecimal(), "镇静", 1),
            DoseEffectRule(60.toBigDecimal(), "嗜睡", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
        ),
    )

    private val trazodone = d(
        key = "trazodone", name = "曲唑酮", colorArgb = 0xFF557F9C, tabletDose = 50.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "镇静剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂"),
        moleculeTag = "三唑并吡啶类", halfLifeHours = 7.0, bioavailabilityPct = 65, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP3A4", "CYP2D6"),
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "镇静", 2),
            DoseEffectRule(50.toBigDecimal(), "镇静", 2),
            DoseEffectRule(100.toBigDecimal(), "释然", 1),
            DoseEffectRule(200.toBigDecimal(), "释然", 2),
            DoseEffectRule(300.toBigDecimal(), "镇静", 3),
            DoseEffectRule(300.toBigDecimal(), "恍惚", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
        ),
    )

    private val vortioxetine = d(
        key = "vortioxetine", name = "伏硫西汀", colorArgb = 0xFF39617F, tabletDose = 10.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂"),
        effectTags = listOf("选择性血清素再摄取抑制剂"),
        moleculeTag = "", halfLifeHours = 66.0, bioavailabilityPct = 75, route = Route.ORAL,
        tmaxHours = 8.0,
        substrates = listOf("CYP2D6", "CYP2C19", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(5.toBigDecimal(), "专注", 1),
            DoseEffectRule(10.toBigDecimal(), "释然", 2),
            DoseEffectRule(20.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(20.toBigDecimal(), "血清素综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val tianeptine = d(
        key = "tianeptine", name = "噻奈普汀", colorArgb = 0xFF5E87A6, tabletDose = 12.5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂"),
        effectTags = listOf("血清素再摄取抑制剂"),
        moleculeTag = "三环类（非经典）", halfLifeHours = 2.5, bioavailabilityPct = 99, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(12.5.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(12.5.toBigDecimal(), "释然", 1),
            DoseEffectRule(25.toBigDecimal(), "释然", 2),
            DoseEffectRule(37.5.toBigDecimal(), "欣快", 1),
            DoseEffectRule(50.toBigDecimal(), "欣快", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val reboxetine = d(
        key = "reboxetine", name = "瑞波西汀", colorArgb = 0xFF4A7090, tabletDose = 4.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂"),
        effectTags = listOf("去甲肾上腺素再摄取抑制剂"),
        moleculeTag = "", halfLifeHours = 12.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(4.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(4.toBigDecimal(), "专注", 1),
            DoseEffectRule(8.toBigDecimal(), "释然", 1),
            DoseEffectRule(12.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(12.toBigDecimal(), "失眠", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
        ),
    )

    private val milnacipran = d(
        key = "milnacipran", name = "米那普仑", colorArgb = 0xFF6488A8, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂"),
        moleculeTag = "", halfLifeHours = 8.0, bioavailabilityPct = 85, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(25.toBigDecimal(), "释然", 1),
            DoseEffectRule(50.toBigDecimal(), "释然", 2),
            DoseEffectRule(100.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(200.toBigDecimal(), "血清素综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
        ),
    )

    private val amitriptyline = d(
        key = "amitriptyline", name = "阿米替林", colorArgb = 0xFFB4552E, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "镇静剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂", "抗胆碱能剂"),
        moleculeTag = "三环类", halfLifeHours = 16.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2D6", "CYP2C19", "CYP1A2", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "镇静", 2),
            DoseEffectRule(25.toBigDecimal(), "口干", 1),
            DoseEffectRule(50.toBigDecimal(), "释然", 2),
            DoseEffectRule(50.toBigDecimal(), "便秘", 1),
            DoseEffectRule(75.toBigDecimal(), "镇静", 2),
            DoseEffectRule(100.toBigDecimal(), "视物模糊", 1),
            DoseEffectRule(100.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(150.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(150.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -2),
        ),
    )

    private val imipramine = d(
        key = "imipramine", name = "丙咪嗪", colorArgb = 0xFFC06034, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂", "抗胆碱能剂"),
        moleculeTag = "三环类", halfLifeHours = 12.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2C19", "CYP2D6", "CYP1A2", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "镇静", 1),
            DoseEffectRule(25.toBigDecimal(), "口干", 1),
            DoseEffectRule(50.toBigDecimal(), "释然", 2),
            DoseEffectRule(75.toBigDecimal(), "镇静", 2),
            DoseEffectRule(100.toBigDecimal(), "便秘", 1),
            DoseEffectRule(100.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(150.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(200.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -2),
        ),
    )

    private val clomipramine = d(
        key = "clomipramine", name = "氯米帕明", colorArgb = 0xFFB85F3A, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗强迫剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂", "抗胆碱能剂"),
        moleculeTag = "三环类", halfLifeHours = 19.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2C19", "CYP2D6", "CYP1A2", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "释然", 2),
            DoseEffectRule(25.toBigDecimal(), "口干", 1),
            DoseEffectRule(50.toBigDecimal(), "强迫", -1),
            DoseEffectRule(75.toBigDecimal(), "镇静", 2),
            DoseEffectRule(100.toBigDecimal(), "释然", 2),
            DoseEffectRule(150.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(200.toBigDecimal(), "血清素综合征", 1),
            DoseEffectRule(225.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val doxepin = d(
        key = "doxepin", name = "多塞平", colorArgb = 0xFFA04A26, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "镇静剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂", "抗胆碱能剂"),
        moleculeTag = "三环类", halfLifeHours = 15.0, bioavailabilityPct = 30, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2C19", "CYP2D6", "CYP1A2"),
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "镇静", 2),
            DoseEffectRule(10.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(50.toBigDecimal(), "释然", 2),
            DoseEffectRule(50.toBigDecimal(), "口干", 1),
            DoseEffectRule(75.toBigDecimal(), "镇静", 3),
            DoseEffectRule(100.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(150.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
        ),
    )

    private val nortriptyline = d(
        key = "nortriptyline", name = "去甲替林", colorArgb = 0xFFC96B3C, tabletDose = 10.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂", "抗胆碱能剂"),
        moleculeTag = "三环类", halfLifeHours = 30.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2D6", "CYP2C19"),
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "镇静", 1),
            DoseEffectRule(25.toBigDecimal(), "释然", 2),
            DoseEffectRule(50.toBigDecimal(), "镇静", 2),
            DoseEffectRule(75.toBigDecimal(), "口干", 1),
            DoseEffectRule(75.toBigDecimal(), "便秘", 1),
            DoseEffectRule(100.toBigDecimal(), "抗胆碱能综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val maprotiline = d(
        key = "maprotiline", name = "马普替林", colorArgb = 0xFFCC7040, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "镇静剂"),
        effectTags = listOf("去甲肾上腺素再摄取抑制剂", "抗胆碱能剂"),
        moleculeTag = "四环类", halfLifeHours = 51.0, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 8.0,
        substrates = listOf("CYP2D6", "CYP2C19"),
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "镇静", 2),
            DoseEffectRule(50.toBigDecimal(), "释然", 1),
            DoseEffectRule(75.toBigDecimal(), "释然", 2),
            DoseEffectRule(100.toBigDecimal(), "镇静", 2),
            DoseEffectRule(150.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(225.toBigDecimal(), "肌阵挛", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val mianserin = d(
        key = "mianserin", name = "米安色林", colorArgb = 0xFFB46633, tabletDose = 30.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "镇静剂"),
        effectTags = listOf("血清素和正肾素再摄取抑制剂", "抗胆碱能剂"),
        moleculeTag = "四环类", halfLifeHours = 26.0, bioavailabilityPct = 30, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("CYP2D6", "CYP1A2", "CYP3A4", "CYP2C19"),
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "镇静", 2),
            DoseEffectRule(30.toBigDecimal(), "释然", 2),
            DoseEffectRule(45.toBigDecimal(), "镇静", 2),
            DoseEffectRule(60.toBigDecimal(), "释然", 1),
            DoseEffectRule(90.toBigDecimal(), "镇静", 3),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
        ),
    )

    private val phenelzine = d(
        key = "phenelzine", name = "苯乙肼", colorArgb = 0xFF7B1FA2, tabletDose = 15.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂"),
        effectTags = listOf("单胺氧化酶抑制剂"),
        moleculeTag = "肼类", halfLifeHours = 11.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 1.0,
        rules = listOf(
            DoseEffectRule(15.toBigDecimal(), "释然", 1),
            DoseEffectRule(30.toBigDecimal(), "欣快", 1),
            DoseEffectRule(30.toBigDecimal(), "失眠", 1),
            DoseEffectRule(45.toBigDecimal(), "欣快", 2),
            DoseEffectRule(60.toBigDecimal(), "高血压", 1),
            DoseEffectRule(75.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 2),
            NeuroDelta(Neurotransmitters.DOPAMINE, 2),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 2),
        ),
    )

    private val tranylcypromine = d(
        key = "tranylcypromine", name = "反苯环丙胺", colorArgb = 0xFF9C27B0, tabletDose = 10.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "中枢兴奋剂"),
        effectTags = listOf("单胺氧化酶抑制剂"),
        moleculeTag = "环丙胺类", halfLifeHours = 2.5, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 1.5,
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "专注", 2),
            DoseEffectRule(10.toBigDecimal(), "欣快", 1),
            DoseEffectRule(20.toBigDecimal(), "欣快", 2),
            DoseEffectRule(20.toBigDecimal(), "失眠", 1),
            DoseEffectRule(30.toBigDecimal(), "高血压", 1),
            DoseEffectRule(40.toBigDecimal(), "欣快", 3),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 2),
            NeuroDelta(Neurotransmitters.SEROTONIN, 2),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 2),
        ),
    )

    private val moclobemide = d(
        key = "moclobemide", name = "吗氯贝胺", colorArgb = 0xFF9575CD, tabletDose = 150.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂"),
        effectTags = listOf("单胺氧化酶抑制剂"),
        moleculeTag = "苄甲酰胺类", halfLifeHours = 2.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP2C19", "CYP2D6"),
        inhibitors = listOf("CYP2C19", "CYP2D6"),
        rules = listOf(
            DoseEffectRule(150.toBigDecimal(), "释然", 1),
            DoseEffectRule(150.toBigDecimal(), "欣快", 1),
            DoseEffectRule(300.toBigDecimal(), "欣快", 1),
            DoseEffectRule(300.toBigDecimal(), "失眠", 1),
            DoseEffectRule(450.toBigDecimal(), "欣快", 2),
            DoseEffectRule(600.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 2),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
            NeuroDelta(Neurotransmitters.DOPAMINE, 1),
        ),
    )

    private val selegiline = d(
        key = "selegiline", name = "司来吉兰", colorArgb = 0xFF8E5CD6, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("中枢兴奋剂"),
        effectTags = listOf("单胺氧化酶抑制剂"),
        moleculeTag = "丙炔胺类", halfLifeHours = 2.0, bioavailabilityPct = 10, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP2B6", "CYP2C19", "CYP1A2", "CYP2D6"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "专注", 1),
            DoseEffectRule(10.toBigDecimal(), "专注", 2),
            DoseEffectRule(10.toBigDecimal(), "欣快", 1),
            DoseEffectRule(20.toBigDecimal(), "欣快", 2),
            DoseEffectRule(20.toBigDecimal(), "失眠", 1),
            DoseEffectRule(30.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 2),
        ),
    )

    private val chlorpromazine = d(
        key = "chlorpromazine", name = "氯丙嗪", colorArgb = 0xFF3949AB, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂", "镇静剂"),
        effectTags = listOf("多巴胺受体拮抗剂", "抗胆碱能剂"),
        moleculeTag = "吩噻嗪类", halfLifeHours = 30.0, bioavailabilityPct = 32, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("CYP2D6", "CYP1A2", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "镇静", 2),
            DoseEffectRule(50.toBigDecimal(), "镇静", 2),
            DoseEffectRule(100.toBigDecimal(), "欣快", -2),
            DoseEffectRule(100.toBigDecimal(), "镇静", 3),
            DoseEffectRule(200.toBigDecimal(), "口干", 1),
            DoseEffectRule(300.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(400.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(400.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
        ),
    )

    private val haloperidol = d(
        key = "haloperidol", name = "氟哌啶醇", colorArgb = 0xFF303F9F, tabletDose = 2.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体拮抗剂"),
        moleculeTag = "丁酰苯类", halfLifeHours = 20.0, bioavailabilityPct = 65, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2D6", "CYP3A4"),
        inhibitors = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(2.toBigDecimal(), "欣快", -1),
            DoseEffectRule(2.toBigDecimal(), "专注", 1),
            DoseEffectRule(5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(5.toBigDecimal(), "欣快", -1),
            DoseEffectRule(10.toBigDecimal(), "欣快", -2),
            DoseEffectRule(15.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(20.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
        ),
    )

    private val perphenazine = d(
        key = "perphenazine", name = "奋乃静", colorArgb = 0xFF455CA0, tabletDose = 2.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体拮抗剂", "抗胆碱能剂"),
        moleculeTag = "吩噻嗪类", halfLifeHours = 10.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2C19", "CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(2.toBigDecimal(), "镇静", 1),
            DoseEffectRule(2.toBigDecimal(), "欣快", -1),
            DoseEffectRule(4.toBigDecimal(), "镇静", 1),
            DoseEffectRule(8.toBigDecimal(), "镇静", 2),
            DoseEffectRule(12.toBigDecimal(), "欣快", -2),
            DoseEffectRule(24.toBigDecimal(), "口干", 1),
            DoseEffectRule(40.toBigDecimal(), "抗胆碱能综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val fluphenazine = d(
        key = "fluphenazine", name = "氟奋乃静", colorArgb = 0xFF3E4F9E, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体拮抗剂", "抗胆碱能剂"),
        moleculeTag = "吩噻嗪类", halfLifeHours = 20.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2D6", "CYP1A2", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(1.toBigDecimal(), "镇静", 1),
            DoseEffectRule(5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(5.toBigDecimal(), "欣快", -1),
            DoseEffectRule(10.toBigDecimal(), "欣快", -2),
            DoseEffectRule(15.toBigDecimal(), "口干", 1),
            DoseEffectRule(20.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(20.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val thioridazine = d(
        key = "thioridazine", name = "硫利达嗪", colorArgb = 0xFF5C6BC0, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂", "镇静剂"),
        effectTags = listOf("多巴胺受体拮抗剂", "抗胆碱能剂"),
        moleculeTag = "吩噻嗪类", halfLifeHours = 10.0, bioavailabilityPct = 25, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2D6", "CYP1A2"),
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "镇静", 2),
            DoseEffectRule(50.toBigDecimal(), "镇静", 3),
            DoseEffectRule(50.toBigDecimal(), "口干", 1),
            DoseEffectRule(100.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(100.toBigDecimal(), "视物模糊", 1),
            DoseEffectRule(200.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(300.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val trifluoperazine = d(
        key = "trifluoperazine", name = "三氟拉嗪", colorArgb = 0xFF42508F, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体拮抗剂", "抗胆碱能剂"),
        moleculeTag = "吩噻嗪类", halfLifeHours = 13.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("CYP2D6", "CYP1A2", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(2.toBigDecimal(), "镇静", 1),
            DoseEffectRule(2.toBigDecimal(), "欣快", -1),
            DoseEffectRule(5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(10.toBigDecimal(), "欣快", -2),
            DoseEffectRule(20.toBigDecimal(), "口干", 1),
            DoseEffectRule(20.toBigDecimal(), "便秘", 1),
            DoseEffectRule(40.toBigDecimal(), "抗胆碱能综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val sulpiride = d(
        key = "sulpiride", name = "舒必利", colorArgb = 0xFF4A57A8, tabletDose = 100.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂", "抗抑郁剂"),
        effectTags = listOf("多巴胺受体拮抗剂"),
        moleculeTag = "苯甲酰胺类", halfLifeHours = 7.0, bioavailabilityPct = 30, route = Route.ORAL,
        tmaxHours = 4.0,
        rules = listOf(
            DoseEffectRule(100.toBigDecimal(), "欣快", -1),
            DoseEffectRule(100.toBigDecimal(), "释然", 1),
            DoseEffectRule(200.toBigDecimal(), "释然", 2),
            DoseEffectRule(200.toBigDecimal(), "镇静", 1),
            DoseEffectRule(400.toBigDecimal(), "镇静", 2),
            DoseEffectRule(600.toBigDecimal(), "欣快", -2),
            DoseEffectRule(800.toBigDecimal(), "焦虑", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
        ),
    )

    private val clozapine = d(
        key = "clozapine", name = "氯氮平", colorArgb = 0xFF5E35B1, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂", "镇静剂"),
        effectTags = listOf("多巴胺受体拮抗剂", "抗胆碱能剂"),
        moleculeTag = "二苯并二氮䓬类", halfLifeHours = 12.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 2.5,
        substrates = listOf("CYP1A2", "CYP3A4", "CYP2C19", "CYP2D6"),
        inhibitors = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(12.5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(25.toBigDecimal(), "镇静", 2),
            DoseEffectRule(25.toBigDecimal(), "口干", 1),
            DoseEffectRule(50.toBigDecimal(), "镇静", 2),
            DoseEffectRule(100.toBigDecimal(), "镇静", 3),
            DoseEffectRule(100.toBigDecimal(), "释然", 2),
            DoseEffectRule(150.toBigDecimal(), "口干", 2),
            DoseEffectRule(200.toBigDecimal(), "便秘", 2),
            DoseEffectRule(200.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(300.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(300.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(350.toBigDecimal(), "肌阵挛", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -1),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -2),
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val risperidone = d(
        key = "risperidone", name = "利培酮", colorArgb = 0xFF6A4FB5, tabletDose = 1.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体拮抗剂"),
        moleculeTag = "苯并异噁唑类", halfLifeHours = 20.0, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(0.5.toBigDecimal(), "欣快", -1),
            DoseEffectRule(0.5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(1.toBigDecimal(), "欣快", -2),
            DoseEffectRule(2.toBigDecimal(), "镇静", 1),
            DoseEffectRule(3.toBigDecimal(), "镇静", 2),
            DoseEffectRule(4.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(6.toBigDecimal(), "镇静", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val olanzapine = d(
        key = "olanzapine", name = "奥氮平", colorArgb = 0xFF7E57C2, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂", "镇静剂"),
        effectTags = listOf("多巴胺受体拮抗剂", "抗胆碱能剂"),
        moleculeTag = "噻吩并苯二氮䓬类", halfLifeHours = 30.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 5.0,
        substrates = listOf("CYP1A2", "CYP2D6", "CYP2C19", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(2.5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(2.5.toBigDecimal(), "释然", 1),
            DoseEffectRule(5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(5.toBigDecimal(), "欣快", -1),
            DoseEffectRule(10.toBigDecimal(), "镇静", 3),
            DoseEffectRule(10.toBigDecimal(), "口干", 1),
            DoseEffectRule(15.toBigDecimal(), "便秘", 1),
            DoseEffectRule(20.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(20.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
        ),
    )

    private val quetiapine = d(
        key = "quetiapine", name = "喹硫平", colorArgb = 0xFF9575CD, tabletDose = 100.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂", "镇静剂"),
        effectTags = listOf("多巴胺受体拮抗剂"),
        moleculeTag = "二苯并硫氮䓬类", halfLifeHours = 7.0, bioavailabilityPct = 9, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP3A4", "CYP2D6"),
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "镇静", 2),
            DoseEffectRule(50.toBigDecimal(), "镇静", 2),
            DoseEffectRule(100.toBigDecimal(), "镇静", 3),
            DoseEffectRule(100.toBigDecimal(), "释然", 1),
            DoseEffectRule(200.toBigDecimal(), "镇静", 3),
            DoseEffectRule(200.toBigDecimal(), "口干", 1),
            DoseEffectRule(300.toBigDecimal(), "镇静", 2),
            DoseEffectRule(400.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(450.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -1),
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
        ),
    )

    private val ziprasidone = d(
        key = "ziprasidone", name = "齐拉西酮", colorArgb = 0xFF7986CB, tabletDose = 20.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体拮抗剂"),
        moleculeTag = "苯并异噻唑类", halfLifeHours = 7.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 5.0,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "镇静", 1),
            DoseEffectRule(20.toBigDecimal(), "欣快", -1),
            DoseEffectRule(40.toBigDecimal(), "镇静", 2),
            DoseEffectRule(60.toBigDecimal(), "欣快", -2),
            DoseEffectRule(80.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(120.toBigDecimal(), "镇静", 2),
            DoseEffectRule(160.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val paliperidone = d(
        key = "paliperidone", name = "帕利哌酮", colorArgb = 0xFF6E7BC9, tabletDose = 3.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体拮抗剂"),
        moleculeTag = "苯并异噁唑类", halfLifeHours = 23.0, bioavailabilityPct = 28, route = Route.ORAL,
        tmaxHours = 24.0,
        rules = listOf(
            DoseEffectRule(1.5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(1.5.toBigDecimal(), "欣快", -1),
            DoseEffectRule(3.toBigDecimal(), "镇静", 2),
            DoseEffectRule(3.toBigDecimal(), "欣快", -2),
            DoseEffectRule(6.toBigDecimal(), "欣快", -2),
            DoseEffectRule(6.toBigDecimal(), "镇静", 2),
            DoseEffectRule(9.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(12.toBigDecimal(), "镇静", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
        ),
    )

    private val asenapine = d(
        key = "asenapine", name = "阿塞那林", colorArgb = 0xFF7C7BC9, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂", "镇静剂"),
        effectTags = listOf("多巴胺受体拮抗剂"),
        moleculeTag = "吡咯并吡啶类", halfLifeHours = 24.0, bioavailabilityPct = 35, route = Route.SUBLINGUAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP1A2", "CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(2.5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(2.5.toBigDecimal(), "欣快", -1),
            DoseEffectRule(5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(5.toBigDecimal(), "欣快", -2),
            DoseEffectRule(10.toBigDecimal(), "镇静", 3),
            DoseEffectRule(10.toBigDecimal(), "口干", 1),
            DoseEffectRule(20.toBigDecimal(), "镇静", 3),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
        ),
    )

    private val lurasidone = d(
        key = "lurasidone", name = "鲁拉西酮", colorArgb = 0xFF6F79C4, tabletDose = 40.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂", "镇静剂"),
        effectTags = listOf("多巴胺受体拮抗剂"),
        moleculeTag = "苯并异噻唑类", halfLifeHours = 20.0, bioavailabilityPct = 15, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("CYP3A4", "CYP2D6"),
        rules = listOf(
            DoseEffectRule(20.toBigDecimal(), "镇静", 2),
            DoseEffectRule(20.toBigDecimal(), "欣快", -1),
            DoseEffectRule(40.toBigDecimal(), "镇静", 2),
            DoseEffectRule(40.toBigDecimal(), "释然", 1),
            DoseEffectRule(60.toBigDecimal(), "欣快", -2),
            DoseEffectRule(80.toBigDecimal(), "镇静", 3),
            DoseEffectRule(80.toBigDecimal(), "恶心", 1),
            DoseEffectRule(120.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val amisulpride = d(
        key = "amisulpride", name = "氨磺必利", colorArgb = 0xFF5F6EC0, tabletDose = 50.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体拮抗剂"),
        moleculeTag = "苯甲酰胺类", halfLifeHours = 14.0, bioavailabilityPct = 48, route = Route.ORAL,
        tmaxHours = 4.0,
        rules = listOf(
            DoseEffectRule(50.toBigDecimal(), "欣快", -1),
            DoseEffectRule(50.toBigDecimal(), "释然", 1),
            DoseEffectRule(100.toBigDecimal(), "欣快", -1),
            DoseEffectRule(200.toBigDecimal(), "欣快", -1),
            DoseEffectRule(200.toBigDecimal(), "镇静", 1),
            DoseEffectRule(400.toBigDecimal(), "欣快", -2),
            DoseEffectRule(400.toBigDecimal(), "镇静", 2),
            DoseEffectRule(800.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(1200.toBigDecimal(), "镇静", 3),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -2),
        ),
    )

    private val aripiprazole = d(
        key = "aripiprazole", name = "阿立哌唑", colorArgb = 0xFF7C4DFF, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体部分激动剂"),
        moleculeTag = "喹诺酮类", halfLifeHours = 75.0, bioavailabilityPct = 87, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(2.toBigDecimal(), "欣快", -1),
            DoseEffectRule(2.toBigDecimal(), "专注", 1),
            DoseEffectRule(5.toBigDecimal(), "欣快", -1),
            DoseEffectRule(5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(10.toBigDecimal(), "欣快", -2),
            DoseEffectRule(15.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(15.toBigDecimal(), "失眠", 1),
            DoseEffectRule(20.toBigDecimal(), "镇静", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -1),
        ),
    )

    private val brexpiprazole = d(
        key = "brexpiprazole", name = "依匹哌唑", colorArgb = 0xFF8C63F2, tabletDose = 1.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体部分激动剂"),
        moleculeTag = "喹诺酮类", halfLifeHours = 91.0, bioavailabilityPct = 95, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(0.5.toBigDecimal(), "欣快", -1),
            DoseEffectRule(0.5.toBigDecimal(), "专注", 1),
            DoseEffectRule(1.toBigDecimal(), "欣快", -1),
            DoseEffectRule(2.toBigDecimal(), "欣快", -1),
            DoseEffectRule(2.toBigDecimal(), "镇静", 1),
            DoseEffectRule(4.toBigDecimal(), "欣快", -2),
            DoseEffectRule(6.toBigDecimal(), "焦虑", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -1),
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val cariprazine = d(
        key = "cariprazine", name = "卡利拉嗪", colorArgb = 0xFF9C7BF0, tabletDose = 1.5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗精神病剂"),
        effectTags = listOf("多巴胺受体部分激动剂"),
        moleculeTag = "哌嗪类", halfLifeHours = 336.0, bioavailabilityPct = 49, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4", "CYP2D6"),
        rules = listOf(
            DoseEffectRule(1.5.toBigDecimal(), "欣快", -1),
            DoseEffectRule(1.5.toBigDecimal(), "专注", 1),
            DoseEffectRule(3.toBigDecimal(), "欣快", -1),
            DoseEffectRule(3.toBigDecimal(), "欣快", -1),
            DoseEffectRule(4.5.toBigDecimal(), "欣快", -2),
            DoseEffectRule(4.5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(6.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(6.toBigDecimal(), "失眠", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -1),
        ),
    )

    private val lithium = d(
        key = "lithium", name = "碳酸锂", colorArgb = 0xFF0097A7, tabletDose = 300.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心境稳定剂"),
        effectTags = listOf("锂盐"),
        moleculeTag = "锂盐", halfLifeHours = 24.0, bioavailabilityPct = 100, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(300.toBigDecimal(), "释然", 1),
            DoseEffectRule(300.toBigDecimal(), "欣快", -1),
            DoseEffectRule(600.toBigDecimal(), "欣快", -2),
            DoseEffectRule(600.toBigDecimal(), "镇静", 1),
            DoseEffectRule(900.toBigDecimal(), "欣快", -2),
            DoseEffectRule(900.toBigDecimal(), "镇静", 1),
            DoseEffectRule(1200.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(1500.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(1800.toBigDecimal(), "谵妄", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.DOPAMINE, -1),
        ),
    )

    private val valproate = d(
        key = "valproate", name = "丙戊酸钠", colorArgb = 0xFF00838F, tabletDose = 200.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心境稳定剂"),
        effectTags = listOf("GABA受体激动剂"),
        moleculeTag = "支链脂肪酸类", halfLifeHours = 12.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2C9"),
        inhibitors = listOf("CYP2C9", "CYP2C19"),
        rules = listOf(
            DoseEffectRule(200.toBigDecimal(), "欣快", -1),
            DoseEffectRule(200.toBigDecimal(), "释然", 1),
            DoseEffectRule(500.toBigDecimal(), "欣快", -2),
            DoseEffectRule(500.toBigDecimal(), "镇静", 1),
            DoseEffectRule(750.toBigDecimal(), "欣快", -2),
            DoseEffectRule(1000.toBigDecimal(), "镇静", 2),
            DoseEffectRule(1250.toBigDecimal(), "恶心", 1),
            DoseEffectRule(1500.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(2000.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
            NeuroDelta(Neurotransmitters.DOPAMINE, -1),
        ),
    )

    private val carbamazepine = d(
        key = "carbamazepine", name = "卡马西平", colorArgb = 0xFF008BA3, tabletDose = 100.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心境稳定剂"),
        effectTags = listOf("钠通道阻滞剂"),
        moleculeTag = "亚氨芪类", halfLifeHours = 14.0, bioavailabilityPct = 80, route = Route.ORAL,
        tmaxHours = 5.0,
        substrates = listOf("CYP3A4", "CYP2C9", "CYP1A2"),
        inducers = listOf("CYP3A4", "CYP2C9", "CYP1A2", "CYP2B6", "CYP2C19"),
        rules = listOf(
            DoseEffectRule(100.toBigDecimal(), "欣快", -1),
            DoseEffectRule(100.toBigDecimal(), "释然", 1),
            DoseEffectRule(200.toBigDecimal(), "欣快", -2),
            DoseEffectRule(200.toBigDecimal(), "镇静", 1),
            DoseEffectRule(300.toBigDecimal(), "欣快", -2),
            DoseEffectRule(400.toBigDecimal(), "镇静", 2),
            DoseEffectRule(600.toBigDecimal(), "欣快", -2),
            DoseEffectRule(600.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(800.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(1200.toBigDecimal(), "谵妄", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
            NeuroDelta(Neurotransmitters.DOPAMINE, -1),
        ),
    )

    private val lamotrigine = d(
        key = "lamotrigine", name = "拉莫三嗪", colorArgb = 0xFF00ACC1, tabletDose = 50.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心境稳定剂"),
        effectTags = listOf("钠通道阻滞剂"),
        moleculeTag = "苯基三嗪类", halfLifeHours = 25.0, bioavailabilityPct = 98, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("UGT1A4"),
        inducers = listOf("UGT1A4"),
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "欣快", -1),
            DoseEffectRule(25.toBigDecimal(), "释然", 1),
            DoseEffectRule(100.toBigDecimal(), "欣快", -1),
            DoseEffectRule(100.toBigDecimal(), "专注", 1),
            DoseEffectRule(200.toBigDecimal(), "欣快", -2),
            DoseEffectRule(200.toBigDecimal(), "镇静", 1),
            DoseEffectRule(300.toBigDecimal(), "欣快", -2),
            DoseEffectRule(400.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(500.toBigDecimal(), "视物模糊", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
            NeuroDelta(Neurotransmitters.GLUTAMATE, -1),
        ),
    )

    private val oxcarbazepine = d(
        key = "oxcarbazepine", name = "奥卡西平", colorArgb = 0xFF009FA5, tabletDose = 150.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心境稳定剂"),
        effectTags = listOf("钠通道阻滞剂"),
        moleculeTag = "亚氨芪类", halfLifeHours = 9.0, bioavailabilityPct = 95, route = Route.ORAL,
        tmaxHours = 4.5,
        substrates = listOf("CYP3A4", "CYP2C19"),
        inhibitors = listOf("CYP2C19"),
        rules = listOf(
            DoseEffectRule(150.toBigDecimal(), "欣快", -1),
            DoseEffectRule(150.toBigDecimal(), "释然", 1),
            DoseEffectRule(300.toBigDecimal(), "欣快", -2),
            DoseEffectRule(300.toBigDecimal(), "镇静", 1),
            DoseEffectRule(450.toBigDecimal(), "欣快", -2),
            DoseEffectRule(600.toBigDecimal(), "镇静", 2),
            DoseEffectRule(750.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(900.toBigDecimal(), "恶心", 1),
            DoseEffectRule(1200.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
            NeuroDelta(Neurotransmitters.GLUTAMATE, -1),
        ),
    )

    private val diazepam = d(
        key = "diazepam", name = "地西泮", colorArgb = 0xFF2E7D32, tabletDose = 2.5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("抗焦虑剂", "镇静剂"),
        effectTags = listOf("苯二氮卓类受体激动剂", "GABA受体激动剂"),
        moleculeTag = "苯二氮卓类", halfLifeHours = 40.0, bioavailabilityPct = 94, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4", "CYP2C19"),
        rules = listOf(
            DoseEffectRule(2.toBigDecimal(), "镇静", 2),
            DoseEffectRule(2.toBigDecimal(), "释然", 1),
            DoseEffectRule(5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(5.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(10.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(10.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(15.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(20.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 2),
        ),
    )

    private val lorazepam = d(
        key = "lorazepam", name = "劳拉西泮", colorArgb = 0xFF388E3C, tabletDose = 0.5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("抗焦虑剂", "镇静剂"),
        effectTags = listOf("苯二氮卓类受体激动剂", "GABA受体激动剂"),
        moleculeTag = "苯二氮卓类", halfLifeHours = 15.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("UGT2B15"),
        rules = listOf(
            DoseEffectRule(0.5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(0.5.toBigDecimal(), "释然", 1),
            DoseEffectRule(1.toBigDecimal(), "镇静", 2),
            DoseEffectRule(1.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(2.toBigDecimal(), "镇静", 2),
            DoseEffectRule(2.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(3.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(4.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 2),
        ),
    )

    private val clonazepam = d(
        key = "clonazepam", name = "氯硝西泮", colorArgb = 0xFF43A047, tabletDose = 0.5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("抗焦虑剂", "镇静剂", "抗强迫剂"),
        effectTags = listOf("苯二氮卓类受体激动剂", "GABA受体激动剂"),
        moleculeTag = "苯二氮卓类", halfLifeHours = 35.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(0.25.toBigDecimal(), "镇静", 1),
            DoseEffectRule(0.25.toBigDecimal(), "释然", 1),
            DoseEffectRule(0.5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(0.5.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(1.toBigDecimal(), "镇静", 2),
            DoseEffectRule(1.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(2.toBigDecimal(), "镇静", 2),
            DoseEffectRule(2.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(4.toBigDecimal(), "镇静", 3),
            DoseEffectRule(6.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 2),
        ),
    )

    private val estazolam = d(
        key = "estazolam", name = "艾司唑仑", colorArgb = 0xFF1B5E20, tabletDose = 1.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("镇静剂"),
        effectTags = listOf("苯二氮卓类受体激动剂", "GABA受体激动剂"),
        moleculeTag = "苯二氮卓类", halfLifeHours = 15.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP3A4", "CYP2C19"),
        rules = listOf(
            DoseEffectRule(1.toBigDecimal(), "镇静", 2),
            DoseEffectRule(1.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(2.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(2.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(3.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(4.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 2),
        ),
    )

    private val zolpidem = d(
        key = "zolpidem", name = "唑吡坦", colorArgb = 0xFF33691E, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("镇静剂", "梦境增强剂"),
        effectTags = listOf("GABA受体激动剂"),
        moleculeTag = "咪唑并吡啶类", halfLifeHours = 2.5, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4", "CYP2C9", "CYP1A2"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(5.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(10.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(10.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(15.toBigDecimal(), "恍惚", 2),
            DoseEffectRule(20.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(20.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
        ),
    )

    private val zopiclone = d(
        key = "zopiclone", name = "佐匹克隆", colorArgb = 0xFF558B2F, tabletDose = 3.75.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("镇静剂"),
        effectTags = listOf("GABA受体激动剂"),
        moleculeTag = "环吡咯酮类", halfLifeHours = 5.0, bioavailabilityPct = 78, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4", "CYP2C8"),
        rules = listOf(
            DoseEffectRule(3.75.toBigDecimal(), "镇静", 2),
            DoseEffectRule(3.75.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(7.5.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(7.5.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(11.25.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(15.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
        ),
    )

    private val eszopiclone = d(
        key = "eszopiclone", name = "右佐匹克隆", colorArgb = 0xFF689F38, tabletDose = 1.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("镇静剂"),
        effectTags = listOf("GABA受体激动剂"),
        moleculeTag = "环吡咯酮类", halfLifeHours = 6.0, bioavailabilityPct = 78, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4", "CYP2C8"),
        rules = listOf(
            DoseEffectRule(1.toBigDecimal(), "镇静", 1),
            DoseEffectRule(1.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(2.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(2.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(3.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(4.5.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
        ),
    )

    private val buspirone = d(
        key = "buspirone", name = "丁螺环酮", colorArgb = 0xFF7CB342, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗焦虑剂"),
        effectTags = listOf("血清素受体激动剂"),
        moleculeTag = "螺环类", halfLifeHours = 2.5, bioavailabilityPct = 4, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP3A4", "CYP2D6"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "释然", 1),
            DoseEffectRule(10.toBigDecimal(), "释然", 2),
            DoseEffectRule(15.toBigDecimal(), "释然", 2),
            DoseEffectRule(20.toBigDecimal(), "失眠", 1),
            DoseEffectRule(30.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(60.toBigDecimal(), "欣快", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.DOPAMINE, -1),
        ),
    )

    private val tandospirone = d(
        key = "tandospirone", name = "坦度螺酮", colorArgb = 0xFF8BC34A, tabletDose = 10.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗焦虑剂"),
        effectTags = listOf("血清素受体激动剂"),
        moleculeTag = "螺环类", halfLifeHours = 3.0, bioavailabilityPct = 15, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "释然", 1),
            DoseEffectRule(20.toBigDecimal(), "释然", 1),
            DoseEffectRule(20.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(30.toBigDecimal(), "释然", 2),
            DoseEffectRule(40.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(60.toBigDecimal(), "恶心", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val phenobarbital = d(
        key = "phenobarbital", name = "苯巴比妥", colorArgb = 0xFF4E342E, tabletDose = 15.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("镇静剂"),
        effectTags = listOf("GABA受体激动剂"),
        moleculeTag = "巴比妥类", halfLifeHours = 53.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2C9", "CYP3A4", "CYP2C19"),
        inducers = listOf("CYP3A4", "CYP2C9", "CYP2C19", "CYP1A2", "CYP2B6"),
        rules = listOf(
            DoseEffectRule(15.toBigDecimal(), "镇静", 2),
            DoseEffectRule(15.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(30.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(30.toBigDecimal(), "欣快", 1),
            DoseEffectRule(60.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(60.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(100.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(100.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(200.toBigDecimal(), "呼吸抑制", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 2),
        ),
    )

    private val promethazine = d(
        key = "promethazine", name = "异丙嗪", colorArgb = 0xFF6A4E70, tabletDose = 12.5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("镇静剂"),
        effectTags = listOf("抗组胺剂", "抗胆碱能剂"),
        moleculeTag = "吩噻嗪类", halfLifeHours = 10.0, bioavailabilityPct = 25, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(6.25.toBigDecimal(), "镇静", 2),
            DoseEffectRule(12.5.toBigDecimal(), "镇静", 2),
            DoseEffectRule(12.5.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(25.toBigDecimal(), "镇静", 3),
            DoseEffectRule(25.toBigDecimal(), "口干", 1),
            DoseEffectRule(50.toBigDecimal(), "视物模糊", 1),
            DoseEffectRule(75.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(100.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val benzatropine = d(
        key = "benzatropine", name = "苯扎托品", colorArgb = 0xFFBF6000, tabletDose = 0.5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("谵妄剂"),
        effectTags = listOf("抗胆碱能剂"),
        moleculeTag = "托品烷类", halfLifeHours = 5.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(0.5.toBigDecimal(), "口干", 1),
            DoseEffectRule(0.5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(1.toBigDecimal(), "口干", 1),
            DoseEffectRule(1.toBigDecimal(), "视物模糊", 1),
            DoseEffectRule(1.toBigDecimal(), "便秘", 1),
            DoseEffectRule(2.toBigDecimal(), "抗胆碱能综合征", 1),
            DoseEffectRule(2.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(3.toBigDecimal(), "谵妄", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -2),
        ),
    )

    private val methylphenidate = d(
        key = "methylphenidate", name = "哌甲酯", colorArgb = 0xFFE0533D, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_I,
        subjectiveTags = listOf("中枢兴奋剂"),
        effectTags = listOf("多巴胺与去甲肾上腺素再摄取抑制剂"),
        moleculeTag = "哌啶类", halfLifeHours = 3.0, bioavailabilityPct = 30, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "专注", 2),
            DoseEffectRule(5.toBigDecimal(), "欣快", 1),
            DoseEffectRule(10.toBigDecimal(), "专注", 2),
            DoseEffectRule(10.toBigDecimal(), "欣快", 2),
            DoseEffectRule(15.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(20.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(25.toBigDecimal(), "失眠", 1),
            DoseEffectRule(30.toBigDecimal(), "心动过速", 2),
            DoseEffectRule(40.toBigDecimal(), "焦虑", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 2),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 2),
        ),
    )

    private val atomoxetine = d(
        key = "atomoxetine", name = "托莫西汀", colorArgb = 0xFFEA7A4B, tabletDose = 10.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("记忆抑制剂"),
        effectTags = listOf("去甲肾上腺素再摄取抑制剂"),
        moleculeTag = "托品类", halfLifeHours = 5.0, bioavailabilityPct = 80, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2D6", "CYP2C19"),
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "专注", 1),
            DoseEffectRule(25.toBigDecimal(), "专注", 2),
            DoseEffectRule(25.toBigDecimal(), "欣快", 1),
            DoseEffectRule(40.toBigDecimal(), "专注", 2),
            DoseEffectRule(40.toBigDecimal(), "失眠", 1),
            DoseEffectRule(60.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(80.toBigDecimal(), "恶心", 1),
            DoseEffectRule(100.toBigDecimal(), "心动过速", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 2),
        ),
    )

    private val modafinil = d(
        key = "modafinil", name = "莫达非尼", colorArgb = 0xFFEF6C00, tabletDose = 100.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_I,
        subjectiveTags = listOf("中枢兴奋剂"),
        effectTags = listOf("多巴胺与去甲肾上腺素再摄取抑制剂"),
        moleculeTag = "二苯甲磺酰类", halfLifeHours = 12.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP3A4"),
        inducers = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(100.toBigDecimal(), "专注", 2),
            DoseEffectRule(100.toBigDecimal(), "欣快", 1),
            DoseEffectRule(200.toBigDecimal(), "专注", 3),
            DoseEffectRule(200.toBigDecimal(), "欣快", 2),
            DoseEffectRule(200.toBigDecimal(), "失眠", 1),
            DoseEffectRule(300.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(300.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(400.toBigDecimal(), "失眠", 2),
            DoseEffectRule(600.toBigDecimal(), "焦虑", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 2),
            NeuroDelta(Neurotransmitters.HISTAMINE, 1),
        ),
    )

    private val codeine = d(
        key = "codeine", name = "可待因", colorArgb = 0xFFA02828, tabletDose = 15.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.NARCOTIC,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "阿片类", halfLifeHours = 3.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(15.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(15.toBigDecimal(), "镇静", 1),
            DoseEffectRule(30.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(30.toBigDecimal(), "便秘", 1),
            DoseEffectRule(30.toBigDecimal(), "恶心", 1),
            DoseEffectRule(45.toBigDecimal(), "欣快", 1),
            DoseEffectRule(60.toBigDecimal(), "欣快", 1),
            DoseEffectRule(60.toBigDecimal(), "镇静", 2),
            DoseEffectRule(90.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(120.toBigDecimal(), "便秘", 2),
            DoseEffectRule(120.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(150.toBigDecimal(), "阿片类中毒", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ENDORPHIN, 2),
        ),
    )

    private val tramadol = d(
        key = "tramadol", name = "曲马多", colorArgb = 0xFFB33A3A, tabletDose = 50.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂", "血清素再摄取抑制剂", "血清素能镇痛药"),
        moleculeTag = "环己醇类", halfLifeHours = 6.0, bioavailabilityPct = 75, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2D6", "CYP2B6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(50.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(50.toBigDecimal(), "欣快", 1),
            DoseEffectRule(100.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(100.toBigDecimal(), "欣快", 1),
            DoseEffectRule(100.toBigDecimal(), "镇静", 1),
            DoseEffectRule(150.toBigDecimal(), "欣快", 1),
            DoseEffectRule(200.toBigDecimal(), "欣快", 2),
            DoseEffectRule(200.toBigDecimal(), "镇静", 2),
            DoseEffectRule(200.toBigDecimal(), "便秘", 1),
            DoseEffectRule(250.toBigDecimal(), "恶心", 1),
            DoseEffectRule(300.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(350.toBigDecimal(), "肌阵挛", 1),
            DoseEffectRule(400.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(400.toBigDecimal(), "阿片类中毒", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ENDORPHIN, 2),
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val fentanyl = d(
        key = "fentanyl", name = "芬太尼", colorArgb = 0xFF8E1414, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MCG, managementClass = ManagementClass.NARCOTIC,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "苯基哌啶类", halfLifeHours = 17.0, bioavailabilityPct = 60, route = Route.TRANSDERMAL,
        tmaxHours = 8.0,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(12.5.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(25.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(25.toBigDecimal(), "欣快", 1),
            DoseEffectRule(25.toBigDecimal(), "镇静", 1),
            DoseEffectRule(50.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(50.toBigDecimal(), "欣快", 2),
            DoseEffectRule(50.toBigDecimal(), "镇静", 2),
            DoseEffectRule(75.toBigDecimal(), "欣快", 2),
            DoseEffectRule(100.toBigDecimal(), "镇静", 3),
            DoseEffectRule(100.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(150.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(200.toBigDecimal(), "瞳孔缩小", 1),
            DoseEffectRule(200.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(300.toBigDecimal(), "阿片类中毒", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ENDORPHIN, 2),
        ),
    )

    private val oxycodone = d(
        key = "oxycodone", name = "羟考酮", colorArgb = 0xFFC0392B, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.NARCOTIC,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "阿片类", halfLifeHours = 4.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(5.toBigDecimal(), "欣快", 1),
            DoseEffectRule(5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(10.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(10.toBigDecimal(), "欣快", 2),
            DoseEffectRule(15.toBigDecimal(), "欣快", 2),
            DoseEffectRule(20.toBigDecimal(), "欣快", 2),
            DoseEffectRule(20.toBigDecimal(), "镇静", 2),
            DoseEffectRule(20.toBigDecimal(), "便秘", 1),
            DoseEffectRule(30.toBigDecimal(), "镇静", 3),
            DoseEffectRule(40.toBigDecimal(), "镇静", 3),
            DoseEffectRule(40.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(40.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(60.toBigDecimal(), "瞳孔缩小", 1),
            DoseEffectRule(80.toBigDecimal(), "阿片类中毒", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ENDORPHIN, 2),
        ),
    )

    private val hydrocodone = d(
        key = "hydrocodone", name = "氢可酮", colorArgb = 0xFFCD5C5C, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.NARCOTIC,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "阿片类", halfLifeHours = 5.0, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(7.5.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(7.5.toBigDecimal(), "欣快", 1),
            DoseEffectRule(10.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(10.toBigDecimal(), "欣快", 1),
            DoseEffectRule(15.toBigDecimal(), "欣快", 2),
            DoseEffectRule(15.toBigDecimal(), "镇静", 2),
            DoseEffectRule(20.toBigDecimal(), "欣快", 2),
            DoseEffectRule(20.toBigDecimal(), "便秘", 1),
            DoseEffectRule(30.toBigDecimal(), "镇静", 3),
            DoseEffectRule(30.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(40.toBigDecimal(), "阿片类中毒", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ENDORPHIN, 2),
        ),
    )

    private val methadone = d(
        key = "methadone", name = "美沙酮", colorArgb = 0xFF7E1D1D, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.NARCOTIC,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "二苯庚酮类", halfLifeHours = 22.0, bioavailabilityPct = 80, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("CYP3A4", "CYP2B6", "CYP2D6"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(5.toBigDecimal(), "欣快", 1),
            DoseEffectRule(5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(10.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(10.toBigDecimal(), "欣快", 2),
            DoseEffectRule(10.toBigDecimal(), "镇静", 2),
            DoseEffectRule(15.toBigDecimal(), "欣快", 2),
            DoseEffectRule(15.toBigDecimal(), "镇静", 2),
            DoseEffectRule(20.toBigDecimal(), "欣快", 2),
            DoseEffectRule(20.toBigDecimal(), "镇静", 2),
            DoseEffectRule(20.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(30.toBigDecimal(), "镇静", 3),
            DoseEffectRule(30.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(40.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(40.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(50.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(60.toBigDecimal(), "嗜睡", 3),
            DoseEffectRule(60.toBigDecimal(), "呼吸抑制", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ENDORPHIN, 2),
        ),
    )

    private val buprenorphine = d(
        key = "buprenorphine", name = "丁丙诺啡", colorArgb = 0xFF9E2A2A, tabletDose = 0.2.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.NARCOTIC,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "蒂巴因衍生物", halfLifeHours = 37.0, bioavailabilityPct = 30, route = Route.SUBLINGUAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(0.2.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(0.2.toBigDecimal(), "镇静", 1),
            DoseEffectRule(0.4.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(0.4.toBigDecimal(), "镇静", 2),
            DoseEffectRule(1.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(1.toBigDecimal(), "镇静", 2),
            DoseEffectRule(1.toBigDecimal(), "欣快", 1),
            DoseEffectRule(2.toBigDecimal(), "欣快", 1),
            DoseEffectRule(2.toBigDecimal(), "镇静", 2),
            DoseEffectRule(4.toBigDecimal(), "欣快", 2),
            DoseEffectRule(4.toBigDecimal(), "镇静", 3),
            DoseEffectRule(4.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(8.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(12.toBigDecimal(), "嗜睡", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ENDORPHIN, 2),
        ),
    )

    private val pethidine = d(
        key = "pethidine", name = "哌替啶", colorArgb = 0xFFB44848, tabletDose = 50.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.NARCOTIC,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "苯基哌啶类", halfLifeHours = 4.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP3A4", "CYP2B6"),
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(25.toBigDecimal(), "欣快", 1),
            DoseEffectRule(50.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(50.toBigDecimal(), "欣快", 1),
            DoseEffectRule(50.toBigDecimal(), "镇静", 2),
            DoseEffectRule(50.toBigDecimal(), "恶心", 1),
            DoseEffectRule(75.toBigDecimal(), "欣快", 2),
            DoseEffectRule(75.toBigDecimal(), "镇静", 2),
            DoseEffectRule(100.toBigDecimal(), "欣快", 2),
            DoseEffectRule(100.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(100.toBigDecimal(), "肌阵挛", 1),
            DoseEffectRule(150.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(150.toBigDecimal(), "肌阵挛", 1),
            DoseEffectRule(200.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(200.toBigDecimal(), "肌阵挛", 2),
            DoseEffectRule(250.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(300.toBigDecimal(), "呼吸抑制", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ENDORPHIN, 2),
        ),
    )

    private val pentazocine = d(
        key = "pentazocine", name = "喷他佐辛", colorArgb = 0xFFC05F5F, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.NARCOTIC,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "苯并吗啡烷类", halfLifeHours = 3.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(15.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(15.toBigDecimal(), "欣快", 1),
            DoseEffectRule(15.toBigDecimal(), "镇静", 1),
            DoseEffectRule(30.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(30.toBigDecimal(), "镇静", 2),
            DoseEffectRule(45.toBigDecimal(), "欣快", 1),
            DoseEffectRule(45.toBigDecimal(), "镇静", 2),
            DoseEffectRule(60.toBigDecimal(), "欣快", 2),
            DoseEffectRule(60.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(90.toBigDecimal(), "恍惚", 2),
            DoseEffectRule(120.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(150.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ENDORPHIN, 2),
        ),
    )

    private val tapentadol = d(
        key = "tapentadol", name = "他喷他多", colorArgb = 0xFFA94040, tabletDose = 50.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂", "去甲肾上腺素再摄取抑制剂"),
        moleculeTag = "环己醇类", halfLifeHours = 4.0, bioavailabilityPct = 32, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP2D6", "CYP2C9"),
        rules = listOf(
            DoseEffectRule(50.toBigDecimal(), "镇静", 1),
            DoseEffectRule(50.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(75.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(75.toBigDecimal(), "欣快", 1),
            DoseEffectRule(100.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(100.toBigDecimal(), "欣快", 1),
            DoseEffectRule(100.toBigDecimal(), "镇静", 2),
            DoseEffectRule(150.toBigDecimal(), "欣快", 2),
            DoseEffectRule(150.toBigDecimal(), "镇静", 2),
            DoseEffectRule(200.toBigDecimal(), "欣快", 2),
            DoseEffectRule(200.toBigDecimal(), "便秘", 1),
            DoseEffectRule(300.toBigDecimal(), "镇静", 3),
            DoseEffectRule(400.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(400.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(500.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(600.toBigDecimal(), "阿片类中毒", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ENDORPHIN, 2),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
        ),
    )

    private val dextromethorphan = d(
        key = "dextromethorphan", name = "右美沙芬", colorArgb = 0xFFD84A4A, tabletDose = 15.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("解离剂"),
        effectTags = listOf("NMDA受体拮抗剂", "血清素再摄取抑制剂"),
        moleculeTag = "右吗喃类", halfLifeHours = 4.0, bioavailabilityPct = 11, route = Route.ORAL,
        tmaxHours = 2.5,
        substrates = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "镇吐", 1),
            DoseEffectRule(10.toBigDecimal(), "镇静", 1),
            DoseEffectRule(30.toBigDecimal(), "镇吐", 1),
            DoseEffectRule(30.toBigDecimal(), "镇静", 2),
            DoseEffectRule(60.toBigDecimal(), "欣快", 1),
            DoseEffectRule(60.toBigDecimal(), "解离", 1),
            DoseEffectRule(90.toBigDecimal(), "解离", 1),
            DoseEffectRule(120.toBigDecimal(), "欣快", 2),
            DoseEffectRule(120.toBigDecimal(), "解离", 2),
            DoseEffectRule(120.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(150.toBigDecimal(), "欣快", 2),
            DoseEffectRule(180.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(180.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(240.toBigDecimal(), "谵妄", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GLUTAMATE, -1),
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val acetaminophen = d(
        key = "acetaminophen", name = "对乙酰氨基酚", colorArgb = 0xFFB58A2C, tabletDose = 500.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.OTC_A,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("对乙酰氨基酚类"),
        moleculeTag = "乙酰苯胺类", halfLifeHours = 2.5, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 1.0,
        rules = listOf(
            DoseEffectRule(250.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(250.toBigDecimal(), "释然", 1),
            DoseEffectRule(500.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(500.toBigDecimal(), "释然", 1),
            DoseEffectRule(650.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(1000.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(1000.toBigDecimal(), "镇静", 1),
            DoseEffectRule(1500.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(2000.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(3000.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(4000.toBigDecimal(), "肝损伤", 1),
            DoseEffectRule(5000.toBigDecimal(), "肝损伤", 1),
            DoseEffectRule(5000.toBigDecimal(), "恶心", 1),
            DoseEffectRule(6000.toBigDecimal(), "肝损伤", 2),
            DoseEffectRule(7000.toBigDecimal(), "肝损伤", 2),
            DoseEffectRule(7000.toBigDecimal(), "谵妄", 1),
        ),
    )

    private val ibuprofen = d(
        key = "ibuprofen", name = "布洛芬", colorArgb = 0xFFC0912F, tabletDose = 200.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.OTC_A,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("非甾体抗炎药"),
        moleculeTag = "芳基丙酸类", halfLifeHours = 2.0, bioavailabilityPct = 49, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP2C9"),
        rules = listOf(
            DoseEffectRule(100.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(100.toBigDecimal(), "释然", 1),
            DoseEffectRule(200.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(200.toBigDecimal(), "释然", 1),
            DoseEffectRule(300.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(400.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(600.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(600.toBigDecimal(), "恶心", 1),
            DoseEffectRule(800.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(800.toBigDecimal(), "恶心", 1),
            DoseEffectRule(1200.toBigDecimal(), "恶心", 1),
        ),
    )

    private val naproxen = d(
        key = "naproxen", name = "萘普生", colorArgb = 0xFFB8973A, tabletDose = 250.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.OTC_B,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("非甾体抗炎药"),
        moleculeTag = "芳基丙酸类", halfLifeHours = 14.0, bioavailabilityPct = 95, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2C9", "CYP1A2"),
        rules = listOf(
            DoseEffectRule(125.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(250.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(250.toBigDecimal(), "释然", 1),
            DoseEffectRule(375.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(500.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(500.toBigDecimal(), "恶心", 1),
            DoseEffectRule(750.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(750.toBigDecimal(), "恶心", 1),
            DoseEffectRule(1000.toBigDecimal(), "恶心", 1),
            DoseEffectRule(1250.toBigDecimal(), "恶心", 2),
        ),
    )

    private val aspirin = d(
        key = "aspirin", name = "阿司匹林", colorArgb = 0xFFC7A23F, tabletDose = 100.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.OTC_B,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("非甾体抗炎药"),
        moleculeTag = "水杨酸类", halfLifeHours = 6.0, bioavailabilityPct = 68, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP2C9", "UGT1A6"),
        rules = listOf(
            DoseEffectRule(50.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(100.toBigDecimal(), "释然", 1),
            DoseEffectRule(300.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(300.toBigDecimal(), "释然", 1),
            DoseEffectRule(500.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(600.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(600.toBigDecimal(), "恶心", 1),
            DoseEffectRule(900.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(900.toBigDecimal(), "出汗", 1),
            DoseEffectRule(1200.toBigDecimal(), "恶心", 1),
            DoseEffectRule(1500.toBigDecimal(), "恶心", 1),
            DoseEffectRule(1500.toBigDecimal(), "出汗", 1),
            DoseEffectRule(2000.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(2500.toBigDecimal(), "谵妄", 2),
            DoseEffectRule(2500.toBigDecimal(), "出汗", 2),
        ),
    )

    private val diclofenac = d(
        key = "diclofenac", name = "双氯芬酸", colorArgb = 0xFFB27D24, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("非甾体抗炎药"),
        moleculeTag = "芳基乙酸类", halfLifeHours = 2.0, bioavailabilityPct = 54, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP2C9", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(12.5.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(25.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(25.toBigDecimal(), "释然", 1),
            DoseEffectRule(50.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(50.toBigDecimal(), "恶心", 1),
            DoseEffectRule(75.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(75.toBigDecimal(), "恶心", 1),
            DoseEffectRule(100.toBigDecimal(), "恶心", 1),
            DoseEffectRule(150.toBigDecimal(), "恶心", 1),
            DoseEffectRule(150.toBigDecimal(), "肝损伤", 1),
        ),
    )

    private val celecoxib = d(
        key = "celecoxib", name = "塞来昔布", colorArgb = 0xFFA98B3F, tabletDose = 100.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("非甾体抗炎药"),
        moleculeTag = "吡唑类", halfLifeHours = 11.0, bioavailabilityPct = 74, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2C9", "CYP3A4"),
        inhibitors = listOf("CYP2D6"),
        rules = listOf(
            DoseEffectRule(100.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(100.toBigDecimal(), "释然", 1),
            DoseEffectRule(200.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(200.toBigDecimal(), "释然", 1),
            DoseEffectRule(300.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(400.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(400.toBigDecimal(), "恶心", 1),
            DoseEffectRule(600.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(600.toBigDecimal(), "恶心", 1),
        ),
    )

    private val nefopam = d(
        key = "nefopam", name = "奈福泮", colorArgb = 0xFFB56E2A, tabletDose = 20.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("镇痛剂", "抗抑郁剂"),
        effectTags = listOf("血清素再摄取抑制剂", "血清素能镇痛药"),
        moleculeTag = "5-甲基-1-苯基-1,3,4,6-四氢-2,5-苯并氧氮䓬类", halfLifeHours = 4.0, bioavailabilityPct = 36, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(10.toBigDecimal(), "欣快", 1),
            DoseEffectRule(20.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(20.toBigDecimal(), "欣快", 1),
            DoseEffectRule(30.toBigDecimal(), "欣快", 2),
            DoseEffectRule(30.toBigDecimal(), "镇静", 1),
            DoseEffectRule(40.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(40.toBigDecimal(), "欣快", 2),
            DoseEffectRule(60.toBigDecimal(), "欣快", 2),
            DoseEffectRule(60.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(60.toBigDecimal(), "恶心", 1),
            DoseEffectRule(90.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(90.toBigDecimal(), "欣快", 2),
            DoseEffectRule(120.toBigDecimal(), "肌阵挛", 1),
            DoseEffectRule(120.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
        ),
    )

    private val donepezil = d(
        key = "donepezil", name = "多奈哌齐", colorArgb = 0xFF558B2F, tabletDose = 5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("记忆增强剂"),
        effectTags = listOf("胆碱酯酶抑制剂"),
        moleculeTag = "哌啶类", halfLifeHours = 70.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "善记", 1),
            DoseEffectRule(5.toBigDecimal(), "专注", 1),
            DoseEffectRule(10.toBigDecimal(), "善记", 2),
            DoseEffectRule(10.toBigDecimal(), "专注", 1),
            DoseEffectRule(15.toBigDecimal(), "善记", 2),
            DoseEffectRule(15.toBigDecimal(), "失眠", 1),
            DoseEffectRule(20.toBigDecimal(), "善记", 2),
            DoseEffectRule(20.toBigDecimal(), "恶心", 1),
            DoseEffectRule(25.toBigDecimal(), "恶心", 2),
            DoseEffectRule(30.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1),
        ),
    )

    private val galantamine = d(
        key = "galantamine", name = "加兰他敏", colorArgb = 0xFF689F38, tabletDose = 4.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("梦境增强剂", "记忆增强剂"),
        effectTags = listOf("胆碱酯酶抑制剂"),
        moleculeTag = "石蒜科生物碱类", halfLifeHours = 7.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP2D6", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(4.toBigDecimal(), "善记", 1),
            DoseEffectRule(4.toBigDecimal(), "专注", 1),
            DoseEffectRule(8.toBigDecimal(), "善记", 2),
            DoseEffectRule(8.toBigDecimal(), "专注", 1),
            DoseEffectRule(12.toBigDecimal(), "善记", 2),
            DoseEffectRule(12.toBigDecimal(), "失眠", 1),
            DoseEffectRule(12.toBigDecimal(), "恶心", 1),
            DoseEffectRule(16.toBigDecimal(), "失眠", 1),
            DoseEffectRule(16.toBigDecimal(), "恶心", 1),
            DoseEffectRule(20.toBigDecimal(), "恶心", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1),
        ),
    )

    private val rivastigmine = d(
        key = "rivastigmine", name = "卡巴拉汀", colorArgb = 0xFF4C7C2A, tabletDose = 1.5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("记忆增强剂", "梦境增强剂"),
        effectTags = listOf("胆碱酯酶抑制剂"),
        moleculeTag = "氨基甲酸酯类", halfLifeHours = 1.5, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 1.0,
        rules = listOf(
            DoseEffectRule(1.5.toBigDecimal(), "善记", 1),
            DoseEffectRule(1.5.toBigDecimal(), "恶心", 1),
            DoseEffectRule(3.toBigDecimal(), "善记", 1),
            DoseEffectRule(3.toBigDecimal(), "恶心", 1),
            DoseEffectRule(3.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(4.5.toBigDecimal(), "善记", 2),
            DoseEffectRule(6.toBigDecimal(), "善记", 2),
            DoseEffectRule(6.toBigDecimal(), "恶心", 2),
            DoseEffectRule(6.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(9.toBigDecimal(), "恶心", 2),
            DoseEffectRule(12.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1),
        ),
    )

    private val huperzine = d(
        key = "huperzine", name = "石杉碱甲", colorArgb = 0xFF7CB342, tabletDose = 50.toBigDecimal(),
        doseUnit = DoseUnit.MCG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("记忆增强剂"),
        effectTags = listOf("胆碱酯酶抑制剂"),
        moleculeTag = "石杉科生物碱类", halfLifeHours = 12.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(50.toBigDecimal(), "善记", 1),
            DoseEffectRule(100.toBigDecimal(), "善记", 1),
            DoseEffectRule(100.toBigDecimal(), "专注", 1),
            DoseEffectRule(150.toBigDecimal(), "善记", 2),
            DoseEffectRule(200.toBigDecimal(), "失眠", 1),
            DoseEffectRule(200.toBigDecimal(), "恶心", 1),
            DoseEffectRule(300.toBigDecimal(), "失眠", 1),
            DoseEffectRule(300.toBigDecimal(), "恶心", 1),
            DoseEffectRule(400.toBigDecimal(), "恶心", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1),
        ),
    )

    private val melatonin = d(
        key = "melatonin", name = "褪黑素", colorArgb = 0xFF7A9B4F, tabletDose = 1.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("镇静剂", "梦境增强剂"),
        effectTags = listOf("褪黑素受体激动剂"),
        moleculeTag = "吲哚类激素", halfLifeHours = 0.7, bioavailabilityPct = 30, route = Route.ORAL,
        tmaxHours = 0.7,
        substrates = listOf("CYP1A2"),
        rules = listOf(
            DoseEffectRule(0.5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(0.5.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(1.toBigDecimal(), "镇静", 1),
            DoseEffectRule(1.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(2.toBigDecimal(), "镇静", 2),
            DoseEffectRule(2.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(3.toBigDecimal(), "镇静", 2),
            DoseEffectRule(3.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(5.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(5.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(10.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(10.toBigDecimal(), "恍惚", 2),
        ),
    )

    private val magnesium_glycinate = d(
        key = "magnesium_glycinate", name = "甘氨酸镁", colorArgb = 0xFF8A9A5B, tabletDose = 100.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("镇静剂"),
        effectTags = listOf("矿物质补充剂"),
        moleculeTag = "氨基酸螯合镁", halfLifeHours = 8.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(100.toBigDecimal(), "镇静", 1),
            DoseEffectRule(200.toBigDecimal(), "镇静", 1),
            DoseEffectRule(200.toBigDecimal(), "善记", 1),
            DoseEffectRule(300.toBigDecimal(), "镇静", 2),
            DoseEffectRule(400.toBigDecimal(), "镇静", 2),
            DoseEffectRule(400.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(600.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(800.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(800.toBigDecimal(), "恶心", 1),
            DoseEffectRule(1000.toBigDecimal(), "恶心", 1),
        ),
    )

    private val magnesium_threonate = d(
        key = "magnesium_threonate", name = "苏糖酸镁", colorArgb = 0xFF94A65D, tabletDose = 1000.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("记忆增强剂", "镇静剂"),
        effectTags = listOf("矿物质补充剂"),
        moleculeTag = "苏糖酸螯合镁", halfLifeHours = 8.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(500.toBigDecimal(), "善记", 1),
            DoseEffectRule(500.toBigDecimal(), "镇静", 1),
            DoseEffectRule(1000.toBigDecimal(), "善记", 1),
            DoseEffectRule(1000.toBigDecimal(), "专注", 1),
            DoseEffectRule(1000.toBigDecimal(), "镇静", 1),
            DoseEffectRule(1500.toBigDecimal(), "善记", 2),
            DoseEffectRule(1500.toBigDecimal(), "镇静", 1),
            DoseEffectRule(2000.toBigDecimal(), "善记", 2),
            DoseEffectRule(2000.toBigDecimal(), "镇静", 1),
            DoseEffectRule(2500.toBigDecimal(), "镇静", 1),
            DoseEffectRule(2500.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(3000.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(4000.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(4000.toBigDecimal(), "恶心", 1),
        ),
    )

    private val pregnenolone = d(
        key = "pregnenolone", name = "孕烯醇酮", colorArgb = 0xFFA3984B, tabletDose = 25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("记忆增强剂"),
        effectTags = listOf("神经固醇类"),
        moleculeTag = "神经固醇类", halfLifeHours = 2.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(10.toBigDecimal(), "欣快", 1),
            DoseEffectRule(10.toBigDecimal(), "善记", 1),
            DoseEffectRule(25.toBigDecimal(), "欣快", 1),
            DoseEffectRule(25.toBigDecimal(), "善记", 1),
            DoseEffectRule(50.toBigDecimal(), "善记", 1),
            DoseEffectRule(50.toBigDecimal(), "专注", 1),
            DoseEffectRule(75.toBigDecimal(), "善记", 2),
            DoseEffectRule(100.toBigDecimal(), "欣快", 1),
            DoseEffectRule(100.toBigDecimal(), "失眠", 1),
            DoseEffectRule(150.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(200.toBigDecimal(), "失眠", 1),
            DoseEffectRule(200.toBigDecimal(), "焦虑", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
        ),
    )

    private val five_htp = d(
        key = "five_htp", name = "5-羟基色氨酸", colorArgb = 0xFF7E9B52, tabletDose = 50.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("抗抑郁剂", "镇静剂"),
        effectTags = listOf("血清素前体"),
        moleculeTag = "氨基酸类", halfLifeHours = 2.0, bioavailabilityPct = 55, route = Route.ORAL,
        tmaxHours = 1.5,
        rules = listOf(
            DoseEffectRule(25.toBigDecimal(), "欣快", 1),
            DoseEffectRule(25.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(50.toBigDecimal(), "欣快", 1),
            DoseEffectRule(50.toBigDecimal(), "释然", 1),
            DoseEffectRule(50.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(100.toBigDecimal(), "欣快", 2),
            DoseEffectRule(100.toBigDecimal(), "恶心", 1),
            DoseEffectRule(150.toBigDecimal(), "欣快", 2),
            DoseEffectRule(200.toBigDecimal(), "欣快", 2),
            DoseEffectRule(200.toBigDecimal(), "恶心", 2),
            DoseEffectRule(250.toBigDecimal(), "出汗", 1),
            DoseEffectRule(300.toBigDecimal(), "血清素综合征", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 2),
        ),
    )

    private val tryptophan = d(
        key = "tryptophan", name = "L-色氨酸", colorArgb = 0xFF88A35A, tabletDose = 500.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("镇静剂", "抗抑郁剂"),
        effectTags = listOf("血清素前体"),
        moleculeTag = "氨基酸类", halfLifeHours = 2.5, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(250.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(250.toBigDecimal(), "欣快", 1),
            DoseEffectRule(500.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(500.toBigDecimal(), "欣快", 1),
            DoseEffectRule(500.toBigDecimal(), "释然", 1),
            DoseEffectRule(750.toBigDecimal(), "欣快", 1),
            DoseEffectRule(1000.toBigDecimal(), "欣快", 1),
            DoseEffectRule(1000.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(1500.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(1500.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(2000.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(2000.toBigDecimal(), "恶心", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val stjohnswort = d(
        key = "stjohnswort", name = "贯叶连翘（圣约翰草）", colorArgb = 0xFFB5A03C, tabletDose = 300.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("抗抑郁剂"),
        effectTags = listOf("血清素再摄取抑制剂"),
        moleculeTag = "贯叶连翘提取物", halfLifeHours = 9.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP3A4"),
        inducers = listOf("CYP3A4", "CYP2C9", "CYP1A2", "CYP2C19", "CYP2D6"),
        rules = listOf(
            DoseEffectRule(300.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(300.toBigDecimal(), "释然", 1),
            DoseEffectRule(600.toBigDecimal(), "释然", 2),
            DoseEffectRule(600.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(900.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(900.toBigDecimal(), "释然", 2),
            DoseEffectRule(900.toBigDecimal(), "恶心", 1),
            DoseEffectRule(1200.toBigDecimal(), "愉悦", 2),
            DoseEffectRule(1200.toBigDecimal(), "释然", 2),
            DoseEffectRule(1200.toBigDecimal(), "恶心", 2),
            DoseEffectRule(1500.toBigDecimal(), "恶心", 2),
            DoseEffectRule(1500.toBigDecimal(), "出汗", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val valerian = d(
        key = "valerian", name = "缬草", colorArgb = 0xFF6E8B3D, tabletDose = 300.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("镇静剂"),
        effectTags = listOf("缬草酸类"),
        moleculeTag = "败酱科植物提取物", halfLifeHours = 9.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(150.toBigDecimal(), "镇静", 1),
            DoseEffectRule(300.toBigDecimal(), "镇静", 2),
            DoseEffectRule(300.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(450.toBigDecimal(), "镇静", 2),
            DoseEffectRule(600.toBigDecimal(), "镇静", 2),
            DoseEffectRule(600.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(900.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(900.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(1200.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(1200.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(1500.toBigDecimal(), "恍惚", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
        ),
    )

    private val kava = d(
        key = "kava", name = "卡瓦（卡瓦椒）", colorArgb = 0xFF7B8C4E, tabletDose = 100.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("镇静剂", "抗焦虑剂"),
        effectTags = listOf("卡瓦内酯类"),
        moleculeTag = "卡瓦内酯类", halfLifeHours = 9.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(70.toBigDecimal(), "镇静", 1),
            DoseEffectRule(70.toBigDecimal(), "释然", 1),
            DoseEffectRule(140.toBigDecimal(), "镇静", 2),
            DoseEffectRule(140.toBigDecimal(), "欣快", 1),
            DoseEffectRule(210.toBigDecimal(), "欣快", 1),
            DoseEffectRule(210.toBigDecimal(), "镇静", 2),
            DoseEffectRule(280.toBigDecimal(), "欣快", 1),
            DoseEffectRule(280.toBigDecimal(), "解离", 1),
            DoseEffectRule(350.toBigDecimal(), "解离", 1),
            DoseEffectRule(350.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(420.toBigDecimal(), "解离", 1),
            DoseEffectRule(420.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(490.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(560.toBigDecimal(), "肝损伤", 1),
            DoseEffectRule(700.toBigDecimal(), "肝损伤", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
        ),
    )

    private val ginkgo = d(
        key = "ginkgo", name = "银杏叶提取物", colorArgb = 0xFF9AA84E, tabletDose = 40.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("记忆增强剂"),
        effectTags = listOf("黄酮萜类"),
        moleculeTag = "银杏黄酮类", halfLifeHours = 4.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2C8", "CYP3A4"),
        rules = listOf(
            DoseEffectRule(40.toBigDecimal(), "善记", 1),
            DoseEffectRule(40.toBigDecimal(), "专注", 1),
            DoseEffectRule(80.toBigDecimal(), "善记", 1),
            DoseEffectRule(80.toBigDecimal(), "专注", 1),
            DoseEffectRule(120.toBigDecimal(), "善记", 2),
            DoseEffectRule(120.toBigDecimal(), "专注", 1),
            DoseEffectRule(160.toBigDecimal(), "善记", 2),
            DoseEffectRule(200.toBigDecimal(), "善记", 2),
            DoseEffectRule(240.toBigDecimal(), "善记", 2),
            DoseEffectRule(240.toBigDecimal(), "恶心", 1),
            DoseEffectRule(320.toBigDecimal(), "恶心", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1),
        ),
    )

    private val ginseng = d(
        key = "ginseng", name = "人参", colorArgb = 0xFFB08D57, tabletDose = 100.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("记忆增强剂", "中枢兴奋剂"),
        effectTags = listOf("人参皂苷类"),
        moleculeTag = "五加科植物提取物", halfLifeHours = 6.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(50.toBigDecimal(), "欣快", 1),
            DoseEffectRule(50.toBigDecimal(), "专注", 1),
            DoseEffectRule(100.toBigDecimal(), "欣快", 1),
            DoseEffectRule(100.toBigDecimal(), "专注", 1),
            DoseEffectRule(200.toBigDecimal(), "欣快", 1),
            DoseEffectRule(200.toBigDecimal(), "专注", 2),
            DoseEffectRule(300.toBigDecimal(), "欣快", 1),
            DoseEffectRule(300.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(400.toBigDecimal(), "欣快", 2),
            DoseEffectRule(400.toBigDecimal(), "失眠", 1),
            DoseEffectRule(400.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(600.toBigDecimal(), "失眠", 1),
            DoseEffectRule(600.toBigDecimal(), "高血压", 1),
            DoseEffectRule(800.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(800.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 1),
        ),
    )

    private val fishoil = d(
        key = "fishoil", name = "鱼油（ω-3脂肪酸）", colorArgb = 0xFF6B8E23, tabletDose = 500.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("记忆增强剂", "抗抑郁剂"),
        effectTags = listOf("ω-3多不饱和脂肪酸"),
        moleculeTag = "ω-3多不饱和脂肪酸", halfLifeHours = 20.0, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 5.0,
        rules = listOf(
            DoseEffectRule(250.toBigDecimal(), "善记", 1),
            DoseEffectRule(250.toBigDecimal(), "释然", 1),
            DoseEffectRule(500.toBigDecimal(), "善记", 1),
            DoseEffectRule(500.toBigDecimal(), "释然", 1),
            DoseEffectRule(1000.toBigDecimal(), "善记", 1),
            DoseEffectRule(1000.toBigDecimal(), "释然", 1),
            DoseEffectRule(1500.toBigDecimal(), "善记", 1),
            DoseEffectRule(1500.toBigDecimal(), "释然", 1),
            DoseEffectRule(2000.toBigDecimal(), "善记", 1),
            DoseEffectRule(2000.toBigDecimal(), "释然", 1),
            DoseEffectRule(2000.toBigDecimal(), "恶心", 1),
            DoseEffectRule(3000.toBigDecimal(), "善记", 1),
            DoseEffectRule(3000.toBigDecimal(), "恶心", 1),
            DoseEffectRule(4000.toBigDecimal(), "恶心", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val inositol = d(
        key = "inositol", name = "肌醇", colorArgb = 0xFFB7AE7C, tabletDose = 1000.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("抗抑郁剂", "抗强迫剂"),
        effectTags = listOf("环状糖醇类"),
        moleculeTag = "环状糖醇类", halfLifeHours = 8.0, bioavailabilityPct = 80, route = Route.ORAL,
        tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(500.toBigDecimal(), "释然", 1),
            DoseEffectRule(500.toBigDecimal(), "强迫", -1),
            DoseEffectRule(1000.toBigDecimal(), "释然", 1),
            DoseEffectRule(1000.toBigDecimal(), "强迫", -1),
            DoseEffectRule(2000.toBigDecimal(), "释然", 1),
            DoseEffectRule(2000.toBigDecimal(), "强迫", -2),
            DoseEffectRule(3000.toBigDecimal(), "释然", 1),
            DoseEffectRule(3000.toBigDecimal(), "强迫", -2),
            DoseEffectRule(4000.toBigDecimal(), "释然", 2),
            DoseEffectRule(4000.toBigDecimal(), "强迫", -2),
            DoseEffectRule(6000.toBigDecimal(), "释然", 2),
            DoseEffectRule(8000.toBigDecimal(), "释然", 2),
            DoseEffectRule(8000.toBigDecimal(), "恶心", 1),
            DoseEffectRule(10000.toBigDecimal(), "恶心", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val ltheanine = d(
        key = "ltheanine", name = "L-茶氨酸", colorArgb = 0xFF83A36A, tabletDose = 100.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("镇静剂"),
        effectTags = listOf("氨基酸类"),
        moleculeTag = "氨基酸类", halfLifeHours = 1.0, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 1.0,
        rules = listOf(
            DoseEffectRule(50.toBigDecimal(), "镇静", 1),
            DoseEffectRule(50.toBigDecimal(), "专注", 1),
            DoseEffectRule(100.toBigDecimal(), "镇静", 1),
            DoseEffectRule(100.toBigDecimal(), "释然", 1),
            DoseEffectRule(200.toBigDecimal(), "镇静", 1),
            DoseEffectRule(200.toBigDecimal(), "释然", 1),
            DoseEffectRule(300.toBigDecimal(), "镇静", 2),
            DoseEffectRule(300.toBigDecimal(), "释然", 1),
            DoseEffectRule(400.toBigDecimal(), "镇静", 2),
            DoseEffectRule(400.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(500.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(600.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(800.toBigDecimal(), "嗜睡", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 1),
        ),
    )

    private val nicotine = d(
        key = "nicotine", name = "尼古丁", colorArgb = 0xFF5D4037, tabletDose = 1.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.SPECIAL,
        subjectiveTags = listOf("中枢兴奋剂"),
        effectTags = listOf("烟碱受体激动剂"),
        moleculeTag = "吡啶-吡咯烷生物碱", halfLifeHours = 2.0, bioavailabilityPct = 30, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP2A6", "CYP2B6"),
        inducers = listOf("CYP1A2"),
        rules = listOf(
            DoseEffectRule(0.5.toBigDecimal(), "专注", 1),
            DoseEffectRule(0.5.toBigDecimal(), "欣快", 1),
            DoseEffectRule(1.toBigDecimal(), "专注", 1),
            DoseEffectRule(1.toBigDecimal(), "欣快", 1),
            DoseEffectRule(2.toBigDecimal(), "专注", 2),
            DoseEffectRule(2.toBigDecimal(), "欣快", 1),
            DoseEffectRule(3.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(4.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(4.toBigDecimal(), "恶心", 1),
            DoseEffectRule(6.toBigDecimal(), "恶心", 1),
            DoseEffectRule(6.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(8.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(8.toBigDecimal(), "恶心", 2),
            DoseEffectRule(10.toBigDecimal(), "焦虑", 2),
            DoseEffectRule(12.toBigDecimal(), "恶心", 2),
            DoseEffectRule(14.toBigDecimal(), "心动过速", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1),
            NeuroDelta(Neurotransmitters.DOPAMINE, 1),
        ),
    )

    private val ethanol = d(
        key = "ethanol", name = "乙醇", colorArgb = 0xFF8D6E63, tabletDose = 10.toBigDecimal(),
        doseUnit = DoseUnit.G, managementClass = ManagementClass.SPECIAL,
        subjectiveTags = listOf("镇静剂"),
        effectTags = listOf("乙醇", "GABA受体激动剂"),
        moleculeTag = "醇类", halfLifeHours = 0.5, bioavailabilityPct = 80, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP2E1"),
        inducers = listOf("CYP2E1"),
        rules = listOf(
            DoseEffectRule(5.toBigDecimal(), "欣快", 1),
            DoseEffectRule(5.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(10.toBigDecimal(), "欣快", 1),
            DoseEffectRule(10.toBigDecimal(), "镇静", 1),
            DoseEffectRule(10.toBigDecimal(), "释然", 1),
            DoseEffectRule(15.toBigDecimal(), "欣快", 2),
            DoseEffectRule(15.toBigDecimal(), "镇静", 1),
            DoseEffectRule(20.toBigDecimal(), "欣快", 2),
            DoseEffectRule(20.toBigDecimal(), "镇静", 2),
            DoseEffectRule(20.toBigDecimal(), "释然", 1),
            DoseEffectRule(30.toBigDecimal(), "镇静", 2),
            DoseEffectRule(30.toBigDecimal(), "解离", 1),
            DoseEffectRule(40.toBigDecimal(), "镇静", 2),
            DoseEffectRule(40.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(40.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(50.toBigDecimal(), "解离", 1),
            DoseEffectRule(60.toBigDecimal(), "镇静", 3),
            DoseEffectRule(60.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(70.toBigDecimal(), "恍惚", 2),
            DoseEffectRule(80.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(100.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(100.toBigDecimal(), "恍惚", 2),
            DoseEffectRule(120.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(150.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(150.toBigDecimal(), "呼吸抑制", 2),
            DoseEffectRule(200.toBigDecimal(), "呼吸抑制", 2),
            DoseEffectRule(200.toBigDecimal(), "谵妄", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 2),
            NeuroDelta(Neurotransmitters.GLUTAMATE, -1),
        ),
    )

    private val arecoline = d(
        key = "arecoline", name = "槟榔碱", colorArgb = 0xFF6D4C41, tabletDose = 1.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.SPECIAL,
        subjectiveTags = listOf("中枢兴奋剂"),
        effectTags = listOf("毒蕈碱型乙酰胆碱受体激动剂"),
        moleculeTag = "吡啶-哌啶生物碱", halfLifeHours = 1.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 0.5,
        rules = listOf(
            DoseEffectRule(0.5.toBigDecimal(), "欣快", 1),
            DoseEffectRule(0.5.toBigDecimal(), "专注", 1),
            DoseEffectRule(1.toBigDecimal(), "欣快", 1),
            DoseEffectRule(1.toBigDecimal(), "专注", 1),
            DoseEffectRule(2.toBigDecimal(), "欣快", 1),
            DoseEffectRule(2.toBigDecimal(), "专注", 1),
            DoseEffectRule(2.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(3.toBigDecimal(), "欣快", 1),
            DoseEffectRule(3.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(3.toBigDecimal(), "出汗", 1),
            DoseEffectRule(4.toBigDecimal(), "恶心", 1),
            DoseEffectRule(4.toBigDecimal(), "出汗", 1),
            DoseEffectRule(6.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(6.toBigDecimal(), "恶心", 1),
            DoseEffectRule(8.toBigDecimal(), "焦虑", 1),
            DoseEffectRule(8.toBigDecimal(), "恶心", 2),
            DoseEffectRule(10.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 2),
        ),
    )

    /** 全部内置药物 */
    val all: List<DrugInfo> = listOf(
        fluvoxamine, sertraline, paroxetine, alprazolam, ketamine,
        trihexyphenidyl, venlafaxine, morphine, lsd, caffeine,
        fluoxetine, citalopram, escitalopram, duloxetine, bupropion,
        mirtazapine, trazodone, vortioxetine, tianeptine, reboxetine,
        milnacipran, amitriptyline, imipramine, clomipramine, doxepin,
        nortriptyline, maprotiline, mianserin, phenelzine, tranylcypromine,
        moclobemide, selegiline, chlorpromazine, haloperidol, perphenazine,
        fluphenazine, thioridazine, trifluoperazine, sulpiride, clozapine,
        risperidone, olanzapine, quetiapine, ziprasidone, paliperidone,
        asenapine, lurasidone, amisulpride, aripiprazole, brexpiprazole,
        cariprazine, lithium, valproate, carbamazepine, lamotrigine,
        oxcarbazepine, diazepam, lorazepam, clonazepam, estazolam,
        zolpidem, zopiclone, eszopiclone, buspirone, tandospirone,
        phenobarbital, promethazine, benzatropine, methylphenidate, atomoxetine,
        modafinil, codeine, tramadol, fentanyl, oxycodone,
        hydrocodone, methadone, buprenorphine, pethidine, pentazocine,
        tapentadol, dextromethorphan, acetaminophen, ibuprofen, naproxen,
        aspirin, diclofenac, celecoxib, nefopam, donepezil,
        galantamine, rivastigmine, huperzine, melatonin, magnesium_glycinate,
        magnesium_threonate, pregnenolone, five_htp, tryptophan, stjohnswort,
        valerian, kava, ginkgo, ginseng, fishoil,
        inositol, ltheanine, nicotine, ethanol, arecoline,
    )

    private val byKey = all.associateBy { it.builtinKey }

    fun byBuiltinKey(key: String): DrugInfo? = byKey[key]
}

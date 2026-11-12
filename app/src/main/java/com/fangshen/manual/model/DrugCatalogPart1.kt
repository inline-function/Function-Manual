package com.fangshen.manual.model

/** 药库分片：条目过多，单一 object 的 <clinit> 会超过 JVM 方法体上限，故拆分。 */
internal object DrugCatalogPart1 {

    private val fluvoxamine = d(
        key = "fluvoxamine", name = "氟伏沙明", colorArgb = 0xFFC9A227, tabletDose = 50.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗焦虑剂", "抗强迫剂"),
        effectTags = listOf("选择性血清素再摄取抑制剂", "血清素再摄取抑制剂"),
        moleculeTag = "", halfLifeHours = 15.0, bioavailabilityPct = 53, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("CYP2D6", "CYP1A2"),
        rules = listOf(
            DoseEffectRule(50.0.toBigDecimal(), "愉悦", 2, isChronic = true),
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
            DoseEffectRule(50.0.toBigDecimal(), "愉悦", 1, isChronic = true),
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
            DoseEffectRule(10.0.toBigDecimal(), "愉悦", 1, isChronic = true),
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
            DoseEffectRule(0.4.toBigDecimal(), "镇静", 1, isChronic = true),
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
        activeMetabolites = listOf(ActiveMetabolite(key = "norfluoxetine", fmPct = 100, potency = 0.9, formationDelayHours = 4.0)),
        rules = listOf(
            DoseEffectRule(20.toBigDecimal(), "愉悦", 1, isChronic = true),
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
            DoseEffectRule(20.toBigDecimal(), "愉悦", 1, isChronic = true),
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
            DoseEffectRule(5.toBigDecimal(), "愉悦", 1, isChronic = true),
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
        activeMetabolites = listOf(ActiveMetabolite(key = "paliperidone", fmPct = 60, potency = 0.6)),
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


    val entries: List<DrugInfo> = listOf(
        fluvoxamine, sertraline, paroxetine, alprazolam, ketamine,
        trihexyphenidyl, venlafaxine, morphine, lsd, caffeine,
        fluoxetine, citalopram, escitalopram, duloxetine, bupropion,
        mirtazapine, trazodone, vortioxetine, tianeptine, reboxetine,
        milnacipran, amitriptyline, imipramine, clomipramine, doxepin,
        nortriptyline, maprotiline, mianserin, phenelzine, tranylcypromine,
        moclobemide, selegiline, chlorpromazine, haloperidol, perphenazine,
        fluphenazine, thioridazine, trifluoperazine, sulpiride, clozapine,
        risperidone, olanzapine, quetiapine, ziprasidone, paliperidone,
        asenapine, lurasidone, amisulpride, aripiprazole
    )
}

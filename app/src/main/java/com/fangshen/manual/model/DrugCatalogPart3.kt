package com.fangshen.manual.model

/** 药库分片：条目过多，单一 object 的 <clinit> 会超过 JVM 方法体上限，故拆分。 */
internal object DrugCatalogPart3 {

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
        vdLiters = 35.0,
        nonlinearity = MichaelisMenten(vmaxMgPerLPerHour = 200.0, kmMgPerL = 30.0),
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

    // —— 加巴喷丁类（α2δ 钙通道配体）：抗焦虑、神经痛 ——
    private val pregabalin = d(
        key = "pregabalin", name = "普瑞巴林", colorArgb = 0xFF4CAF50, tabletDose = 75.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("抗焦虑剂", "镇痛剂"),
        effectTags = listOf("加巴喷丁类"),
        moleculeTag = "加巴喷丁类", halfLifeHours = 6.5, bioavailabilityPct = 90,
        route = Route.ORAL, tmaxHours = 1.5,
        rules = listOf(
            DoseEffectRule(75.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(150.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(300.0.toBigDecimal(), "镇静", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GLUTAMATE, -1)),
    )

    private val gabapentin = d(
        key = "gabapentin", name = "加巴喷丁", colorArgb = 0xFF4CAF50, tabletDose = 300.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗焦虑剂", "镇痛剂"),
        effectTags = listOf("加巴喷丁类"),
        moleculeTag = "加巴喷丁类", halfLifeHours = 5.5, bioavailabilityPct = 40,
        route = Route.ORAL, tmaxHours = 3.0,
        saturableAbsorption = SaturableAbsorption(d50Mg = 390.0, fMaxPct = 100),
        rules = listOf(
            DoseEffectRule(300.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(900.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(1800.0.toBigDecimal(), "镇静", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GLUTAMATE, -1)),
    )

    // —— NMDA 受体拮抗剂（解离剂）：金刚烷胺、美金刚、地佐环平 ——
    private val amantadine = d(
        key = "amantadine", name = "金刚烷胺", colorArgb = 0xFF90CAF9, tabletDose = 100.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("解离剂", "中枢兴奋剂"),
        effectTags = listOf("NMDA受体拮抗剂", "多巴胺释放促进剂"),
        moleculeTag = "金刚烷胺类", halfLifeHours = 12.0, bioavailabilityPct = 90,
        route = Route.ORAL, tmaxHours = 4.0,
        rules = listOf(
            DoseEffectRule(100.0.toBigDecimal(), "失眠", 1),
            DoseEffectRule(200.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(300.0.toBigDecimal(), "解离", 1),
            DoseEffectRule(500.0.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.DOPAMINE, 1)),
    )

    private val memantine = d(
        key = "memantine", name = "美金刚", colorArgb = 0xFF90CAF9, tabletDose = 10.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("解离剂", "认知增强剂"),
        effectTags = listOf("NMDA受体拮抗剂"),
        moleculeTag = "金刚烷胺类", halfLifeHours = 75.0, bioavailabilityPct = 95,
        route = Route.ORAL, tmaxHours = 6.0,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "善记", 1),
            DoseEffectRule(10.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(20.0.toBigDecimal(), "解离", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GABA, 0), // 占位避免误读：美金刚不直接作用于 GABA
            NeuroDelta(Neurotransmitters.GLUTAMATE, -1),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1),
        ),
    )

    private val dizocilpine = d(
        key = "dizocilpine", name = "地佐环平", colorArgb = 0xFF90CAF9, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RESEARCH,
        subjectiveTags = listOf("解离剂"),
        effectTags = listOf("NMDA受体拮抗剂"),
        moleculeTag = "二苯并环庚烯亚胺类", halfLifeHours = 2.0, bioavailabilityPct = 100,
        route = Route.IV,
        rules = listOf(
            DoseEffectRule(0.05.toBigDecimal(), "解离", 1),
            DoseEffectRule(0.05.toBigDecimal(), "镇静", 1),
            DoseEffectRule(0.1.toBigDecimal(), "解离", 2),
            DoseEffectRule(0.2.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GLUTAMATE, -2)),
    )

    // —— 抗组胺 / 抗胆碱能谵妄剂（苯海拉明类）——
    private val diphenhydramine = d(
        key = "diphenhydramine", name = "苯海拉明", colorArgb = 0xFF795548, tabletDose = 25.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.OTC_A,
        subjectiveTags = listOf("谵妄剂", "镇静剂"),
        effectTags = listOf("抗组胺剂", "抗胆碱能剂", "乙醇胺类"),
        moleculeTag = "乙醇胺类抗组胺剂", halfLifeHours = 8.0, bioavailabilityPct = 60,
        route = Route.ORAL, tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(25.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(50.0.toBigDecimal(), "口干", 1),
            DoseEffectRule(100.0.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(200.0.toBigDecimal(), "谵妄", 2),
            DoseEffectRule(200.0.toBigDecimal(), "视物模糊", 1),
            DoseEffectRule(400.0.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(400.0.toBigDecimal(), "尿潴留", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val dimenhydrinate = d(
        key = "dimenhydrinate", name = "茶苯海明", colorArgb = 0xFF795548, tabletDose = 50.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.OTC_A,
        subjectiveTags = listOf("谵妄剂", "镇静剂"),
        effectTags = listOf("抗组胺剂", "抗胆碱能剂", "乙醇胺类"),
        moleculeTag = "8-氯茶碱盐（苯海拉明复合盐）", halfLifeHours = 5.5, bioavailabilityPct = 60,
        route = Route.ORAL, tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(50.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(100.0.toBigDecimal(), "口干", 1),
            DoseEffectRule(200.0.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(400.0.toBigDecimal(), "谵妄", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val doxylamine = d(
        key = "doxylamine", name = "多西拉敏", colorArgb = 0xFF4527A0, tabletDose = 25.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("催眠剂", "谵妄剂"),
        effectTags = listOf("抗组胺剂", "抗胆碱能剂", "乙醇胺类"),
        moleculeTag = "乙醇胺类抗组胺剂", halfLifeHours = 10.0, bioavailabilityPct = 25,
        route = Route.ORAL, tmaxHours = 3.0,
        rules = listOf(
            DoseEffectRule(25.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(50.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(75.0.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(150.0.toBigDecimal(), "谵妄", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.HISTAMINE, -2),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val scopolamine = d(
        key = "scopolamine", name = "东莨菪碱", colorArgb = 0xFF795548, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("谵妄剂"),
        effectTags = listOf("抗胆碱能剂", "毒蕈碱受体拮抗剂"),
        moleculeTag = "托烷类生物碱", halfLifeHours = 8.0, bioavailabilityPct = 28,
        route = Route.TRANSDERMAL, tmaxHours = 24.0,
        rules = listOf(
            DoseEffectRule(0.5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(1.0.toBigDecimal(), "口干", 1),
            DoseEffectRule(2.0.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(5.0.toBigDecimal(), "谵妄", 2),
            DoseEffectRule(5.0.toBigDecimal(), "视物模糊", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -2)),
    )

    // —— 苯丙胺类兴奋剂 / 共情剂（常见毒品）——
    private val methamphetamine = d(
        key = "methamphetamine", name = "甲基苯丙胺", colorArgb = 0xFFE53935, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("中枢兴奋剂", "欣快剂"),
        effectTags = listOf("苯丙胺类", "多巴胺释放促进剂", "血清素释放剂", "中枢兴奋剂"),
        moleculeTag = "苯丙胺类", halfLifeHours = 10.0, bioavailabilityPct = 60,
        route = Route.INHALATION, tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(10.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(20.0.toBigDecimal(), "失眠", 1),
            DoseEffectRule(50.0.toBigDecimal(), "欣快", 2),
            DoseEffectRule(100.0.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 3),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 2),
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val amphetamine = d(
        key = "amphetamine", name = "苯丙胺", colorArgb = 0xFFE53935, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("中枢兴奋剂", "欣快剂"),
        effectTags = listOf("苯丙胺类", "多巴胺释放促进剂", "中枢兴奋剂"),
        moleculeTag = "苯丙胺类", halfLifeHours = 10.0, bioavailabilityPct = 30,
        route = Route.ORAL, tmaxHours = 3.0,
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(10.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(20.0.toBigDecimal(), "失眠", 1),
            DoseEffectRule(40.0.toBigDecimal(), "欣快", 2),
            DoseEffectRule(60.0.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 2),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 2),
        ),
    )

    private val mdma = d(
        key = "mdma", name = "MDMA", colorArgb = 0xFFF48FB1, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("共情剂", "中枢兴奋剂"),
        effectTags = listOf("苯丙胺类", "血清素释放剂", "多巴胺释放促进剂", "中枢兴奋剂"),
        moleculeTag = "苯异丙胺类（亚甲二氧基）", halfLifeHours = 8.0, bioavailabilityPct = 60,
        route = Route.ORAL, tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(50.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(100.0.toBigDecimal(), "敏知", 1),
            DoseEffectRule(150.0.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(200.0.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 3),
            NeuroDelta(Neurotransmitters.DOPAMINE, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
        ),
    )

    private val cocaine = d(
        key = "cocaine", name = "可卡因", colorArgb = 0xFFE53935, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("中枢兴奋剂", "欣快剂"),
        effectTags = listOf("单胺再摄取抑制剂", "血清素再摄取抑制剂", "中枢兴奋剂", "局部麻醉剂"),
        moleculeTag = "苯甲酰芽子碱甲酯（古柯生物碱）", halfLifeHours = 1.0, bioavailabilityPct = 40,
        route = Route.INSUFFLATION, tmaxHours = 0.75,
        rules = listOf(
            DoseEffectRule(20.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(50.0.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(100.0.toBigDecimal(), "欣快", 2),
            DoseEffectRule(150.0.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 2),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 2),
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
        ),
    )

    private val mephedrone = d(
        key = "mephedrone", name = "甲卡西酮", colorArgb = 0xFFF48FB1, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("共情剂", "中枢兴奋剂"),
        effectTags = listOf("卡西酮类", "血清素释放剂", "多巴胺释放促进剂", "中枢兴奋剂"),
        moleculeTag = "合成卡西酮类", halfLifeHours = 4.5, bioavailabilityPct = 50,
        route = Route.INSUFFLATION, tmaxHours = 1.0,
        rules = listOf(
            DoseEffectRule(50.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(100.0.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(150.0.toBigDecimal(), "欣快", 2),
            DoseEffectRule(200.0.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 2),
            NeuroDelta(Neurotransmitters.DOPAMINE, 2),
        ),
    )

    private val khat = d(
        key = "khat", name = "恰特草", colorArgb = 0xFF9E9D24, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("中枢兴奋剂"),
        effectTags = listOf("卡西酮类", "中枢兴奋剂"),
        moleculeTag = "天然卡西酮（植物叶片）", halfLifeHours = 1.5, bioavailabilityPct = 50,
        route = Route.ORAL, tmaxHours = 1.5,
        rules = listOf(
            DoseEffectRule(1000.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(5000.0.toBigDecimal(), "失眠", 1),
            DoseEffectRule(10000.0.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
        ),
    )

    // —— 阿片类（含违禁品种）——
    private val heroin = d(
        key = "heroin", name = "海洛因", colorArgb = 0xFFEF9A9A, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("镇痛剂", "中枢抑制剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "二乙酰吗啡（半合成阿片）", halfLifeHours = 2.0, bioavailabilityPct = 100,
        activeMetabolites = listOf(ActiveMetabolite(key = "morphine", fmPct = 100, potency = 1.5)),
        route = Route.IV, tmaxHours = null,
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(10.0.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(20.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(40.0.toBigDecimal(), "瞳孔缩小", 1),
            DoseEffectRule(60.0.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(80.0.toBigDecimal(), "欣快", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.ENDORPHIN, 2)),
    )

    private val loperamide = d(
        key = "loperamide", name = "洛哌丁胺", colorArgb = 0xFFEF9A9A, tabletDose = 2.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.OTC_A,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "外周作用阿片（哌啶类）", halfLifeHours = 10.5, bioavailabilityPct = 1,
        route = Route.ORAL, tmaxHours = 5.0,
        rules = listOf(
            DoseEffectRule(2.0.toBigDecimal(), "便秘", 1),
            DoseEffectRule(20.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(40.0.toBigDecimal(), "便秘", 2),
        ),
    )

    // —— 大麻类 ——
    private val cannabis = d(
        key = "cannabis", name = "大麻", colorArgb = 0xFF8E24AA, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("迷幻剂", "镇静剂"),
        effectTags = listOf("大麻素受体激动剂"),
        moleculeTag = "植物源性大麻素（Δ9-THC 为主）", halfLifeHours = 8.0, bioavailabilityPct = 30,
        route = Route.INHALATION, tmaxHours = 0.5,
        rules = listOf(
            DoseEffectRule(2.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(5.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(10.0.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(20.0.toBigDecimal(), "敏知", 1),
            DoseEffectRule(30.0.toBigDecimal(), "心动过速", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.DOPAMINE, 1)),
    )

    // —— 迷幻剂（色胺类 / 苯乙胺类）——
    private val psilocybin = d(
        key = "psilocybin", name = "裸盖菇素", colorArgb = 0xFF8E24AA, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("迷幻剂"),
        effectTags = listOf("血清素受体激动剂", "色胺类"),
        moleculeTag = "色胺类（4-磷酸氧基-N,N-二甲基色胺）", halfLifeHours = 2.5, bioavailabilityPct = 50,
        route = Route.ORAL, tmaxHours = 1.5,
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "敏知", 1),
            DoseEffectRule(10.0.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(25.0.toBigDecimal(), "恍惚", 2),
            DoseEffectRule(40.0.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.SEROTONIN, 1)),
    )

    private val mescaline = d(
        key = "mescaline", name = "麦司卡林", colorArgb = 0xFF8E24AA, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("迷幻剂"),
        effectTags = listOf("血清素受体激动剂", "苯乙胺类"),
        moleculeTag = "苯乙胺类（三甲氧基苯乙胺）", halfLifeHours = 6.0, bioavailabilityPct = 30,
        route = Route.ORAL, tmaxHours = 4.0,
        rules = listOf(
            DoseEffectRule(100.0.toBigDecimal(), "敏知", 1),
            DoseEffectRule(200.0.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(400.0.toBigDecimal(), "恍惚", 2),
            DoseEffectRule(600.0.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.SEROTONIN, 1),
            NeuroDelta(Neurotransmitters.DOPAMINE, 1),
        ),
    )

    private val dmt = d(
        key = "dmt", name = "DMT", colorArgb = 0xFF8E24AA, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("迷幻剂"),
        effectTags = listOf("血清素受体激动剂", "色胺类"),
        moleculeTag = "色胺类（N,N-二甲基色胺）", halfLifeHours = 0.4, bioavailabilityPct = 75,
        route = Route.INHALATION, tmaxHours = 0.25,
        rules = listOf(
            DoseEffectRule(10.0.toBigDecimal(), "敏知", 1),
            DoseEffectRule(20.0.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(40.0.toBigDecimal(), "恍惚", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.SEROTONIN, 2)),
    )

    // —— 解离剂（笑气、PCP）——
    private val nitrous_oxide = d(
        key = "nitrous_oxide", name = "一氧化二氮", colorArgb = 0xFF90CAF9, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HAZARDOUS,
        subjectiveTags = listOf("解离剂"),
        effectTags = listOf("NMDA受体拮抗剂"),
        moleculeTag = "笑气（医用麻醉气体）", halfLifeHours = 0.25, bioavailabilityPct = 100,
        route = Route.INHALATION, tmaxHours = 0.1,
        description = "作用仅数十秒至数分钟；长期滥用耗竭维生素 B12，造成脊髓亚急性联合变性（不可逆的麻木与无力）。",
        addictive = true, addictionRatePerHour = 0.25,
        rules = listOf(
            DoseEffectRule(500.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(1000.0.toBigDecimal(), "解离", 1),
            DoseEffectRule(4000.0.toBigDecimal(), "解离", 2),
            DoseEffectRule(8000.0.toBigDecimal(), "恍惚", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GLUTAMATE, -1)),
    )

    private val pcp = d(
        key = "pcp", name = "苯环利定", colorArgb = 0xFF90CAF9, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("解离剂"),
        effectTags = listOf("NMDA受体拮抗剂", "芳基环己胺类"),
        moleculeTag = "芳基环己胺类", halfLifeHours = 7.0, bioavailabilityPct = 50,
        route = Route.ORAL, tmaxHours = 1.5,
        rules = listOf(
            DoseEffectRule(1.0.toBigDecimal(), "解离", 1),
            DoseEffectRule(3.0.toBigDecimal(), "解离", 2),
            DoseEffectRule(5.0.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(10.0.toBigDecimal(), "谵妄", 2),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.GLUTAMATE, -2),
            NeuroDelta(Neurotransmitters.DOPAMINE, 1),
        ),
    )

    // —— GHB 与助眠滥用品种 ——
    private val ghb = d(
        key = "ghb", name = "GHB", colorArgb = 0xFF4527A0, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("催眠剂", "欣快剂"),
        effectTags = listOf("GHB受体激动剂", "GABA受体激动剂"),
        moleculeTag = "γ-羟基丁酸（内源性神经递质）", halfLifeHours = 0.8, bioavailabilityPct = 90,
        route = Route.ORAL, tmaxHours = 0.75,
        rules = listOf(
            DoseEffectRule(500.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(1000.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(2000.0.toBigDecimal(), "欣快", 2),
            DoseEffectRule(2500.0.toBigDecimal(), "镇静", 2),
            DoseEffectRule(3000.0.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GABA, 1)),
    )

    private val amyl_nitrite = d(
        key = "amyl_nitrite", name = "亚硝酸异戊酯", colorArgb = 0xFFD81B60, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HAZARDOUS,
        subjectiveTags = listOf("欣快剂"),
        effectTags = listOf("一氧化氮供体"),
        moleculeTag = "短链亚硝酸酯（Poppers/Rush）", halfLifeHours = 0.25, bioavailabilityPct = 100,
        route = Route.INHALATION, tmaxHours = 0.1,
        description = "吸入后瞬间扩张血管、产生温热与欣快感；与伟哥类联用可致致命低血压。易燃且对皮肤黏膜腐蚀。",
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(50.0.toBigDecimal(), "潮红", 1),
            DoseEffectRule(80.0.toBigDecimal(), "低血压", 1),
            DoseEffectRule(20.0.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(50.0.toBigDecimal(), "欣快", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.NOREPINEPHRINE, -1)),
    )

    private val flunitrazepam = d(
        key = "flunitrazepam", name = "氟硝西泮", colorArgb = 0xFF607D8B, tabletDose = 1.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("催眠剂"),
        effectTags = listOf("苯二氮卓类受体激动剂", "GABA受体激动剂"),
        moleculeTag = "硝基苯二氮卓类", halfLifeHours = 18.0, bioavailabilityPct = 85,
        route = Route.ORAL, tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(1.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(2.0.toBigDecimal(), "镇静", 2),
            DoseEffectRule(4.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(6.0.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GABA, 1)),
    )

    private val triazolam = d(
        key = "triazolam", name = "三唑仑", colorArgb = 0xFF607D8B, tabletDose = 0.25.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_I,
        subjectiveTags = listOf("催眠剂"),
        effectTags = listOf("苯二氮卓类受体激动剂", "GABA受体激动剂"),
        moleculeTag = "三唑并苯二氮卓类", halfLifeHours = 2.5, bioavailabilityPct = 44,
        route = Route.ORAL, tmaxHours = 1.5,
        rules = listOf(
            DoseEffectRule(0.25.toBigDecimal(), "镇静", 1),
            DoseEffectRule(0.5.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(1.0.toBigDecimal(), "镇静", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GABA, 1)),
    )

    private val midazolam = d(
        key = "midazolam", name = "咪达唑仑", colorArgb = 0xFF607D8B, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("催眠剂"),
        effectTags = listOf("苯二氮卓类受体激动剂", "GABA受体激动剂"),
        moleculeTag = "咪唑并苯二氮卓类", halfLifeHours = 2.5, bioavailabilityPct = 100,
        route = Route.IV, tmaxHours = null,
        rules = listOf(
            DoseEffectRule(2.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(5.0.toBigDecimal(), "镇静", 2),
            DoseEffectRule(10.0.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GABA, 1)),
    )

    private val nitrazepam = d(
        key = "nitrazepam", name = "硝西泮", colorArgb = 0xFF607D8B, tabletDose = 5.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("催眠剂"),
        effectTags = listOf("苯二氮卓类受体激动剂", "GABA受体激动剂"),
        moleculeTag = "硝基苯二氮卓类", halfLifeHours = 26.0, bioavailabilityPct = 55,
        route = Route.ORAL, tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(10.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(20.0.toBigDecimal(), "镇静", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GABA, 1)),
    )

    private val methaqualone = d(
        key = "methaqualone", name = "甲喹酮", colorArgb = 0xFF4527A0, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("催眠剂"),
        effectTags = listOf("GABA受体激动剂", "喹唑啉酮类"),
        moleculeTag = "喹唑啉酮类（已退市）", halfLifeHours = 20.0, bioavailabilityPct = 80,
        route = Route.ORAL, tmaxHours = 2.0,
        rules = listOf(
            DoseEffectRule(75.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(150.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(300.0.toBigDecimal(), "镇静", 2),
            DoseEffectRule(500.0.toBigDecimal(), "呼吸抑制", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GABA, 1)),
    )

    // —— 拉西坦类（益智类促认知药）——
    private val piracetam = d(
        key = "piracetam", name = "吡拉西坦", colorArgb = 0xFFF57F17, tabletDose = 800.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("认知增强剂", "记忆增强剂"),
        effectTags = listOf("拉西坦类", "AMPA受体调节剂"),
        moleculeTag = "2-氧代吡咯烷类（环状 GABA 衍生物）", halfLifeHours = 5.0, bioavailabilityPct = 95,
        route = Route.ORAL, tmaxHours = 1.5,
        rules = listOf(
            DoseEffectRule(800.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(1600.0.toBigDecimal(), "善记", 1),
            DoseEffectRule(3200.0.toBigDecimal(), "善记", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1)),
    )

    private val aniracetam = d(
        key = "aniracetam", name = "阿尼西坦", colorArgb = 0xFFF57F17, tabletDose = 750.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("认知增强剂", "记忆增强剂"),
        effectTags = listOf("拉西坦类", "AMPA受体调节剂"),
        moleculeTag = "2-氧代吡咯烷类（茴香酰衍生物）", halfLifeHours = 2.0, bioavailabilityPct = 30,
        route = Route.ORAL, tmaxHours = 1.0,
        rules = listOf(
            DoseEffectRule(750.0.toBigDecimal(), "善记", 1),
            DoseEffectRule(1500.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(3000.0.toBigDecimal(), "镇静", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1)),
    )

    private val oxiracetam = d(
        key = "oxiracetam", name = "奥拉西坦", colorArgb = 0xFFF57F17, tabletDose = 800.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("认知增强剂", "记忆增强剂"),
        effectTags = listOf("拉西坦类", "AMPA受体调节剂"),
        moleculeTag = "2-氧代吡咯烷类（羟基衍生物）", halfLifeHours = 5.0, bioavailabilityPct = 75,
        route = Route.ORAL, tmaxHours = 1.5,
        rules = listOf(
            DoseEffectRule(800.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(1600.0.toBigDecimal(), "善记", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1)),
    )

    private val levetiracetam = d(
        key = "levetiracetam", name = "左乙拉西坦", colorArgb = 0xFFF57F17, tabletDose = 500.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("认知增强剂", "心境稳定剂"),
        effectTags = listOf("拉西坦类", "SV2A蛋白配体"),
        moleculeTag = "2-氧代吡咯烷类（乙基衍生物）", halfLifeHours = 7.0, bioavailabilityPct = 95,
        route = Route.ORAL, tmaxHours = 1.0,
        rules = listOf(
            DoseEffectRule(500.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(1500.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(3000.0.toBigDecimal(), "焦虑", 1),
        ),
    )

    private val phenylpiracetam = d(
        key = "phenylpiracetam", name = "芬洛西坦", colorArgb = 0xFFF57F17, tabletDose = 100.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("认知增强剂", "中枢兴奋剂"),
        effectTags = listOf("拉西坦类", "AMPA受体调节剂", "中枢兴奋剂"),
        moleculeTag = "2-氧代吡咯烷类（苯基衍生物）", halfLifeHours = 3.5, bioavailabilityPct = 80,
        route = Route.ORAL, tmaxHours = 1.0,
        rules = listOf(
            DoseEffectRule(100.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(200.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(400.0.toBigDecimal(), "失眠", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, 1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 1),
        ),
    )

    // —— 镇吐药 ——
    private val domperidone = d(
        key = "domperidone", name = "多潘立酮", colorArgb = 0xFF00BFA5, tabletDose = 10.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.OTC_A,
        subjectiveTags = listOf("镇吐剂"),
        effectTags = listOf("多巴胺受体拮抗剂（外周）"),
        moleculeTag = "苯并咪唑类（不易透血脑屏障）", halfLifeHours = 7.5, bioavailabilityPct = 15,
        route = Route.ORAL, tmaxHours = 1.5,
        substrates = listOf("CYP3A4"),
        rules = listOf(
            DoseEffectRule(10.0.toBigDecimal(), "镇吐", 1),
            DoseEffectRule(30.0.toBigDecimal(), "镇吐", 2),
        ),
    )

    private val metoclopramide = d(
        key = "metoclopramide", name = "甲氧氯普胺", colorArgb = 0xFF00BFA5, tabletDose = 10.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("镇吐剂"),
        effectTags = listOf("多巴胺受体拮抗剂"),
        moleculeTag = "苯甲酰胺类（胃复安）", halfLifeHours = 5.0, bioavailabilityPct = 80,
        route = Route.ORAL, tmaxHours = 1.5,
        rules = listOf(
            DoseEffectRule(10.0.toBigDecimal(), "镇吐", 1),
            DoseEffectRule(30.0.toBigDecimal(), "镇吐", 2),
        ),
    )


    val entries: List<DrugInfo> = listOf(
        tryptophan, stjohnswort, valerian, kava, ginkgo,
        ginseng, fishoil, inositol, ltheanine, nicotine,
        ethanol, arecoline, pregabalin, gabapentin, amantadine,
        memantine, dizocilpine, diphenhydramine, dimenhydrinate, doxylamine,
        scopolamine, methamphetamine, amphetamine, mdma, cocaine,
        mephedrone, khat, heroin, loperamide, cannabis,
        psilocybin, mescaline, dmt, nitrous_oxide, pcp,
        ghb, amyl_nitrite, flunitrazepam, triazolam, midazolam,
        nitrazepam, methaqualone, piracetam, aniracetam, oxiracetam,
        levetiracetam, phenylpiracetam, domperidone, metoclopramide
    )
}

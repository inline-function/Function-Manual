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
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "血清素能停药",
        description = "选择性5-羟色胺再摄取抑制剂，兼激动σ-1受体，口服后起效需二至四周，用于抑郁症、强迫症及广泛性焦虑等焦虑障碍，其中抗强迫作用最为突出；为CYP1A2与CYP2C19强抑制剂，与茶碱、华法林、氯氮平、苯妥英联用可使后者血药浓度明显升高、须减量，与单胺氧化酶抑制剂联用致5-羟色胺综合征属禁忌。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗强迫", 4, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("镇静", 0, 2), DrugAttribute("抗胆碱", 0, 1)),
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
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "血清素能停药",
        description = "选择性5-羟色胺再摄取抑制剂，对多巴胺载体有弱抑制，用于抑郁症、强迫症、惊恐障碍、社交焦虑、创伤后应激障碍及经前焦虑，适应证覆盖最广；对细胞色素P450酶抑制最弱、药物相互作用少，常见恶心、腹泻、出汗与性功能减退，与单胺氧化酶抑制剂联用属禁忌。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗强迫", 3, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("镇静", 0, 2), DrugAttribute("抗胆碱", 0, 1)),
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
        addictive = true, addictionRatePerHour = 0.008, toleranceGroup = "血清素能停药",
        description = "选择性5-羟色胺再摄取抑制剂，在同类中抗胆碱与镇静作用最强、停药综合征最重，用于抑郁症、强迫症、惊恐障碍、社交焦虑、广泛性焦虑及创伤后应激障碍；为CYP2D6强抑制剂，与三环类抗抑郁药、抗精神病药联用须减量，与单胺氧化酶抑制剂及硫利达嗪联用属禁忌，骤停可致头晕、感觉异常。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗强迫", 3, 5), DrugAttribute("抗焦虑", 3, 5), DrugAttribute("镇静", 1, 3), DrugAttribute("抗胆碱", 1, 3)),
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
        addictive = true, addictionRatePerHour = 0.03, toleranceGroup = "GABA能耐受",
        description = "苯二氮卓类GABA-A受体别构增强剂，起效快、半衰期短，用于惊恐障碍与广泛性焦虑障碍及抑郁症伴焦虑的短期辅助；快速耐受、生理依赖与戒断风险高，与阿片类镇痛药、乙醇联用可致致命性呼吸抑制，经CYP3A4代谢，与唑类抗真菌药、大环内酯类抗生素及葡萄柚汁联用血药浓度升高。",
        attributes = listOf(DrugAttribute("抗焦虑", 4, 5), DrugAttribute("镇静", 3, 5), DrugAttribute("催眠", 2, 4), DrugAttribute("抗抑郁", 0, 2), DrugAttribute("依赖性", -1, 5), DrugAttribute("呼吸抑制", -1, 3)),
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
        description = "NMDA受体非竞争性拮抗剂，兼激动μ阿片受体；用于麻醉、慢性疼痛与难治性癫痫持续状态，鼻喷或静脉给药数小时内快速改善难治性抑郁与自杀意念；可致分离性幻觉、血压升高与膀胱间质损伤，心理渴求与耐受进展快，与巴比妥类、阿片类联用加重呼吸抑制。",
        attributes = listOf(DrugAttribute("镇痛", 3, 5), DrugAttribute("抗抑郁", 2, 5), DrugAttribute("致幻", 2, 5), DrugAttribute("镇静", 1, 4), DrugAttribute("依赖性", -1, 4)),
        addictive = true, addictionRatePerHour = 0.04, toleranceGroup = "解离剂耐受",
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
        description = "中枢性M1型毒蕈碱受体拮抗剂，用于抗精神病药所致药源性帕金森综合征、帕金森病及抗精神病药所致流涎；外周抗胆碱副作用明显，口干、便秘、尿潴留与视物模糊常见，老年患者易发生谵妄与认知损害，青光眼及前列腺增生者慎用，长期用药者停药须逐渐减量、骤停可致症状反跳。",
        attributes = listOf(DrugAttribute("抗胆碱", 4, 5), DrugAttribute("抗帕金森", 3, 5), DrugAttribute("镇静", 0, 2)),
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
        addictive = true, addictionRatePerHour = 0.01, toleranceGroup = "血清素能停药",
        description = "5-羟色胺与去甲肾上腺素再摄取抑制剂，低剂量以5-HT效应为主、高剂量双通道增强，用于抑郁症、广泛性焦虑、社交焦虑与惊恐障碍，起效需二至四周；高剂量可致血压升高，骤停易出现头晕、感觉异常等停药综合征，对CYP2D6抑制弱、与三环类联用仍需谨慎，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗焦虑", 2, 5), DrugAttribute("兴奋", 0, 3), DrugAttribute("镇静", 0, 1), DrugAttribute("依赖性", -1, 2)),
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
        addictive = true, addictionRatePerHour = 0.03, toleranceGroup = "阿片耐受",
        description = "μ阿片受体完全激动剂，用于中重度癌痛、术后疼痛、急性心肌梗死镇痛及急性左心衰肺水肿；镇痛同时伴镇静、欣快、呼吸抑制、便秘与直立性低血压，反复使用耐受与生理依赖进展快，过量致昏迷、针尖样瞳孔与呼吸抑制，与苯二氮卓类、乙醇联用呼吸抑制可致命，纳洛酮可拮抗。",
        attributes = listOf(DrugAttribute("镇痛", 4, 5), DrugAttribute("镇静", 2, 4), DrugAttribute("欣快", 1, 4), DrugAttribute("呼吸抑制", -1, 5), DrugAttribute("依赖性", -1, 5), DrugAttribute("直立性低血压", -1, 3)),
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
        description = "麦角生物碱衍生的5-HT2A受体部分激动型致幻剂，兼作用于5-HT1A与多巴胺受体；曾用于心理治疗与酒精依赖研究，现无公认临床适应证；作用强烈持久，可致幻觉、联觉、思维混乱与闪回，无生理依赖与戒断反应，心动过速、瞳孔扩大、出汗常见，精神病高危者可诱发急性发作。",
        attributes = listOf(DrugAttribute("致幻", 4, 5), DrugAttribute("兴奋", 0, 3), DrugAttribute("欣快", 0, 4), DrugAttribute("镇静", 0, 1)),
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
        description = "腺苷A1/A2A受体拮抗剂并抑制磷酸二酯酶，用于提神、抗疲劳，常与解热镇痛药配伍增强偏头痛与轻度疼痛疗效；过量或敏感者出现心悸、失眠、焦虑与震颤，长期每日摄入可产生轻度生理依赖，骤停致头痛、困倦与易激惹，经CYP1A2代谢，吸烟诱导其清除、妊娠与口服避孕药抑制其清除。",
        attributes = listOf(DrugAttribute("兴奋", 3, 5), DrugAttribute("促认知", 1, 3), DrugAttribute("镇痛", 0, 2)),
        addictive = true, addictionRatePerHour = 0.02, toleranceGroup = "通用耐受",
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
        addictive = true, addictionRatePerHour = 0.004, toleranceGroup = "血清素能停药",
        description = "选择性5-羟色胺再摄取抑制剂，活性代谢物去甲氟西汀半衰期长达一至二周，用于抑郁症、强迫症、神经性贪食、惊恐障碍与经前焦虑；激活作用明显，初期可致焦虑、失眠与头痛，为CYP2D6与2C19强抑制剂，与三环类、抗精神病药联用需减量，停药综合征轻、罕见锥体外系反应，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗强迫", 3, 5), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("兴奋", 0, 2), DrugAttribute("抗胆碱", 0, 1)),
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
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "血清素能停药",
        description = "选择性5-羟色胺再摄取抑制剂，对细胞色素P450酶影响最小、药物相互作用少，用于抑郁症及各类焦虑障碍；常见恶心、出汗、头痛与性功能障碍，剂量依赖性QTc延长使最大剂量限于40mg/日，低钾低镁及联用其他延长QT药物时须心电监测，骤停可致头晕、感觉异常等停药综合征。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("抗强迫", 1, 3), DrugAttribute("镇静", 0, 2)),
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
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "血清素能停药",
        description = "西酞普兰的左旋异构体，对5-羟色胺再摄取抑制作用最强、选择性最高且起效较快，用于抑郁症、广泛性焦虑、惊恐、社交焦虑与强迫症；对CYP酶几乎无抑制、耐受性好、老年患者耐受尤佳，恶心、口干、出汗与头痛常见，剂量依赖性QTc延长风险低于消旋体，疗程结束后宜逐渐减量停药。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗焦虑", 3, 5), DrugAttribute("抗强迫", 2, 4), DrugAttribute("镇静", 0, 2), DrugAttribute("抗胆碱", 0, 1)),
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
        addictive = true, addictionRatePerHour = 0.006, toleranceGroup = "血清素能停药",
        description = "平衡型5-羟色胺与去甲肾上腺素再摄取抑制剂，用于抑郁症、广泛性焦虑障碍、糖尿病性周围神经痛、纤维肌痛与慢性骨骼肌疼痛；常见恶心、口干、便秘、出汗与血压轻度升高，肝功能不全与终末肾病禁用，为CYP1A2与2D6底物，与氟伏沙明、帕罗西汀联用浓度升高，禁与MAOI及曲马多联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("镇痛", 2, 4), DrugAttribute("兴奋", 0, 2), DrugAttribute("镇静", 0, 1)),
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
        description = "去甲肾上腺素-多巴胺再摄取抑制剂兼烟碱受体拮抗剂，用于抑郁症（迟滞、兴趣缺失为主）与戒烟辅助治疗，对性功能与体重影响小；激活作用明显，常见失眠、震颤与焦虑，高剂量或合并厌食、酗酒、脑外伤、停用苯二氮卓者癫痫发作风险显著升高，为CYP2D6强抑制剂，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("兴奋", 2, 4), DrugAttribute("促认知", 1, 3), DrugAttribute("镇静", 0, 1), DrugAttribute("欣快", 0, 2)),
        addictive = true, addictionRatePerHour = 0.01, toleranceGroup = "通用耐受",
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
        addictive = true, addictionRatePerHour = 0.004, toleranceGroup = "血清素能停药",
        description = "去甲肾上腺素能-特异性5-HT能抗抑郁药：阻断α2自身受体并拮抗5-HT2、5-HT3与H1受体，抗抑郁同时显著改善失眠、焦虑与食欲；镇静、体重增加明显而性功能障碍少，起效相对快、一至两周即可改善失眠，粒细胞缺乏罕见，与MAOI联用禁忌、由SSRI换药需冲洗期，老年人低血压跌倒风险须注意。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("镇静", 3, 5), DrugAttribute("催眠", 2, 4), DrugAttribute("抗胆碱", 0, 1)),
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
        description = "5-HT2A受体拮抗剂兼弱5-HT再摄取抑制剂，并阻断H1与α1受体，用于伴失眠的抑郁症、原发性失眠与焦虑状态；低剂量催眠、中剂量抗抑郁，常见直立性低血压、头晕与晨起困倦，阴茎异常勃起为罕见急症，可轻度延长QTc，经CYP3A4代谢，与其抑制剂联用血药浓度升高，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 2, 4), DrugAttribute("催眠", 3, 5), DrugAttribute("镇静", 2, 4), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("直立性低血压", -1, 3)),
        addictive = true, addictionRatePerHour = 0.004, toleranceGroup = "通用耐受",
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
        description = "多模式5-HT调节剂：抑制5-HT再摄取、激动5-HT1A并拮抗5-HT3、1D与7受体，用于成人抑郁症，起效需四周，对认知症状改善有循证支持；恶心、呕吐常见、餐后服用可减轻，性功能障碍与体重增加较轻，头晕与嗜睡偏少，经CYP2D6代谢、帕罗西汀显著升高其浓度，与单胺氧化酶抑制剂联用禁忌。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("促认知", 1, 3), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("镇静", 0, 2), DrugAttribute("抗强迫", 0, 2)),
        addictive = true, addictionRatePerHour = 0.004, toleranceGroup = "通用耐受",
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
        description = "谷氨酸能调节剂，增强AMPA受体介导的突触可塑性与神经营养因子表达，用于抑郁症与焦虑状态；起效较快、对认知与性功能影响小；治疗剂量安全性好，但大剂量具μ阿片受体激动作用，超剂量滥用可致依赖、戒断与呼吸抑制，欧美已有致死报告，与MAOI联用禁忌，肝肾功能不全者减量。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("促认知", 0, 2), DrugAttribute("镇静", 0, 1), DrugAttribute("依赖性", -1, 3)),
        addictive = true, addictionRatePerHour = 0.02, toleranceGroup = "血清素能停药",
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
        description = "选择性去甲肾上腺素再摄取抑制剂，用于抑郁症（迟滞、动力缺失为主）及部分焦虑障碍；激活作用明显，常见失眠、出汗、便秘、尿潴留与心动过速，可致轻度直立性低血压，对5-HT系统作用弱故性功能障碍与恶心较少，与MAOI联用禁忌，肝功能不全及老年人需减量，突然停药可致停药症状。",
        attributes = listOf(DrugAttribute("抗抑郁", 2, 4), DrugAttribute("兴奋", 1, 3), DrugAttribute("镇静", 0, 1), DrugAttribute("抗焦虑", 0, 2), DrugAttribute("直立性低血压", -1, 2)),
        addictive = true, addictionRatePerHour = 0.004, toleranceGroup = "通用耐受",
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
        description = "平衡型5-羟色胺与去甲肾上腺素再摄取抑制剂，用于抑郁症、纤维肌痛与慢性疼痛，对强迫症疗效有限；去甲肾上腺素效应可致尿频、尿急、出汗与血压升高，恶心、口干、头痛常见，与食物同服减轻胃肠道反应，对CYP酶几乎无抑制、药物相互作用少，肝肾功能不全需减量，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("镇痛", 1, 4), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("兴奋", 0, 2)),
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "通用耐受",
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
        description = "三环类抗抑郁药，抑制5-HT与去甲肾上腺素再摄取并阻断5-HT2、α1、H1与M受体；用于抑郁症、神经病理性疼痛、偏头痛预防与慢性紧张性头痛，低剂量用于失眠；抗胆碱副作用、镇静、体重增加与直立性低血压明显，可致传导阻滞与癫痫诱发，过量致心脏毒性致死风险高，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("镇痛", 2, 4), DrugAttribute("镇静", 3, 5), DrugAttribute("催眠", 2, 4), DrugAttribute("抗胆碱", 3, 5), DrugAttribute("直立性低血压", -1, 4)),
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "血清素能停药",
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
        description = "叔胺型三环类抗抑郁药，抑制5-HT与去甲肾上腺素再摄取，用于内源性抑郁症、惊恐障碍、强迫症与儿童夜间遗尿；镇静、抗胆碱与直立性低血压明显，可致传导紊乱与癫痫诱发，儿童遗尿治疗时须警惕心脏毒性，过量致死风险高，与SSRI联用需谨慎，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("抗强迫", 2, 4), DrugAttribute("镇静", 2, 4), DrugAttribute("抗胆碱", 2, 4), DrugAttribute("直立性低血压", -1, 3)),
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "血清素能停药",
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
        description = "三环类中5-HT再摄取抑制作用最强者，强迫症的一线三环药物，亦用于抑郁症、惊恐障碍与发作性睡病猝倒；抗强迫起效慢、疗程常需八周以上，抗胆碱、镇静、直立性低血压与癫痫诱发风险明显，与SSRI联用可致5-HT综合征，过量致心律失常、抽搐乃至致死，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗强迫", 4, 5), DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("镇静", 2, 4), DrugAttribute("抗胆碱", 2, 4)),
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "血清素能停药",
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
        description = "三环类抗抑郁药，抑制5-HT与去甲肾上腺素再摄取并强效阻断H1受体；用于抑郁症与焦虑状态，小剂量改善失眠、外用制剂止痒；镇静与口干明显，抗胆碱副作用随剂量升高，老年人易发生跌倒、谵妄，可轻度延长QTc，闭角型青光眼与尿潴留者慎用，过量致心脏毒性，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 2, 4), DrugAttribute("催眠", 3, 5), DrugAttribute("镇静", 3, 5), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("抗胆碱", 2, 4)),
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "血清素能停药",
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
        description = "阿米替林的去甲基活性代谢物，属仲胺型三环类，以抑制去甲肾上腺素再摄取为主，治疗血药浓度窗口明确，用于抑郁症、神经病理性疼痛与偏头痛预防；抗胆碱与直立性低血压轻于阿米替林，仍可致传导紊乱、心电图改变与癫痫诱发，过量致死风险高，有条件应行血药浓度监测，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("镇痛", 2, 4), DrugAttribute("镇静", 2, 4), DrugAttribute("抗胆碱", 1, 3), DrugAttribute("直立性低血压", -1, 3)),
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "血清素能停药",
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
        description = "四环类抗抑郁药，选择性抑制去甲肾上腺素再摄取，用于抑郁症（迟滞、激越型）及焦虑性抑郁；镇静、抗胆碱与体重增加明显，皮疹与光敏反应常见，剂量超过每日150mg或与抗精神病药联用时癫痫发作风险显著升高，心脏传导抑制与过量致死风险高，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 2, 4), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("镇静", 2, 4), DrugAttribute("抗胆碱", 2, 4), DrugAttribute("直立性低血压", -1, 3)),
        addictive = true, addictionRatePerHour = 0.005, toleranceGroup = "血清素能停药",
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
        description = "四环类抗抑郁药，阻断α2自身受体并拮抗5-HT2、5-HT3与H1受体，用于抑郁症（伴失眠、激越）与焦虑障碍；镇静、体重增加与食欲增强明显，抗胆碱作用轻、心脏毒性低、过量相对安全；罕见粒细胞缺乏与再生障碍性贫血，老年患者须定期查血象并警惕直立性低血压跌倒，禁与MAOI联用。",
        attributes = listOf(DrugAttribute("抗抑郁", 2, 4), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("镇静", 2, 4), DrugAttribute("催眠", 1, 3), DrugAttribute("抗胆碱", 0, 1)),
        addictive = true, addictionRatePerHour = 0.004, toleranceGroup = "血清素能停药",
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
        description = "不可逆非选择性单胺氧化酶抑制剂，用于非典型抑郁、难治性抑郁、惊恐障碍与社交焦虑；与富含酪胺食物如奶酪、腌肉、葡萄酒、发酵豆制品联用可致高血压危象，与SSRI、曲马多、哌甲酯、伪麻黄碱联用可致5-HT综合征或血压危象，直立性低血压常见，换用其他抗抑郁药需两周冲洗期。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("抗焦虑", 3, 5), DrugAttribute("抗强迫", 1, 3), DrugAttribute("镇静", 1, 3), DrugAttribute("直立性低血压", -1, 4)),
        addictive = true, addictionRatePerHour = 0.003, toleranceGroup = "通用耐受",
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
        description = "不可逆非选择性单胺氧化酶抑制剂，化学结构含环丙胺环、代谢物具苯丙胺样作用，用于难治性抑郁症；起效相对快、激活作用明显，与富含酪胺食物联用致高血压危象，与SSRI、曲马多、拟交感胺类联用属禁忌，常见失眠、直立性低血压与体重增加，停药后酶活性恢复需两周。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("兴奋", 1, 3), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("镇静", 0, 1), DrugAttribute("直立性低血压", -1, 4)),
        addictive = true, addictionRatePerHour = 0.004, toleranceGroup = "通用耐受",
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
        description = "可逆性选择性单胺氧化酶A抑制剂，用于抑郁症与社交焦虑障碍；酪胺增压效应弱，常规剂量下饮食限制相对宽松，高血压危象风险低于不可逆单胺氧化酶抑制剂；常见头晕、恶心、失眠、口干与直立性低血压，与SSRI、哌替啶、曲马多联用禁忌，肝功能不全者需减量，抗抑郁起效仍需两至三周。",
        attributes = listOf(DrugAttribute("抗抑郁", 2, 4), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("兴奋", 0, 2), DrugAttribute("镇静", 0, 1), DrugAttribute("直立性低血压", -1, 2)),
        addictive = true, addictionRatePerHour = 0.003, toleranceGroup = "通用耐受",
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
        description = "不可逆选择性单胺氧化酶B抑制剂，常规剂量下减少多巴胺降解、增强并延长左旋多巴疗效，用于帕金森病尤其症状波动者的辅助治疗；剂量超过每日10mg选择性消失、MAO-A亦被抑制则须饮食限制；代谢物为苯丙胺与甲基苯丙胺，可致失眠与兴奋，与SSRI、哌替啶、曲马多、三环类联用属禁忌。",
        attributes = listOf(DrugAttribute("抗帕金森", 3, 5), DrugAttribute("抗抑郁", 0, 3), DrugAttribute("兴奋", 0, 2), DrugAttribute("镇静", 0, 1)),
        addictive = true, addictionRatePerHour = 0.002, toleranceGroup = "通用耐受",
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
        description = "吩噻嗪类典型抗精神病药，阻断D2、5-HT2A、α1、H1与M受体；用于精神分裂症阳性症状、躁狂急性期、严重行为紊乱、谵妄及顽固性呃逆、恶心呕吐；镇静、直立性低血压、抗胆碱与体重增加明显，可致光敏、皮肤色素沉着、迟发性运动障碍与恶性综合征，静脉给药可致组织损伤，粒细胞减少罕见。",
        attributes = listOf(DrugAttribute("抗精神病", 3, 5), DrugAttribute("镇静", 4, 5), DrugAttribute("镇吐", 2, 4), DrugAttribute("锥体外系反应", -1, 4), DrugAttribute("直立性低血压", -1, 4), DrugAttribute("抗胆碱", 1, 3)),
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
        description = "丁酰苯类高效价典型抗精神病药，强效阻断D2受体，用于精神分裂症、急性精神病性激越、躁狂、谵妄与抽动秽语综合征；锥体外系反应发生率高，类帕金森、静坐不能、急性肌张力障碍与迟发运动障碍均可见，可致QTc延长与恶性综合征，镇静与直立性低血压较轻，静脉给药须心电监测。",
        attributes = listOf(DrugAttribute("抗精神病", 4, 5), DrugAttribute("镇吐", 2, 4), DrugAttribute("锥体外系反应", -1, 5), DrugAttribute("镇静", 1, 3), DrugAttribute("直立性低血压", -1, 2)),
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
        description = "吩噻嗪类中效价抗精神病药，阻断D2受体并对5-HT再摄取有弱抑制，用于精神分裂症阳性症状、躁狂、恶心呕吐及焦虑性抑郁的短期辅助；镇静与直立性低血压中等，锥体外系反应低于氟哌啶醇但仍常见，抗胆碱副作用随剂量升高，老年痴呆相关精神病患者脑卒中与死亡率升高，与锂盐联用增加神经毒性。",
        attributes = listOf(DrugAttribute("抗精神病", 3, 5), DrugAttribute("镇静", 2, 4), DrugAttribute("镇吐", 1, 3), DrugAttribute("锥体外系反应", -1, 4), DrugAttribute("直立性低血压", -1, 3)),
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
        description = "吩噻嗪类高效价典型抗精神病药，强效阻断D2受体，癸酸酯长效制剂用于精神分裂症维持治疗、防复发与依从性差患者；锥体外系反应突出，静坐不能、类帕金森综合征与迟发运动障碍常见，镇静与抗胆碱较轻，可致直立性低血压、QTc延长与恶性综合征，老年人剂量须大幅下调，长期用药须监测运动障碍。",
        attributes = listOf(DrugAttribute("抗精神病", 4, 5), DrugAttribute("锥体外系反应", -1, 5), DrugAttribute("镇静", 1, 3), DrugAttribute("直立性低血压", -1, 3)),
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
        description = "吩噻嗪类低效价抗精神病药，用于难治性精神分裂症与严重行为紊乱；剂量依赖性QTc显著延长、尖端扭转型室性心动过速风险高，视网膜色素变性、光敏反应与射精障碍常见，抗胆碱与镇静作用强而锥体外系反应轻，因心脏毒性被限制使用，须心电监测且剂量上限明确，禁与其他延长QT间期药物联用。",
        attributes = listOf(DrugAttribute("抗精神病", 2, 4), DrugAttribute("镇静", 4, 5), DrugAttribute("抗胆碱", 3, 5), DrugAttribute("锥体外系反应", -1, 2), DrugAttribute("直立性低血压", -1, 4)),
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
        description = "吩噻嗪类高效价典型抗精神病药，强效阻断D2受体，用于精神分裂症阳性症状与焦虑状态的短期辅助；锥体外系反应常见，类帕金森综合征、静坐不能与迟发运动障碍均可出现，可致直立性低血压、QTc延长、抗胆碱症状与恶性综合征，镇静中等，老年患者脑卒中与肺炎风险升高，须低剂量起始。",
        attributes = listOf(DrugAttribute("抗精神病", 3, 5), DrugAttribute("锥体外系反应", -1, 5), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("镇静", 1, 3), DrugAttribute("直立性低血压", -1, 3)),
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
        description = "苯甲酰胺类非典型抗抑郁与抗精神病药，高选择性阻断D2与D3受体；低剂量解除突触前自身受体抑制、增强多巴胺传导，改善退缩、动力缺失与抑郁样状态，高剂量阻断突触后受体控制幻觉妄想等阳性症状，亦用于神经性厌食；高泌乳素血症最常见，闭经、性功能障碍，锥体外系反应呈剂量相关，可致QTc延长。",
        attributes = listOf(DrugAttribute("抗精神病", 0, 4), DrugAttribute("抗抑郁", 1, 3), DrugAttribute("兴奋", 0, 2), DrugAttribute("镇静", 0, 2), DrugAttribute("锥体外系反应", -1, 3), DrugAttribute("镇吐", 1, 3)),
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
        description = "非典型抗精神病药金标准：广谱阻断D4、5-HT2A、H1、M1与α受体并调节谷氨酸能传导，用于难治性精神分裂症、分裂情感障碍与精神分裂症自杀风险预防；几乎无锥体外系反应，但粒细胞缺乏须每周血象监测，心肌炎、剂量相关癫痫、代谢综合征、便秘、镇静与直立性低血压突出，与苯二氮卓类联用呼吸抑制加重。",
        attributes = listOf(DrugAttribute("抗精神病", 4, 5), DrugAttribute("镇静", 4, 5), DrugAttribute("催眠", 1, 4), DrugAttribute("抗胆碱", 3, 5), DrugAttribute("直立性低血压", -1, 4), DrugAttribute("锥体外系反应", -1, 1)),
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
        description = "苯并异噁唑类非典型抗精神病药，阻断D2、5-HT2A、α1与H1受体，用于精神分裂症、分裂情感障碍、双相I型躁狂与混合发作及儿童孤独症伴易激惹；剂量超过每日6mg时锥体外系反应接近典型药，高泌乳素血症突出，可致闭经、男性乳腺发育与性功能障碍，体重增加、直立性低血压与QTc轻中度延长常见。",
        attributes = listOf(DrugAttribute("抗精神病", 3, 5), DrugAttribute("心境稳定", 2, 4), DrugAttribute("镇静", 2, 4), DrugAttribute("锥体外系反应", -1, 4), DrugAttribute("直立性低血压", -1, 3)),
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
        description = "噻吩并苯二氮卓类非典型抗精神病药，广谱阻断D1至D4、5-HT2A/2C/3、M1、H1与α1受体，用于精神分裂症、双相障碍躁狂与抑郁发作及难治性抑郁症的辅助治疗；镇静、代谢综合征与抗胆碱副作用明显，锥体外系反应轻、泌乳素影响小，直立性低血压与头晕常见，长期用药须监测体重、血糖与血脂。",
        attributes = listOf(DrugAttribute("抗精神病", 4, 5), DrugAttribute("心境稳定", 3, 5), DrugAttribute("镇静", 3, 5), DrugAttribute("催眠", 1, 4), DrugAttribute("抗胆碱", 1, 3), DrugAttribute("锥体外系反应", -1, 2)),
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
        description = "二苯硫氮卓类非典型抗精神病药，对D2与5-HT2A受体亲和力低，对H1、α1、α2受体及去甲肾上腺素转运体亲和力高，用于精神分裂症、双相障碍各相、抑郁症辅助治疗与低剂量原发性失眠；镇静、晨起困倦与直立性低血压常见，代谢综合征与白内障风险存在，锥体外系反应与泌乳素升高极少，骤停需逐步减量。",
        attributes = listOf(DrugAttribute("抗精神病", 2, 5), DrugAttribute("心境稳定", 2, 4), DrugAttribute("镇静", 4, 5), DrugAttribute("催眠", 3, 5), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("直立性低血压", -1, 3)),
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
        description = "苯并异噻唑类非典型抗精神病药，阻断D2与5-HT2A受体、部分激动5-HT1A并抑制单胺再摄取，用于精神分裂症与双相I型躁狂；对体重、血糖与血脂影响极小，锥体外系反应与高泌乳素血症风险低，QTc延长明显、须心电监测，与延长QT药物禁忌联用，常见嗜睡、静坐不能与恶心，须与食物同服吸收方佳。",
        attributes = listOf(DrugAttribute("抗精神病", 3, 5), DrugAttribute("心境稳定", 1, 4), DrugAttribute("镇静", 1, 3), DrugAttribute("兴奋", 0, 2), DrugAttribute("锥体外系反应", -1, 2)),
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
        description = "利培酮的活性代谢物9-羟利培酮，阻断D2、5-HT2A、α1与H1受体，渗透泵缓释制剂血药浓度平稳、峰谷波动小，用于精神分裂症与分裂情感障碍；高泌乳素血症呈剂量相关且常见，锥体外系反应随剂量升高，体重中度增加，可致直立性低血压、QTc轻中度延长与嗜睡，重度肾功能不全者须减量。",
        attributes = listOf(DrugAttribute("抗精神病", 3, 5), DrugAttribute("心境稳定", 1, 3), DrugAttribute("镇静", 1, 3), DrugAttribute("锥体外系反应", -1, 4), DrugAttribute("直立性低血压", -1, 3)),
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
        description = "吡咯并吡啶类非典型抗精神病药，舌下含服吸收，阻断D2与5-HT2A受体、部分激动5-HT1A并阻断H1与α受体，用于精神分裂症与双相I型躁狂及混合发作；锥体外系反应与泌乳素升高较同类少，常见舌下麻木、口腔溃疡、嗜睡与体重增加，吞服无效、必须舌下含服，经CYP1A2代谢、与帕罗西汀联用浓度升高。",
        attributes = listOf(DrugAttribute("抗精神病", 3, 5), DrugAttribute("心境稳定", 2, 4), DrugAttribute("镇静", 2, 4), DrugAttribute("催眠", 1, 3), DrugAttribute("锥体外系反应", -1, 2), DrugAttribute("直立性低血压", -1, 2)),
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
        description = "苯并噻唑类非典型抗精神病药，阻断D2、5-HT2A与5-HT7受体并部分激动5-HT1A，用于精神分裂症与双相I型抑郁发作；对体重、血糖、血脂与泌乳素影响小，常见嗜睡、静坐不能与恶心，必须与至少350千卡食物同服、否则吸收大幅下降，CYP3A4强抑制剂与诱导剂可显著改变其血药浓度。",
        attributes = listOf(DrugAttribute("抗精神病", 3, 5), DrugAttribute("心境稳定", 3, 5), DrugAttribute("镇静", 1, 3), DrugAttribute("锥体外系反应", -1, 2), DrugAttribute("直立性低血压", -1, 3)),
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
        description = "苯甲酰胺类非典型抗精神病药，高选择性阻断边缘系统D2与D3受体；低剂量解除突触前自身抑制、增强多巴胺传导而改善阴性症状与心境恶劣，高剂量阻断突触后受体以控制幻觉妄想，亦用于抽动障碍与心境恶劣；高泌乳素血症在同类中最突出，闭经、性功能障碍常见，锥体外系反应呈剂量相关，QTc可延长、肾功能不全须减量。",
        attributes = listOf(DrugAttribute("抗精神病", 2, 5), DrugAttribute("抗抑郁", 1, 3), DrugAttribute("兴奋", 0, 2), DrugAttribute("镇静", 0, 2), DrugAttribute("锥体外系反应", -1, 3)),
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
        description = "多巴胺系统稳定剂：D2与5-HT1A受体部分激动、5-HT2A拮抗，用于精神分裂症、双相躁狂与维持治疗、抑郁症辅助治疗、抽动秽语综合征与孤独症易激惹；激活作用明显、嗜睡与体重增加轻、高泌乳素风险极低，静坐不能与失眠常见，罕见冲动控制障碍如病理性赌博，经CYP2D6与3A4代谢、与帕罗西汀联用减量。",
        attributes = listOf(DrugAttribute("抗精神病", 3, 5), DrugAttribute("心境稳定", 2, 4), DrugAttribute("兴奋", 0, 2), DrugAttribute("镇静", 0, 2), DrugAttribute("锥体外系反应", -1, 3)),
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

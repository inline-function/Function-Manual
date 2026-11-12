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
        description = "L-色氨酸为人体必需氨基酸，经色氨酸羟化酶羟化生成5-羟色胺并可进一步转化为褪黑素，临床用于轻中度抑郁、慢性失眠与经前期综合征的辅助治疗；与MAOI或SSRI联用可诱发5-羟色胺综合征，宜与碳水化合物同服以减少蛋白竞争血脑屏障转运，妊娠及肝肾功能不全者慎用。",
        attributes = listOf(DrugAttribute("抗抑郁", 1, 3), DrugAttribute("抗焦虑", 1, 2), DrugAttribute("催眠", 1, 3), DrugAttribute("促认知", 0, 1)),
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
        description = "贯叶连翘提取物抑制5-HT、去甲肾上腺素与多巴胺再摄取并轻度抑制单胺氧化酶，用于轻中度抑郁症；其为CYP3A4与P-糖蛋白强诱导剂，可显著降低口服避孕药、华法林、环孢素与抗肿瘤药血药浓度导致治疗失败，与SSRI联用可致5-羟色胺综合征，光敏性皮炎与胃肠道反应为其常见不良反应。",
        attributes = listOf(DrugAttribute("抗抑郁", 2, 4), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("镇静", 0, 2), DrugAttribute("依赖性", -1, 1)),
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
        description = "缬草提取物经濒草素与异戊酸作用于GABA-A受体并抑制GABA降解酶，产生非苯二氮卓类镇静催眠作用，用于焦虑相关失眠与躯体紧张；与乙醇、苯二氮卓类或其他中枢抑制剂联用可加重镇静与共济失调，长期大剂量骤停偶见反跳性兴奋与心悸，气味刺鼻且疗效在数周内渐进显现。",
        attributes = listOf(DrugAttribute("镇静", 2, 4), DrugAttribute("催眠", 2, 4), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("依赖性", -1, 1)),
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
        description = "卡瓦内酯通过增强GABA-A受体氯离子通道开放并阻断电压依赖性钠、钙通道，产生抗焦虑、镇静与局部麻醉作用，用于广泛性焦虑与应激紧张状态；可致肝损伤甚至需肝移植的暴发性肝衰竭，与乙醇或苯二氮卓联用加重中枢抑制，长期使用致卡瓦样皮肤病与交叉耐受。",
        attributes = listOf(DrugAttribute("抗焦虑", 2, 4), DrugAttribute("镇静", 1, 3), DrugAttribute("催眠", 1, 3), DrugAttribute("肝毒性", -1, 3)),
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
        description = "银杏叶提取物经拮抗血小板活化因子、清除自由基与改善微循环提升脑灌注，用于认知功能减退、耳鸣及血管性眩晕的辅助治疗；具有抗血小板聚集作用，与华法林、阿司匹林或非甾体抗炎药联用可增加出血风险，未控制的癫痫与凝血障碍患者慎用。",
        attributes = listOf(DrugAttribute("促认知", 1, 3), DrugAttribute("抗焦虑", 1, 2), DrugAttribute("镇静", 0, 1), DrugAttribute("出血倾向", -1, 2)),
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
        description = "人参皂苷通过上调下丘脑-垂体-肾上腺轴功能与增强多巴胺能神经传递产生抗疲劳与温和中枢兴奋作用，用于虚弱状态、躯体疲劳与注意力减退的辅助调理；可升高血压并诱发失眠与燥热，与华法林联用减弱抗凝效果，躁狂状态及未控制高血压患者禁用。",
        attributes = listOf(DrugAttribute("兴奋", 1, 3), DrugAttribute("促认知", 1, 3), DrugAttribute("欣快", 0, 2), DrugAttribute("依赖性", -1, 1)),
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
        description = "鱼油富含二十碳五烯酸与二十二碳六烯酸，经抑制COX-2炎症通路、调节细胞膜流动性与第二信使信号发挥抗炎与膜稳定作用，用于高甘油三酯血症及抑郁障碍辅助治疗；大剂量可增加出血倾向并升高低密度脂蛋白，与抗凝药联用需监测出血与凝血指标。",
        attributes = listOf(DrugAttribute("降脂", 2, 4), DrugAttribute("促认知", 1, 2), DrugAttribute("抗炎", 1, 3), DrugAttribute("心境稳定", 0, 2)),
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
        description = "肌醇为磷脂酰肌醇信号通路前体，调节5-HT2C与多巴胺受体介导的第二信使转换，用于惊恐障碍、强迫症与经前期综合征的辅助治疗；对多囊卵巢综合征患者兼具改善胰岛素抵抗的代谢获益，耐受性良好，与SSRI联用可增强抗强迫与抗惊恐效应，大剂量可致轻度胃肠胀气。",
        attributes = listOf(DrugAttribute("抗焦虑", 1, 3), DrugAttribute("抗强迫", 1, 3), DrugAttribute("抗抑郁", 0, 2)),
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
        description = "L-茶氨酸竞争性结合谷氨酸受体并促进γ脑电波与GABA生成，产生保持清醒的松弛与抗焦虑作用，常与咖啡因联用改善注意与认知表现；安全性良好、不影响精神运动操作，与中枢镇静剂联用可协同镇定，过量亦未见明显毒性，常作为机能性食品成分用于缓解应激与改善睡眠质量。",
        attributes = listOf(DrugAttribute("抗焦虑", 1, 3), DrugAttribute("镇静", 1, 2), DrugAttribute("促认知", 1, 2), DrugAttribute("催眠", 0, 2)),
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
        addictive = true, addictionRatePerHour = 0.06, toleranceGroup = "兴奋剂耐受",
        description = "尼古丁为烟碱型乙酰胆碱受体激动剂，激动α4β2受体促进伏隔核多巴胺释放，产生兴奋、欣快与注意力增强，用于烟草依赖替代治疗；可致心率加快与血压升高，妊娠期使用影响胎盘与胎儿发育，与多巴胺受体阻断剂联用减弱其兴奋效应，戒断可致易怒与渴求。",
        attributes = listOf(DrugAttribute("兴奋", 2, 4), DrugAttribute("欣快", 1, 3), DrugAttribute("促认知", 1, 2), DrugAttribute("依赖性", -1, 5)),
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
        addictive = true, addictionRatePerHour = 0.08, toleranceGroup = "兴奋剂耐受",
        description = "乙醇通过增强GABA-A受体、抑制NMDA受体并增加中脑边缘多巴胺释放，产生剂量依赖性镇静、抗焦虑与欣快，极大剂量致昏迷与呼吸抑制；与苯二氮卓类、巴比妥类或阿片类联用可发生致命性呼吸抑制，与双硫仑或某些头孢类联用致乙醛蓄积反应，慢性使用致多脏器损害。",
        attributes = listOf(DrugAttribute("镇静", 2, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("欣快", 1, 4), DrugAttribute("呼吸抑制", -1, 4), DrugAttribute("依赖性", -1, 5)),
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
        addictive = true, addictionRatePerHour = 0.03, toleranceGroup = "兴奋剂耐受",
        description = "槟榔碱为毒蕈碱型乙酰胆碱受体部分激动剂，激动M1受体产生中枢温和兴奋、唾液与消化液分泌增加及胃肠蠕动增强，民间用于提神与驱肠虫；长期咀嚼槟榔可致口腔黏膜下纤维性变与口腔鳞癌，与抗胆碱药相互拮抗，精神依赖与成瘾性较强，过量可致流涎、震颤与心动过缓等胆碱能毒性。",
        attributes = listOf(DrugAttribute("兴奋", 1, 3), DrugAttribute("欣快", 0, 2), DrugAttribute("促认知", 0, 2), DrugAttribute("依赖性", -1, 3)),
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
        addictive = true, addictionRatePerHour = 0.01, toleranceGroup = "加巴喷丁类耐受",
        description = "普瑞巴林为电压门控钙通道α2δ亚基配体，抑制谷氨酸、去甲肾上腺素与P物质释放，用于糖尿病性周围神经痛、带状疱疹后神经痛、纤维肌痛及广泛性焦虑障碍；以原型经肾排泄故肾功能不全须减量，与中枢抑制剂联用加重镇静，超适应证滥用可致欣快与依赖。",
        attributes = listOf(DrugAttribute("抗焦虑", 3, 5), DrugAttribute("抗癫痫", 2, 5), DrugAttribute("镇痛", 2, 5), DrugAttribute("镇静", 1, 3), DrugAttribute("欣快", 0, 3)),
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
        addictive = true, addictionRatePerHour = 0.008, toleranceGroup = "加巴喷丁类耐受",
        description = "加巴喷丁结合电压门控钙通道α2δ亚基减少兴奋性递质释放，用于部分性癫痫发作、带状疱疹后神经痛与不安腿综合征；生物利用度随剂量升高非线性下降，需逐步加量与撤药，与含铝镁抗酸药同服显著降低吸收，常见头晕、嗜睡与外周水肿，滥用与误用风险近年受到关注。",
        attributes = listOf(DrugAttribute("抗癫痫", 2, 4), DrugAttribute("镇痛", 1, 4), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("镇静", 1, 3)),
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
        description = "金刚烷胺为非竞争性NMDA受体拮抗剂并促进纹状体多巴胺释放，用于帕金森病症状波动、抗精神病药所致锥体外系反应与A型流感预防和治疗；可致失眠、幻觉、口干与直立性低血压，老年痴呆患者可加重谵妄，骤停可致高热与强直等恶性撤药综合征。",
        attributes = listOf(DrugAttribute("抗帕金森", 2, 4), DrugAttribute("兴奋", 1, 3), DrugAttribute("抗胆碱", 1, 2), DrugAttribute("促认知", 0, 2), DrugAttribute("直立性低血压", -1, 2)),
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
        description = "美金刚为低亲和力、非竞争性NMDA受体拮抗剂，减少谷氨酸兴奋性毒性对神经元的损伤，用于中重度阿尔茨海默病认知障碍；主要以原型经肾排泄，肾功能不全与老年患者需减量，与右美沙芬或氯胺酮联用加重神经精神不良反应，常见头晕、便秘与轻度镇静。",
        attributes = listOf(DrugAttribute("促认知", 2, 4), DrugAttribute("镇静", 0, 2), DrugAttribute("抗焦虑", 0, 1)),
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
        description = "地佐环平为高亲和力非竞争性NMDA受体拮抗剂，阻断谷氨酸兴奋性传递产生解离状态与强效镇痛，仅作为工具药用于实验研究；动物模型中显示抗癫痫与神经保护活性，但在人类可致解离性幻觉、运动障碍与神经毒性，无临床治疗价值亦无监管批准。",
        attributes = listOf(DrugAttribute("致幻", 3, 5), DrugAttribute("镇痛", 0, 3), DrugAttribute("抗癫痫", 0, 3), DrugAttribute("兴奋", 1, 3)),
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
        description = "苯海拉明为H1组胺受体拮抗剂并具中枢性抗胆碱作用，用于过敏性皮肤病、短期失眠诱导与晕动症防治；可透过血脑屏障致嗜睡与次日残余镇静，与乙醇或镇静催眠药联用加重中枢抑制，老年人易致谵妄、尿潴留与跌倒，作为催眠药长期使用可致反跳性失眠。",
        attributes = listOf(DrugAttribute("镇静", 2, 4), DrugAttribute("催眠", 1, 3), DrugAttribute("抗胆碱", 1, 3), DrugAttribute("镇咳", 1, 2), DrugAttribute("直立性低血压", -1, 2)),
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
        description = "茶苯海明为苯海拉明与8-氯茶碱的配伍盐，拮抗H1与毒蕈碱受体并抑制前庭信号传入，用于晕动病、术后及妊娠相关恶心呕吐；可致嗜睡、口干与视物模糊，驾驶员与机械操作者慎用，与抗胆碱药联用加重不良反应，过量可致抗胆碱综合征样表现，六岁以下儿童使用需权衡收益与风险。",
        attributes = listOf(DrugAttribute("防晕动", 3, 5), DrugAttribute("镇静", 2, 4), DrugAttribute("催眠", 1, 3), DrugAttribute("抗胆碱", 1, 2)),
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
        description = "多西拉敏为H1组胺受体拮抗剂，具有较强的中枢镇静与轻中度外周抗胆碱作用，用于妊娠相关恶心呕吐及短程失眠治疗；与维生素B6配伍为妊娠剧吐的一线治疗，与乙醇或其他中枢抑制剂联用明显加重镇静，闭角型青光眼与前列腺增生患者禁用。",
        attributes = listOf(DrugAttribute("催眠", 3, 5), DrugAttribute("镇静", 2, 4), DrugAttribute("抗胆碱", 1, 3), DrugAttribute("止吐", 1, 3)),
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
        description = "东莨菪碱为非选择性毒蕈碱型乙酰胆碱受体拮抗剂，抑制前庭系统与延髓催吐化学感受区，用于晕动病预防与术后恶心呕吐防治；易透过血脑屏障致嗜睡、记忆损害与注意力下降，过量可致抗胆碱谵妄伴生动幻觉，青光眼、前列腺肥大与肠梗阻患者禁用。",
        attributes = listOf(DrugAttribute("抗胆碱", 4, 5), DrugAttribute("防晕动", 3, 5), DrugAttribute("镇静", 2, 4), DrugAttribute("致幻", 2, 4)),
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
        addictive = true, addictionRatePerHour = 0.05, toleranceGroup = "兴奋剂耐受",
        description = "甲基苯丙胺强烈促进中枢多巴胺、去甲肾上腺素与5-HT释放并抑制再摄取，产生强效觉醒、欣快、自信心增强与食欲抑制，仅限难治性嗜睡症短期使用；高度成瘾且可致偏执性精神病、冠状动脉痉挛与高血压危象，与MAOI联用可致死性高热与高血压危象，长期使用致纹状体多巴胺能神经末梢损伤。",
        attributes = listOf(DrugAttribute("兴奋", 4, 5), DrugAttribute("欣快", 4, 5), DrugAttribute("促认知", 1, 3), DrugAttribute("依赖性", -1, 5)),
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
        addictive = true, addictionRatePerHour = 0.04, toleranceGroup = "兴奋剂耐受",
        description = "苯丙胺促进囊泡内多巴胺与去甲肾上腺素释放并抑制再摄取，用于注意缺陷多动障碍与发作性睡病，可显著改善注意力、觉醒与冲动控制；具较高滥用潜力，可致血压升高、厌食与生长迟缓，与SSRI联用增加5-羟色胺综合征风险，结构性心脏病患者禁用。",
        attributes = listOf(DrugAttribute("兴奋", 4, 5), DrugAttribute("欣快", 3, 5), DrugAttribute("促认知", 2, 4), DrugAttribute("抗抑郁", 1, 2), DrugAttribute("依赖性", -1, 5)),
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
        addictive = true, addictionRatePerHour = 0.03, toleranceGroup = "兴奋剂耐受",
        description = "MDMA促使多巴胺、去甲肾上腺素与5-HT大量释放并阻断再摄取，产生强烈共情、欣快与感知增强，主要用于创伤后应激障碍的辅助治疗研究；可致高热综合征、低钠血症、肝损伤与5-羟色胺综合征，具5-HT神经毒性并损害记忆，与MAOI联用致死风险极高，舞蹈场景常因持续运动与脱水加重高热与横纹肌溶解。",
        attributes = listOf(DrugAttribute("欣快", 4, 5), DrugAttribute("兴奋", 2, 4), DrugAttribute("致幻", 1, 3), DrugAttribute("依赖性", -1, 3)),
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
        addictive = true, addictionRatePerHour = 0.1, toleranceGroup = "兴奋剂耐受",
        description = "可卡因阻断多巴胺、去甲肾上腺素与5-HT再摄取并抑制电压门控钠通道，产生强烈欣快与可靠的体表麻醉，临床仅用于鼻、咽部表面麻醉；成瘾性极高且作用短暂，可致冠状动脉痉挛、心律失常、高血压危象与卒中，与乙醇联用生成毒性更强的古柯乙烯。",
        attributes = listOf(DrugAttribute("兴奋", 4, 5), DrugAttribute("欣快", 4, 5), DrugAttribute("镇痛", 1, 3), DrugAttribute("致幻", 1, 3), DrugAttribute("依赖性", -1, 5)),
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
        description = "甲卡西酮为合成卡西酮衍生物，强烈促进多巴胺与5-HT释放并抑制再摄取，产生与甲基苯丙胺和可卡因相似的中枢兴奋与欣快；无任何临床适应证，可致心动过速、血压升高、磨牙与高热综合征，精神依赖性极强，过量可致致死性心律失常与脑血管意外。",
        attributes = listOf(DrugAttribute("兴奋", 4, 5), DrugAttribute("欣快", 4, 5), DrugAttribute("致幻", 0, 2), DrugAttribute("促认知", 1, 2), DrugAttribute("依赖性", -1, 5)),
        addictive = true, addictionRatePerHour = 0.08, toleranceGroup = "兴奋剂耐受",
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
        description = "恰特草嫩叶含天然卡西酮类生物碱，促进多巴胺与去甲肾上腺素释放产生温和中枢兴奋、欣快与社交欲增强，在东非与阿拉伯半岛长期作为传统嗜好品咀嚼；可致血压升高、失眠与厌食，长期使用与心血管病及精神分裂样症状相关，现已列入多国管制清单。",
        attributes = listOf(DrugAttribute("兴奋", 2, 4), DrugAttribute("欣快", 1, 3), DrugAttribute("促认知", 0, 2), DrugAttribute("依赖性", -1, 3)),
        addictive = true, addictionRatePerHour = 0.04, toleranceGroup = "兴奋剂耐受",
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
        addictive = true, addictionRatePerHour = 0.03, toleranceGroup = "阿片耐受",
        description = "海洛因为二乙酰吗啡，激动μ-阿片受体产生强大镇痛、欣快与镇静，无合法临床适应证；注射或吸入迅速产生耐受与严重生理依赖，过量致致死性呼吸抑制与肺水肿，与苯二氮卓类或乙醇联用呼吸骤停风险骤增，过量抢救需大剂量纳洛酮并继以戒断维持治疗。",
        attributes = listOf(DrugAttribute("镇痛", 4, 5), DrugAttribute("欣快", 4, 5), DrugAttribute("镇静", 2, 5), DrugAttribute("呼吸抑制", -1, 5), DrugAttribute("依赖性", -1, 5)),
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
        description = "洛哌丁胺为外周μ-阿片受体激动剂，抑制肠神经系统乙酰胆碱与P物质释放从而减弱肠蠕动、增加肛门括约肌张力，用于急慢性非感染性腹泻；因不易透过血脑屏障故欣快与呼吸抑制风险极低，超大剂量或与CYP3A4抑制剂联用可致QT延长与心律失常，感染性腹泻伴高热便血者禁用以免延缓病原清除。",
        attributes = listOf(DrugAttribute("止泻", 3, 5), DrugAttribute("镇痛", 0, 1), DrugAttribute("依赖性", -1, 1), DrugAttribute("呼吸抑制", -1, 1)),
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
        description = "大麻所含Δ9-四氢大麻酚激动中枢CB1受体产生欣快、时间与空间感知改变及轻度镇静，用于化疗相关恶心呕吐、多发性硬化痉挛与难治性癫痫的辅助治疗；可致短期记忆与执行功能损害、体位性低血压与支气管刺激，具精神病素质者可诱发持久精神病性发作。",
        attributes = listOf(DrugAttribute("欣快", 2, 4), DrugAttribute("致幻", 1, 3), DrugAttribute("镇静", 1, 3), DrugAttribute("镇痛", 1, 3), DrugAttribute("催眠", 1, 3), DrugAttribute("依赖性", -1, 2)),
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
        description = "裸盖菇素口服后脱磷酸为裸盖菇素胺并激动5-HT2A受体，产生深刻的感知、情绪与自我意识体验，目前用于难治性抑郁症与终末期疾病焦虑的辅助治疗研究；毒性低、无生理依赖，但可致急性惊恐发作与一过性血压升高，与SSRI联用致反应减弱，须在监控下使用。",
        attributes = listOf(DrugAttribute("致幻", 4, 5), DrugAttribute("欣快", 1, 3), DrugAttribute("抗抑郁", 1, 3), DrugAttribute("抗焦虑", 0, 3)),
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
        description = "麦司卡林为苯乙胺类经典致幻剂，激动5-HT2A受体产生持续八至十二小时的视幻觉、联觉与意识状态改变，主要用于精神治疗与宗教仪式的人类学研究；生理毒性低且无生理依赖，常见恶心、呕吐、瞳孔扩大与心动过速，精神病史者可诱发持久精神病性反应。",
        attributes = listOf(DrugAttribute("致幻", 4, 5), DrugAttribute("欣快", 1, 3), DrugAttribute("兴奋", 1, 2)),
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
        description = "DMT为色胺类速效致幻剂，激动5-HT2A受体产生约三十分钟的强烈感知解构与自我消解体验，南美传统将其与β-咔啉类单胺氧化酶抑制剂配伍组成死藤水使用；单独使用作用短暂，静脉或吸入给药可致血压骤升与急性惊恐，过量可致5-羟色胺综合征样反应。",
        attributes = listOf(DrugAttribute("致幻", 4, 5), DrugAttribute("欣快", 1, 3), DrugAttribute("兴奋", 0, 2), DrugAttribute("镇静", 0, 2)),
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
        attributes = listOf(DrugAttribute("镇痛", 2, 4), DrugAttribute("镇静", 3, 5), DrugAttribute("催眠", 2, 4), DrugAttribute("致幻", 2, 4), DrugAttribute("欣快", 1, 3)),
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
        description = "苯环利定为芳基环己胺类NMDA受体拮抗剂，产生分离性麻醉、镇痛、兴奋与解离性幻觉，曾用作静脉麻醉药；极易透过血脑屏障，可致血压升高、痛觉缺失、暴力行为与类精神分裂样症状，生理与心理依赖均可发生，过量以呼吸与循环支持治疗为主，长期使用者可出现记忆缺陷与抑郁综合征。",
        attributes = listOf(DrugAttribute("致幻", 4, 5), DrugAttribute("镇痛", 3, 5), DrugAttribute("兴奋", 2, 4), DrugAttribute("依赖性", -1, 3)),
        addictive = true, addictionRatePerHour = 0.01, toleranceGroup = "解离剂耐受",
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
        addictive = true, addictionRatePerHour = 0.06, toleranceGroup = "GABA能耐受",
        description = "GHB为内源性短链脂肪酸神经递质，激动GABA-B受体与特异性受体并促生长激素释放，剂量依赖性产生镇静、催眠与欣快，用于发作性睡病猝倒与酒精戒断辅助治疗；与乙醇或苯二氮卓联用可致昏迷与呼吸抑制，生理依赖性强且戒断可致谵妄与癫痫，须递减撤药。",
        attributes = listOf(DrugAttribute("催眠", 3, 5), DrugAttribute("镇静", 2, 4), DrugAttribute("欣快", 2, 4), DrugAttribute("兴奋", 0, 2), DrugAttribute("呼吸抑制", -1, 3), DrugAttribute("依赖性", -1, 4)),
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
        attributes = listOf(DrugAttribute("降压", 3, 5), DrugAttribute("解氰中毒", 3, 5), DrugAttribute("欣快", 1, 3), DrugAttribute("直立性低血压", -1, 4)),
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
        addictive = true, addictionRatePerHour = 0.025, toleranceGroup = "GABA能耐受",
        description = "氟硝西泮为苯二氮卓类GABA-A受体α亚基正向变构调节剂，产生强效催眠、抗焦虑与显著顺行性遗忘，用于严重入睡困难与麻醉前给药；依赖性、停药反跳与戒断痉挛风险高，与乙醇联用可致意识丧失与呼吸抑制，因被滥用为迷奸药物而受到严格管制，半衰期长致次日宿醉与运动损害常见。",
        attributes = listOf(DrugAttribute("催眠", 4, 5), DrugAttribute("镇静", 3, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("抗癫痫", 1, 3), DrugAttribute("依赖性", -1, 5)),
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
        addictive = true, addictionRatePerHour = 0.03, toleranceGroup = "GABA能耐受",
        description = "三唑仑为超短效苯二氮卓类GABA-A受体激动剂，服药后十五至三十分钟内迅速起效并缩短入睡时间，用于入睡困难型失眠的短期治疗；骤停可致反跳性失眠、焦虑与感觉过敏，依赖性在苯二氮卓类中偏强，与伊曲康唑、酮康唑及利托那韦联用属禁忌，短效特性使其更易出现夜间行为遗忘。",
        attributes = listOf(DrugAttribute("催眠", 4, 5), DrugAttribute("镇静", 3, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("依赖性", -1, 5)),
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
        addictive = true, addictionRatePerHour = 0.03, toleranceGroup = "GABA能耐受",
        description = "咪达唑仑为短效苯二氮卓类GABA-A受体激动剂，起效快且有独特的水溶性，用于麻醉诱导、程序性镇静、癫痫持续状态与重症监护镇静；可致剂量依赖性呼吸抑制与低血压，与阿片类联用需备气道支持，老年与肝肾功能不全者须减量且纳洛酮不能拮抗其作用。",
        attributes = listOf(DrugAttribute("催眠", 4, 5), DrugAttribute("镇静", 3, 5), DrugAttribute("抗焦虑", 3, 5), DrugAttribute("抗癫痫", 2, 4), DrugAttribute("呼吸抑制", -1, 4)),
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
        addictive = true, addictionRatePerHour = 0.02, toleranceGroup = "GABA能耐受",
        description = "硝西泮为中长效苯二氮卓类GABA-A受体激动剂，延长睡眠周期并减轻夜间觉醒，用于睡眠维持障碍与婴幼儿痉挛症辅助治疗；可致次日残余镇静、共济失调与跌倒风险增加，老年人并易致意识模糊，长期用药依赖性渐进形成，停药必须缓慢递减，长期用药尚可致跌倒骨折与认知损害。",
        attributes = listOf(DrugAttribute("催眠", 3, 5), DrugAttribute("镇静", 2, 4), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("抗癫痫", 1, 3), DrugAttribute("依赖性", -1, 4)),
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
        addictive = true, addictionRatePerHour = 0.03, toleranceGroup = "GABA能耐受",
        description = "甲喹酮为喹唑酮类非巴比妥催眠药，作用于GABA-A受体并抑制脊髓反射产生镇静催眠与轻度欣快，曾用于严重失眠与神经性焦虑；治疗指数窄、过量致死率高，可致昏迷、呼吸抑制与心力衰竭，现已退出正规临床并于多国列为管制物资而仅在非法渠道出现。",
        attributes = listOf(DrugAttribute("催眠", 4, 5), DrugAttribute("镇静", 3, 5), DrugAttribute("抗焦虑", 2, 4), DrugAttribute("欣快", 1, 3), DrugAttribute("呼吸抑制", -1, 4), DrugAttribute("依赖性", -1, 5)),
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
        description = "吡拉西坦为环状GABA衍生物，改善神经元膜流动性、线粒体ATP合成并降低红细胞聚集，用于认知减退、脑缺氧后遗症与眩晕的辅助治疗；促认知作用温和，可有轻度抗抑郁与抗焦虑效应而镇静作用弱，与抗凝药联用可延长出血时间，肾功能不全需减量，常见不良反应为轻度失眠与体重增加。",
        attributes = listOf(DrugAttribute("促认知", 2, 4), DrugAttribute("抗焦虑", 0, 2), DrugAttribute("抗抑郁", 0, 2), DrugAttribute("兴奋", 0, 1)),
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
        description = "阿尼西坦为脂溶性拉西坦类促认知药，上调AMPA受体功能并促进乙酰胆碱与多巴胺释放，用于老年认知减退与情绪低落的辅助治疗；动物实验显示一定抗焦虑与抗抑郁样作用，临床证据有限，代谢迅速需每日二至三次给药，常见头痛、不安与失眠，与抗凝药联用可增强其延长出血时间效应。",
        attributes = listOf(DrugAttribute("促认知", 2, 4), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("抗抑郁", 1, 3)),
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
        description = "奥拉西坦为环状GABA类似物，促进脑内ATP合成、增强磷酸戊糖通路并提高乙酰胆碱利用，用于血管性痴呆、脑外伤后认知障碍与发育迟缓辅助治疗；以促认知与温和精神激活作用为主，无依赖性与明显镇静，偶见焦虑、失眠与皮疹，严重肾功能不全者慎用。",
        attributes = listOf(DrugAttribute("促认知", 2, 4), DrugAttribute("兴奋", 0, 2), DrugAttribute("抗抑郁", 0, 2)),
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
        description = "左乙拉西坦结合突触囊泡蛋白2A并抑制电压门控钙通道介导的神经递质释放，用于部分性及全身性癫痫发作的单药或添加治疗；口服生物利用度近百分之百且不经肝细胞色素酶代谢故药物相互作用少，可致嗜睡、乏力、易怒与行为异常，精神病史者需监测情绪与自杀倾向。",
        attributes = listOf(DrugAttribute("抗癫痫", 4, 5), DrugAttribute("镇静", 1, 3), DrugAttribute("促认知", 0, 1)),
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
        description = "芬洛西坦为吡拉西坦的苯环衍生物，调节多巴胺与去甲肾上腺素能传递并增加脑内ATP，产生促认知、温和兴奋与抗抑郁作用，用于躯体疲劳、手术后恢复与认知减退的短期辅助；被世界反兴奋剂机构列为赛内禁用物质，耐受性形成较快，焦虑症与高血压患者慎用。",
        attributes = listOf(DrugAttribute("促认知", 2, 4), DrugAttribute("兴奋", 1, 3), DrugAttribute("欣快", 0, 2), DrugAttribute("抗抑郁", 1, 2)),
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
        description = "多潘立酮为外周多巴胺D2受体拮抗剂，提高食管下括约肌张力、增强胃窦收缩并协调胃排空，用于消化不良、胃食管反流与放疗及化疗止吐；不易透过血脑屏障故锥体外系反应少见但可致高泌乳素血症与男性乳房发育，与酮康唑或红霉素联用增加QT延长与猝死风险。",
        attributes = listOf(DrugAttribute("止吐", 3, 5), DrugAttribute("促胃肠动力", 3, 5), DrugAttribute("锥体外系反应", -1, 2)),
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
        description = "甲氧氯普胺为中枢及外周多巴胺D2受体拮抗剂并激动5-HT4受体促进胃动力，用于糖尿病性胃轻瘫、反流性食管炎与术后呕吐；易透过血脑屏障，可致急性锥体外系反应、迟发性运动障碍与高泌乳素性闭经，儿童、老年人及精神病患者尤须谨慎，与抗胆碱药相互拮抗。",
        attributes = listOf(DrugAttribute("止吐", 3, 5), DrugAttribute("促胃肠动力", 3, 5), DrugAttribute("锥体外系反应", -1, 4), DrugAttribute("镇静", 1, 3)),
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

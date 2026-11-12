package com.fangshen.manual.model

/** 药库分片：条目过多，单一 object 的 <clinit> 会超过 JVM 方法体上限，故拆分。 */
internal object DrugCatalogPart4 {

    private val ondansetron = d(
        key = "ondansetron", name = "昂丹司琼", colorArgb = 0xFF00BFA5, tabletDose = 8.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("镇吐剂"),
        effectTags = listOf("血清素受体拮抗剂"),
        moleculeTag = "咔唑类（5-HT3 拮抗）", halfLifeHours = 3.0, bioavailabilityPct = 60,
        route = Route.ORAL, tmaxHours = 1.5,
        description = "昂丹司琼为选择性5-HT3受体拮抗剂，阻断外周及中枢迷走神经末梢的5-HT3受体而发挥强效镇吐作用，主要用于化疗、放疗及术后恶心呕吐的预防与治疗；常见头痛与便秘，大剂量可延长QT间期，与5-HT能药物联用需警惕血清素综合征。",
        attributes = listOf(DrugAttribute("镇吐", 4, 5), DrugAttribute("抗焦虑", 0, 1), DrugAttribute("镇静", 0, 1), DrugAttribute("依赖性", -1, 0)),
        rules = listOf(
            DoseEffectRule(4.0.toBigDecimal(), "镇吐", 1),
            DoseEffectRule(8.0.toBigDecimal(), "镇吐", 2),
        ),
    )

    private val granisetron = d(
        key = "granisetron", name = "格拉司琼", colorArgb = 0xFF00BFA5, tabletDose = 1.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("镇吐剂"),
        effectTags = listOf("血清素受体拮抗剂"),
        moleculeTag = "咔唑类（5-HT3 拮抗）", halfLifeHours = 5.0, bioavailabilityPct = 60,
        route = Route.ORAL, tmaxHours = 2.0,
        description = "格拉司琼为高选择性5-HT3受体拮抗剂，作用强度与持续时间和昂丹司琼相当，用于化疗、放疗、术后及眩晕相关的恶心呕吐；耐受性好，常见头痛、便秘与一过性转氨酶升高，与酮康唑、CYP3A4诱导剂合用血药浓度变化需监测。",
        attributes = listOf(DrugAttribute("镇吐", 4, 5), DrugAttribute("抗焦虑", 0, 1), DrugAttribute("镇静", 0, 1), DrugAttribute("依赖性", -1, 0)),
        rules = listOf(
            DoseEffectRule(1.0.toBigDecimal(), "镇吐", 1),
            DoseEffectRule(2.0.toBigDecimal(), "镇吐", 2),
        ),
    )

    private val aprepitant = d(
        key = "aprepitant", name = "阿瑞匹坦", colorArgb = 0xFF00BFA5, tabletDose = 80.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("镇吐剂"),
        effectTags = listOf("NK1受体拮抗剂"),
        moleculeTag = "三唑啉酮类（P 物质阻断）", halfLifeHours = 12.0, bioavailabilityPct = 60,
        route = Route.ORAL, tmaxHours = 3.0,
        substrates = listOf("CYP3A4"),
        description = "阿瑞匹坦为神经激肽-1（NK1）受体拮抗剂，阻断P物质介导的呕吐反射，与地塞米松及5-HT3拮抗剂三联用于化疗后急性和延迟性呕吐；为CYP3A4中等抑制剂，可升高地塞米松与甲泼尼龙浓度，并使华法林与口服避孕药血药浓度下降。",
        attributes = listOf(DrugAttribute("镇吐", 3, 5), DrugAttribute("抗抑郁", 0, 1), DrugAttribute("镇静", 0, 1), DrugAttribute("依赖性", -1, 0)),
        rules = listOf(
            DoseEffectRule(80.0.toBigDecimal(), "镇吐", 1),
            DoseEffectRule(125.0.toBigDecimal(), "镇吐", 2),
        ),
    )

    // —— 逆转剂（解毒与拮抗，负权重抵消对应效应）——
    private val naloxone = d(
        key = "naloxone", name = "纳洛酮", colorArgb = 0xFFFFFFFF, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("逆转剂"),
        effectTags = listOf("阿片类受体拮抗剂"),
        moleculeTag = "吗啡 N-烯丙基衍生物（阿片拮抗）", halfLifeHours = 1.5, bioavailabilityPct = 100,
        route = Route.IV, tmaxHours = null,
        description = "纳洛酮为纯阿片受体拮抗剂，对μ受体亲和力强，可迅速逆转阿片类镇痛药与海洛因过量所致的呼吸抑制和昏迷，也用于乙醇及镇静催眠药中毒的催醒；因其作用时间短于多数阿片激动剂，需重复给药或静脉滴注，并警惕疼痛复发与再度呼吸抑制。",
        attributes = listOf(DrugAttribute("阿片拮抗", 4, 5), DrugAttribute("催醒", 3, 4), DrugAttribute("抗休克", 0, 2), DrugAttribute("依赖性", -1, 0)),
        rules = listOf(
            DoseEffectRule(0.1.toBigDecimal(), "呼吸抑制", -2),
            DoseEffectRule(0.1.toBigDecimal(), "镇痛", -1),
            DoseEffectRule(0.4.toBigDecimal(), "欣快", -1),
            DoseEffectRule(0.4.toBigDecimal(), "镇静", -1),
            DoseEffectRule(0.4.toBigDecimal(), "瞳孔缩小", -1),
        ),
    )

    private val flumazenil = d(
        key = "flumazenil", name = "氟马西尼", colorArgb = 0xFFFFFFFF, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("逆转剂"),
        effectTags = listOf("苯二氮卓类受体拮抗剂"),
        moleculeTag = "苯并二氮卓类（咪唑并，受体拮抗）", halfLifeHours = 0.7, bioavailabilityPct = 100,
        route = Route.IV, tmaxHours = null,
        description = "氟马西尼特异性竞争GABA-A受体上的苯二氮卓类识别位点，可逆转地西泮、艾司唑仑等药物的中枢抑制作用，用于苯二氮卓类过量解救与全麻后催醒；作用持续约1至2小时、短于多数苯二氮卓类，用药后可能再次陷入抑制，对长期用药者可诱发急性戒断与癫痫。",
        attributes = listOf(DrugAttribute("苯二氮卓拮抗", 4, 5), DrugAttribute("催醒", 3, 4), DrugAttribute("惊厥风险", -1, 2), DrugAttribute("依赖性", -1, 0)),
        rules = listOf(
            DoseEffectRule(0.2.toBigDecimal(), "镇静", -1),
            DoseEffectRule(0.5.toBigDecimal(), "镇静", -2),
            DoseEffectRule(1.0.toBigDecimal(), "呼吸抑制", -1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GABA, -1)),
    )

    private val cyproheptadine = d(
        key = "cyproheptadine", name = "赛庚啶", colorArgb = 0xFFFFFFFF, tabletDose = 4.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("逆转剂", "谵妄剂"),
        effectTags = listOf("抗组胺剂", "抗胆碱能剂", "血清素受体拮抗剂"),
        moleculeTag = "哌啶类（5-HT2/组胺双拮抗）", halfLifeHours = 8.0, bioavailabilityPct = 70,
        route = Route.ORAL, tmaxHours = 4.0,
        description = "赛庚啶阻断H1受体与5-HT2受体，兼具抗组胺、抗5-HT、轻度抗胆碱与镇静作用，用于荨麻疹、过敏性鼻炎及血管性头痛；因其阻断5-HT2可增加食欲，曾用于神经性厌食与性欲减退的辅助治疗，青光眼、前列腺增生、婴幼儿及孕妇慎用。",
        attributes = listOf(DrugAttribute("抗组胺", 3, 4), DrugAttribute("抗5-HT", 2, 3), DrugAttribute("镇静", 2, 3), DrugAttribute("增食欲", 2, 3), DrugAttribute("抗胆碱", 1, 2), DrugAttribute("依赖性", -1, 0)),
        rules = listOf(
            DoseEffectRule(4.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(8.0.toBigDecimal(), "镇吐", 1),
            DoseEffectRule(12.0.toBigDecimal(), "口干", 1),
            DoseEffectRule(20.0.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.HISTAMINE, -1),
            NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -1),
        ),
    )

    private val epinephrine = d(
        key = "epinephrine", name = "肾上腺素", colorArgb = 0xFFFFFFFF, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("逆转剂"),
        effectTags = listOf("肾上腺素受体激动剂"),
        moleculeTag = "儿茶酚胺（过敏性休克急救）", halfLifeHours = 0.25, bioavailabilityPct = 100,
        route = Route.IV, tmaxHours = null,
        description = "肾上腺素为非选择性α与β肾上腺素受体激动剂，收缩皮肤黏膜内脏血管、兴奋心脏并松弛支气管平滑肌，是过敏性休克、心脏骤停和重度哮喘发作的首选急救药；剂量过大或与MAOI、三环类抗抑郁药合用可致心律失常、血压剧升与脑出血，静注外漏致组织坏死。",
        attributes = listOf(DrugAttribute("升压", 4, 5), DrugAttribute("兴奋心脏", 4, 5), DrugAttribute("平喘", 3, 4), DrugAttribute("抗过敏", 4, 5), DrugAttribute("抗休克", 4, 5)),
        rules = listOf(
            DoseEffectRule(0.1.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(0.25.toBigDecimal(), "高血压", 1),
            DoseEffectRule(0.5.toBigDecimal(), "心动过速", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.NOREPINEPHRINE, 2)),
    )

    private val physostigmine = d(
        key = "physostigmine", name = "毒扁豆碱", colorArgb = 0xFFFFFFFF, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("逆转剂"),
        effectTags = listOf("胆碱酯酶抑制剂"),
        moleculeTag = "吲哚类生物碱（可透血脑屏障）", halfLifeHours = 0.5, bioavailabilityPct = 100,
        route = Route.IV, tmaxHours = null,
        description = "毒扁豆碱为可逆性乙酰胆碱酯酶抑制剂，脂溶性高、易透过血脑屏障，提高中枢乙酰胆碱水平，用于阿托品等抗胆碱能药物中毒的解救和全麻后催醒；过量引起胆碱能危象（心动过缓、腺体分泌、支气管痉挛与肌束震颤），需与阿托品交替使用、及时洗胃。",
        attributes = listOf(DrugAttribute("抗胆碱酯酶", 4, 5), DrugAttribute("催醒", 2, 3), DrugAttribute("促认知", 0, 2), DrugAttribute("抗胆碱能中毒解救", 4, 5)),
        rules = listOf(
            DoseEffectRule(1.0.toBigDecimal(), "谵妄", -1),
            DoseEffectRule(1.0.toBigDecimal(), "口干", -1),
            DoseEffectRule(1.0.toBigDecimal(), "视物模糊", -1),
            DoseEffectRule(2.0.toBigDecimal(), "谵妄", -2),
            DoseEffectRule(2.0.toBigDecimal(), "恶心", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 2)),
    )

    private val atropine = d(
        key = "atropine", name = "阿托品", colorArgb = 0xFFFFFFFF, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("逆转剂", "谵妄剂"),
        effectTags = listOf("毒蕈碱受体拮抗剂", "抗胆碱能剂"),
        moleculeTag = "托烷类生物碱（有机磷中毒解毒）", halfLifeHours = 2.0, bioavailabilityPct = 100,
        route = Route.IV, tmaxHours = null,
        description = "阿托品为非选择性M胆碱受体阻断剂，解除平滑肌痉挛、抑制腺体分泌、加快心率并散大瞳孔，用于内脏绞痛、窦性心动过缓、麻醉前给药及有机磷农药中毒（与胆碱酯酶复能剂合用）；过量出现口干面红、视物模糊、心动过速、排尿困难乃至谵妄惊厥，体温升高者尤慎。",
        attributes = listOf(DrugAttribute("抗胆碱", 4, 5), DrugAttribute("解痉", 3, 4), DrugAttribute("扩瞳", 4, 5), DrugAttribute("抑制腺体分泌", 3, 4), DrugAttribute("抗休克", 2, 4)),
        rules = listOf(
            DoseEffectRule(0.5.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(1.0.toBigDecimal(), "口干", 1),
            DoseEffectRule(3.0.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(5.0.toBigDecimal(), "视物模糊", 1),
            DoseEffectRule(10.0.toBigDecimal(), "谵妄", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.ACETYLCHOLINE, -2)),
    )

    private val esketamine = d(
        key = "esketamine", name = "艾司氯胺酮", colorArgb = 0xFF90CAF9, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("解离剂", "抗抑郁剂"),
        effectTags = listOf("NMDA受体拮抗剂"),
        moleculeTag = "芳基环己胺类（氯胺酮右旋异构体，鼻喷剂）", halfLifeHours = 7.5, bioavailabilityPct = 50,
        route = Route.NASAL_SPRAY, tmaxHours = 0.5,
        description = "艾司氯胺酮为氯胺酮的S对映异构体、强效非竞争性NMDA受体拮抗剂，鼻喷剂用于难治性抑郁症，数小时至数天内起效并可快速降低自杀意念；伴分离感、感知扭曲与一过性血压升高，具有滥用与依赖潜力，每次给药后需监测血压两小时以上，与苯二氮卓类合用可能削弱疗效。",
        attributes = listOf(DrugAttribute("抗抑郁", 3, 5), DrugAttribute("镇痛", 2, 4), DrugAttribute("致幻", 1, 3), DrugAttribute("分离感", 2, 4), DrugAttribute("依赖性", -1, 3)),
        addictive = true, addictionRatePerHour = 0.015, toleranceGroup = "解离剂耐受",
        rules = listOf(
            DoseEffectRule(28.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(56.0.toBigDecimal(), "解离", 1),
            DoseEffectRule(84.0.toBigDecimal(), "解离", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GLUTAMATE, -1)),
    )

    // —— 认知/记忆类补剂 ——
    private val citicoline = d(
        key = "citicoline", name = "胞磷胆碱钠", colorArgb = 0xFFF57F17, tabletDose = 200.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("认知增强剂", "记忆增强剂"),
        effectTags = listOf("胆碱供体"),
        moleculeTag = "胞苷二磷酸胆碱（CDP-胆碱）", halfLifeHours = 12.0, bioavailabilityPct = 50,
        route = Route.ORAL, tmaxHours = 3.0,
        description = "胞磷胆碱钠为核苷衍生物，促进磷脂酰胆碱合成并调节多巴胺与乙酰胆碱代谢，稳定神经元膜、减轻缺血损伤，用于颅脑外伤、脑血管意外及帕金森病的辅助康复，可缩短昏迷时间、改善注意力与记忆；安全性高，偶见面部发热、一过性血压下降与睡眠增多。",
        attributes = listOf(DrugAttribute("促认知", 1, 2), DrugAttribute("神经保护", 1, 2), DrugAttribute("促醒", 1, 2), DrugAttribute("依赖性", -1, 0)),
        rules = listOf(
            DoseEffectRule(200.0.toBigDecimal(), "善记", 1),
            DoseEffectRule(400.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(1000.0.toBigDecimal(), "善记", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.ACETYLCHOLINE, 1)),
    )

    private val phosphatidylserine = d(
        key = "phosphatidylserine", name = "磷脂酰丝氨酸", colorArgb = 0xFFF57F17, tabletDose = 100.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.HEALTH_FOOD,
        subjectiveTags = listOf("记忆增强剂", "认知增强剂"),
        effectTags = listOf("磷脂类膜成分"),
        moleculeTag = "磷脂酰丝氨酸（大豆/向日葵来源）", halfLifeHours = 10.0, bioavailabilityPct = 60,
        route = Route.ORAL, tmaxHours = 3.0,
        description = "磷脂酰丝氨酸为神经元膜的重要磷脂成分，维持膜流动性与受体功能，并参与调节下丘脑-垂体-肾上腺轴的应激反应，用于老年认知功能减退、注意缺陷与运动应激的辅助营养支持；可与咖啡因联用增强警觉性，睡前服用可能加重失眠，鱼油过敏者慎用。",
        attributes = listOf(DrugAttribute("促认知", 1, 2), DrugAttribute("抗应激", 0, 1), DrugAttribute("镇静", 0, 1), DrugAttribute("依赖性", -1, 0)),
        rules = listOf(
            DoseEffectRule(100.0.toBigDecimal(), "善记", 1),
            DoseEffectRule(300.0.toBigDecimal(), "专注", 1),
            DoseEffectRule(600.0.toBigDecimal(), "善记", 2),
        ),
    )

    // ===== 活性代谢物（隐藏条目：由母药自动生成伪剂量，不在药库/服药列表展示）=====
    private val norfluoxetine = d(
        key = "norfluoxetine", name = "去甲氟西汀", colorArgb = 0xFFD4A017,
        tabletDose = null, doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗抑郁剂", "抗强迫剂"),
        effectTags = listOf("选择性血清素再摄取抑制剂"),
        moleculeTag = "三环酮类（去甲基代谢物）", halfLifeHours = 216.0, bioavailabilityPct = 100,
        route = Route.ORAL, tmaxHours = 8.0,
        description = "去甲氟西汀为氟西汀的活性去甲基代谢物，同样选择性抑制5-HT再摄取，母药停药后仍持续发挥抗抑郁、抗强迫与抗贪食作用；半衰期长达1至2周，并强效抑制CYP2D6与2C9，与三环类抗抑郁药、苯妥英、华法林及MAOI联用易致血药浓度升高与血清素综合征。",
        attributes = listOf(DrugAttribute("抗抑郁", 2, 5), DrugAttribute("抗焦虑", 1, 4), DrugAttribute("抗强迫", 2, 5), DrugAttribute("镇静", 0, 2), DrugAttribute("停药综合征", -1, 1)),
        addictive = true, addictionRatePerHour = 0.008, toleranceGroup = "血清素能停药",
        rules = listOf(
            DoseEffectRule(20.0.toBigDecimal(), "愉悦", 1),
            DoseEffectRule(20.0.toBigDecimal(), "释然", 1),
            DoseEffectRule(40.0.toBigDecimal(), "释然", 2),
            DoseEffectRule(60.0.toBigDecimal(), "焦虑", 1),
        ),
        hidden = true,
    )

    private val nordazepam = d(
        key = "nordazepam", name = "去甲西泮", colorArgb = 0xFF2E7D32,
        tabletDose = null, doseUnit = DoseUnit.MG, managementClass = ManagementClass.CONTROLLED_II,
        subjectiveTags = listOf("抗焦虑剂", "镇静剂"),
        effectTags = listOf("苯二氮卓类受体激动剂", "GABA受体激动剂"),
        moleculeTag = "苯二氮卓类（去甲基代谢物）", halfLifeHours = 100.0, bioavailabilityPct = 100,
        route = Route.ORAL, tmaxHours = 2.0,
        description = "去甲西泮为地西泮的活性去甲基代谢物，激动GABA-A受体上的苯二氮卓位点，产生抗焦虑、镇静催眠、抗惊厥与中枢性肌肉松弛作用；半衰期50小时以上、易体内蓄积，老人可致宿醉感、记忆受损与跌倒，长期服用产生耐受、依赖与戒断，与乙醇、阿片类合用呼吸抑制风险显著。",
        attributes = listOf(DrugAttribute("抗焦虑", 3, 5), DrugAttribute("镇静催眠", 2, 4), DrugAttribute("抗惊厥", 2, 4), DrugAttribute("肌肉松弛", 1, 3), DrugAttribute("依赖性", -1, 3)),
        addictive = true, addictionRatePerHour = 0.02, toleranceGroup = "GABA能耐受",
        rules = listOf(
            DoseEffectRule(2.5.toBigDecimal(), "镇静", 1),
            DoseEffectRule(5.0.toBigDecimal(), "镇静", 2),
            DoseEffectRule(10.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(20.0.toBigDecimal(), "嗜睡", 2),
        ),
        hidden = true,
    )

    private val odesmethyltramadol = d(
        key = "odesmethyltramadol", name = "去甲曲马多", colorArgb = 0xFFB33A3A,
        tabletDose = null, doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("镇痛剂"),
        effectTags = listOf("阿片类受体激动剂"),
        moleculeTag = "环己醇类（O-去甲基代谢物）", halfLifeHours = 9.0, bioavailabilityPct = 100,
        route = Route.ORAL, tmaxHours = 2.0,
        description = "去甲曲马多为曲马多经CYP2D6生成的主要活性代谢物，对μ阿片受体亲和力远高于母药，并抑制5-HT与去甲肾上腺素再摄取，镇痛与依赖性均显著强于曲马多；CYP2D6弱代谢者活性下降，与MAOI、SSRI、SNRI联用可致血清素综合征，与苯二氮卓类、乙醇合用呼吸抑制加深。",
        attributes = listOf(DrugAttribute("镇痛", 3, 5), DrugAttribute("抗抑郁", 0, 2), DrugAttribute("呼吸抑制", -1, 2), DrugAttribute("依赖性", -1, 4)),
        addictive = true, addictionRatePerHour = 0.03, toleranceGroup = "通用耐受",
        rules = listOf(
            DoseEffectRule(25.0.toBigDecimal(), "镇痛", 1),
            DoseEffectRule(50.0.toBigDecimal(), "镇痛", 2),
            DoseEffectRule(100.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(150.0.toBigDecimal(), "呼吸抑制", 1),
        ),
        hidden = true,
    )

    // ===== 抗癫痫（非线性消除示范：苯妥英）=====
    private val phenytoin = d(
        key = "phenytoin", name = "苯妥英", colorArgb = 0xFF3949AB, tabletDose = 100.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗癫痫剂"),
        effectTags = listOf("钠通道阻滞剂"),
        moleculeTag = "乙内酰脲类", halfLifeHours = 22.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 4.0,
        substrates = listOf("CYP2C9", "CYP2C19"),
        vdLiters = 50.0,
        nonlinearity = MichaelisMenten(vmaxMgPerLPerHour = 0.41, kmMgPerL = 4.0),
        description = "苯妥英阻断电压依赖性钠通道、抑制癫痫灶放电扩散，对强直阵挛发作与癫痫持续状态疗效确切，亦用于洋地黄中毒性室性心律失常；治疗窗窄并呈零级动力学，为肝酶强诱导剂，与华法林、口服避孕药、异烟肼、氟康唑相互作用复杂，过量致共济失调、眼球震颤与意识模糊。",
        attributes = listOf(DrugAttribute("抗癫痫", 3, 5), DrugAttribute("抗心律失常", 1, 3), DrugAttribute("镇静", 0, 1), DrugAttribute("共济失调", -1, 2)),
        rules = listOf(
            DoseEffectRule(100.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(200.0.toBigDecimal(), "头晕", 1),
            DoseEffectRule(300.0.toBigDecimal(), "分心", 1),
            DoseEffectRule(400.0.toBigDecimal(), "谵妄", 1),
        ),
    )

    // ===== 抗感染药：本身无精神活性，但参与精神药品的联用预警 =====
    private val cefoperazone = d(
        key = "cefoperazone", name = "头孢哌酮", colorArgb = 0xFF80CBC4, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("双硫仑样反应药", "头孢类（含 MTT 侧链）"),
        moleculeTag = "第三代头孢菌素", halfLifeHours = 2.0, bioavailabilityPct = 100,
        route = Route.IV, tmaxHours = null,
        attributes = listOf(DrugAttribute("抗菌", -1, 5), DrugAttribute("抗铜绿假单胞菌", -1, 5), DrugAttribute("出血倾向", -1, 1), DrugAttribute("双硫仑样反应", -1, 2)),
        description = "头孢哌酮为第三代头孢菌素，主要经胆汁排泄、胆道与腹腔感染疗效好，对铜绿假单胞菌与革兰阴性菌作用强；含甲硫氮唑侧链、抑制维生素K依赖凝血因子合成，与华法林、肝素及溶栓药合用增加出血风险，用药期间及停药数日内饮酒可致双硫仑样反应。",
    )

    private val metronidazole = d(
        key = "metronidazole", name = "甲硝唑", colorArgb = 0xFF80CBC4, tabletDose = 200.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("双硫仑样反应药", "硝基咪唑类"),
        moleculeTag = "硝基咪唑类", halfLifeHours = 8.0, bioavailabilityPct = 99, route = Route.ORAL,
        tmaxHours = 2.0,
        inhibitors = listOf("CYP3A4"),
        attributes = listOf(DrugAttribute("抗厌氧菌", -1, 5), DrugAttribute("抗滴虫", -1, 5), DrugAttribute("抗阿米巴", -1, 5), DrugAttribute("双硫仑样反应", -1, 3)),
        description = "甲硝唑为硝基咪唑类衍生物，其活性代谢物破坏厌氧菌、滴虫与阿米巴原虫的DNA结构，为腹腔、盆腔、口腔厌氧菌感染及滴虫性阴道炎的首选药；抑制乙醛脱氢酶，用药期间及停药三日内饮酒致双硫仑样反应，并可增强华法林抗凝作用、诱发口腔金属味与周围神经炎。",
    )

    private val clarithromycin = d(
        key = "clarithromycin", name = "克拉霉素", colorArgb = 0xFF80CBC4, tabletDose = 250.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("大环内酯类", "肝药酶抑制剂"),
        moleculeTag = "大环内酯类", halfLifeHours = 4.0, bioavailabilityPct = 55, route = Route.ORAL,
        tmaxHours = 3.0,
        inhibitors = listOf("CYP3A4"),
        attributes = listOf(DrugAttribute("抗菌", -1, 5), DrugAttribute("抗非典型病原体", -1, 5), DrugAttribute("QT延长", -1, 2), DrugAttribute("胃肠道反应", -1, 2)),
        description = "克拉霉素为大环内酯类抗生素，覆盖链球菌、肺炎支原体、军团菌与幽门螺杆菌，用于呼吸道、皮肤软组织感染及消化性溃疡的根除治疗；为CYP3A4强抑制剂，与辛伐他汀、秋水仙碱、麦角胺、卡马西平及延长QT间期药物联用毒性风险显著，肾功能不全需减量。",
    )

    private val erythromycin = d(
        key = "erythromycin", name = "红霉素", colorArgb = 0xFF80CBC4, tabletDose = 250.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("大环内酯类", "肝药酶抑制剂"),
        moleculeTag = "大环内酯类", halfLifeHours = 1.5, bioavailabilityPct = 35, route = Route.ORAL,
        tmaxHours = 2.5,
        inhibitors = listOf("CYP3A4"),
        attributes = listOf(DrugAttribute("抗菌", -1, 5), DrugAttribute("促胃肠动力", -1, 3), DrugAttribute("胆汁淤积性肝炎", -1, 2), DrugAttribute("QT延长", -1, 2)),
        description = "红霉素为大环内酯类原型药，抗菌谱与青霉素相似而覆盖支原体、衣原体与军团菌，并激动胃动素受体、作为促胃肠动力药用于胃轻瘫；为CYP3A4抑制剂，与茶碱、卡马西平、地高辛、甲泼尼龙及延长QT间期药物存在显著相互作用，酯化物可致胆汁淤积性肝炎。",
    )

    private val ciprofloxacin = d(
        key = "ciprofloxacin", name = "环丙沙星", colorArgb = 0xFF80CBC4, tabletDose = 500.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("喹诺酮类", "肝药酶抑制剂"),
        moleculeTag = "氟喹诺酮类", halfLifeHours = 4.0, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP1A2", "CYP3A4"),
        inhibitors = listOf("CYP1A2"),
        attributes = listOf(DrugAttribute("抗菌", -1, 5), DrugAttribute("抗铜绿假单胞菌", -1, 5), DrugAttribute("肌腱损伤", -1, 2), DrugAttribute("QT延长", -1, 1), DrugAttribute("关节软骨损伤", -1, 2)),
        description = "环丙沙星为氟喹诺酮类，抑制细菌DNA旋转酶与拓扑异构酶IV，覆盖革兰阴性菌、铜绿假单胞菌与部分革兰阳性菌，为尿路、肠道、呼吸道与骨关节感染的常用口服药；影响软骨发育，18岁以下、孕妇及哺乳期禁用，可致肌腱断裂、QT延长与周围神经病，含铝镁抗酸药及铁剂显著降低其吸收。",
    )

    private val rifampin = d(
        key = "rifampin", name = "利福平", colorArgb = 0xFF80CBC4, tabletDose = 300.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("利福霉素类", "肝药酶诱导剂"),
        moleculeTag = "利福霉素类", halfLifeHours = 3.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 2.0,
        inducers = listOf("CYP3A4", "CYP2C9", "CYP2C19", "CYP2B6", "CYP2C8"),
        attributes = listOf(DrugAttribute("抗结核", -1, 5), DrugAttribute("抗MRSA", -1, 4), DrugAttribute("肝毒性", -1, 2), DrugAttribute("酶诱导", -1, 3)),
        description = "利福平为利福霉素类，抑制细菌RNA聚合酶、对结核分枝杆菌与麻风杆菌杀菌力强，为抗结核一线药并用于MRSA、脑膜炎奈瑟菌带菌者的联合治疗；为CYP3A4强诱导剂，显著降低华法林、口服避孕药、环孢素、HIV蛋白酶抑制剂及苯二氮卓类血药浓度，本身可致肝损伤、使体液与隐形眼镜呈橘红色。",
    )

    private val linezolid = d(
        key = "linezolid", name = "利奈唑胺", colorArgb = 0xFF80CBC4, tabletDose = 600.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("单胺氧化酶抑制剂", "噁唑烷酮类"),
        moleculeTag = "噁唑烷酮类", halfLifeHours = 6.0, bioavailabilityPct = 100, route = Route.ORAL,
        tmaxHours = 1.5,
        inhibitors = listOf("MAO-A"),
        attributes = listOf(DrugAttribute("抗MRSA", -1, 5), DrugAttribute("抗VRE", -1, 5), DrugAttribute("血小板减少", -1, 2), DrugAttribute("血清素综合征", -1, 2)),
        description = "利奈唑胺为恶唑烷酮类，结合细菌50S亚基、阻断翻译起始，对耐甲氧西林金葡菌、耐万古霉素肠球菌与多重耐药革兰阳性菌作用强，用于皮肤软组织、肺炎与导管相关感染；为可逆性单胺氧化酶抑制剂，与SSRI、SNRI、肾上腺素能药物及含酪胺食物联用可致血压升高与血清素综合征，疗程超过两周可致血小板减少。",
    )

    private val isoniazid = d(
        key = "isoniazid", name = "异烟肼", colorArgb = 0xFF80CBC4, tabletDose = 300.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("单胺氧化酶抑制剂", "抗结核药"),
        moleculeTag = "异烟酸肼类", halfLifeHours = 3.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 1.0,
        inhibitors = listOf("MAO-A"),
        description = "异烟肼抑制结核分枝杆菌细胞壁分枝菌酸合成，杀菌力强、组织穿透性好，为各型结核病的基础一线药物；在肝内经N-乙酰转移酶代谢，慢乙酰化者易致周围神经炎，需同服维生素B6，与利福平、吡嗪酰胺联用肝毒性叠加，并可升高苯妥英与丙戊酸血药浓度，过量用维生素B6解救。",
        attributes = listOf(DrugAttribute("抗结核", 4, 5), DrugAttribute("抗麻风", 2, 3), DrugAttribute("周围神经炎", -1, 2), DrugAttribute("肝毒性", -1, 2)),
        rules = listOf(
            DoseEffectRule(300.0.toBigDecimal(), "肝损伤", 1),
        ),
    )

    private val ketoconazole = d(
        key = "ketoconazole", name = "酮康唑", colorArgb = 0xFF80CBC4, tabletDose = 200.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("咪唑类抗真菌药", "肝药酶抑制剂"),
        moleculeTag = "咪唑类", halfLifeHours = 8.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 2.0,
        inhibitors = listOf("CYP3A4"),
        attributes = listOf(DrugAttribute("抗真菌", -1, 5), DrugAttribute("抗雄激素", -1, 2), DrugAttribute("肝毒性", -1, 3), DrugAttribute("依赖性", -1, 0)),
        description = "酮康唑为咪唑类抗真菌药，阻断真菌细胞膜麦角甾醇合成中的羊毛甾醇14α-去甲基化，广谱覆盖念珠菌、皮肤癣菌与深部真菌；为CYP3A4强抑制剂，全身用药因严重肝毒性已限于芽生菌、组织胞浆菌病等的二线治疗与库欣综合征的皮质醇合成抑制，与辛伐他汀、环孢素、苯二氮卓类合用毒性风险显著。",
    )

    // ===== 心血管药：不以精神作用为目的，但带来嗜睡/头晕等精神相关副作用 =====
    private val propranolol = d(
        key = "propranolol", name = "普萘洛尔", colorArgb = 0xFFB39DDB, tabletDose = 10.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药", "抗焦虑剂"),
        effectTags = listOf("β受体阻滞剂（亲脂）"),
        moleculeTag = "芳氧丙醇胺类", halfLifeHours = 4.0, bioavailabilityPct = 30, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP2D6", "CYP1A2"),
        description = "普萘洛尔为非选择性β受体阻滞剂，减慢心率、降低心肌耗氧并抑制肾素分泌，用于高血压、心绞痛、甲状腺功能亢进、特发性震颤与偏头痛预防；可减轻表演性焦虑时的心悸、出汗与颤抖，支气管哮喘、严重心动过缓与低血压禁用，骤停可致反跳性高血压、心绞痛与心律失常。",
        attributes = listOf(DrugAttribute("降压", 3, 4), DrugAttribute("抗焦虑", 2, 3), DrugAttribute("抗震颤", 3, 4), DrugAttribute("抗偏头痛", 3, 4), DrugAttribute("减慢心率", 4, 5)),
        rules = listOf(
            DoseEffectRule(10.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(10.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(10.0.toBigDecimal(), "心动过速", -1),
            DoseEffectRule(40.0.toBigDecimal(), "头晕", 1),
            DoseEffectRule(80.0.toBigDecimal(), "抑郁", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.NOREPINEPHRINE, -1)),
    )

    private val metoprolol = d(
        key = "metoprolol", name = "美托洛尔", colorArgb = 0xFFB39DDB, tabletDose = 50.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药"),
        effectTags = listOf("β受体阻滞剂"),
        moleculeTag = "芳氧丙醇胺类", halfLifeHours = 5.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP2D6"),
        description = "美托洛尔为心脏选择性β1受体阻滞剂，减慢心率、降低血压与心肌耗氧，为高血压、心绞痛与慢性心力衰竭改善预后的基础药物；经CYP2D6代谢，与帕罗西汀、氟西汀、安非他酮等CYP2D6强抑制剂联用可使其血药浓度倍增、心动过缓加重，与维拉帕米、地尔硫卓静注合用可致严重传导阻滞。",
        attributes = listOf(DrugAttribute("降压", 3, 5), DrugAttribute("心力衰竭治疗", 3, 5), DrugAttribute("减慢心率", 4, 5), DrugAttribute("抗偏头痛", 1, 3)),
        rules = listOf(
            DoseEffectRule(50.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(100.0.toBigDecimal(), "头晕", 1),
        ),
    )

    private val bisoprolol = d(
        key = "bisoprolol", name = "比索洛尔", colorArgb = 0xFFB39DDB, tabletDose = 5.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药"),
        effectTags = listOf("β受体阻滞剂"),
        moleculeTag = "芳氧丙醇胺类", halfLifeHours = 12.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 3.0,
        description = "比索洛尔为高选择性β1受体阻滞剂，作用持久、肝肾双通道排泄、药代个体差异小，为高血压与慢性心力衰竭的一线药物，可显著降低心衰患者死亡率；脂溶性较低、中枢副作用少，与维拉帕米、地尔硫卓合用加重心动过缓与房室传导阻滞，严重支气管痉挛者仍需慎用。",
        attributes = listOf(DrugAttribute("降压", 3, 5), DrugAttribute("心力衰竭治疗", 3, 5), DrugAttribute("减慢心率", 4, 5), DrugAttribute("直立性低血压", -1, 1)),
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "嗜睡", 1),
        ),
    )

    private val clonidine = d(
        key = "clonidine", name = "可乐定", colorArgb = 0xFFB39DDB, tabletDose = 100.0.toBigDecimal(),
        doseUnit = DoseUnit.MCG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药", "抗多动剂"),
        effectTags = listOf("α2受体激动剂"),
        moleculeTag = "咪唑啉类", halfLifeHours = 12.0, bioavailabilityPct = 95, route = Route.ORAL,
        tmaxHours = 3.0,
        description = "可乐定为中枢α2肾上腺素受体激动剂，降低延髓交感神经传出而降压，并减少去甲肾上腺素释放，用于高血压、注意缺陷多动障碍及阿片、酒精、尼古丁戒断症状的辅助治疗；常见镇静、口干与直立性低血压，骤停可致反跳性高血压与心悸，与β阻滞剂、三环类抗抑郁药合用降压效果改变。",
        attributes = listOf(DrugAttribute("降压", 3, 5), DrugAttribute("抗焦虑", 1, 3), DrugAttribute("镇静", 2, 4), DrugAttribute("注意缺陷多动治疗", 2, 4), DrugAttribute("戒断综合征治疗", 2, 4)),
        rules = listOf(
            DoseEffectRule(100.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(100.0.toBigDecimal(), "口干", 1),
            DoseEffectRule(300.0.toBigDecimal(), "嗜睡", 2),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.NOREPINEPHRINE, -2)),
    )

    private val methyldopa = d(
        key = "methyldopa", name = "甲基多巴", colorArgb = 0xFFB39DDB, tabletDose = 250.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药"),
        effectTags = listOf("α2受体激动剂（前药）"),
        moleculeTag = "多巴酸衍生物", halfLifeHours = 2.0, bioavailabilityPct = 25, route = Route.ORAL,
        tmaxHours = 2.0,
        description = "甲基多巴在中枢转化为α-甲基去甲肾上腺素，激动延髓α2受体、抑制交感神经传出而降压，降压温和、不减少肾血流，为妊娠期慢性高血压的首选药物；常见镇静、口干与鼻塞，长期服用可致Coombs阳性溶血性贫血与药物性肝炎，禁与利血平、单胺氧化酶抑制剂及抑郁患者使用。",
        attributes = listOf(DrugAttribute("降压", 2, 4), DrugAttribute("镇静", 1, 3), DrugAttribute("抗焦虑", 0, 1), DrugAttribute("溶血性贫血", -1, 1)),
        rules = listOf(
            DoseEffectRule(250.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(500.0.toBigDecimal(), "头晕", 1),
            DoseEffectRule(1000.0.toBigDecimal(), "抑郁", 1),
        ),
    )

    private val reserpine = d(
        key = "reserpine", name = "利血平", colorArgb = 0xFFB39DDB, tabletDose = 250.0.toBigDecimal(),
        doseUnit = DoseUnit.MCG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药"),
        effectTags = listOf("单胺耗竭剂"),
        moleculeTag = "萝芙木生物碱", halfLifeHours = 50.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 4.0,
        description = "利血平抑制囊泡单胺转运体、耗竭交感神经末梢与中枢的去甲肾上腺素、多巴胺与5-羟色胺，降低血压并曾用于精神分裂症的镇静治疗；因可诱发或加重抑郁、帕金森样锥体外系症状、腹痛腹泻与胃酸分泌增多，现已很少使用，与MAOI、电休克及抑郁症患者联用风险高。",
        attributes = listOf(DrugAttribute("降压", 2, 4), DrugAttribute("抗精神病", 2, 4), DrugAttribute("镇静", 2, 3), DrugAttribute("锥体外系反应", -1, 3), DrugAttribute("致抑郁", -1, 3)),
        rules = listOf(
            DoseEffectRule(250.0.toBigDecimal(), "抑郁", 1),
            DoseEffectRule(500.0.toBigDecimal(), "抑郁", 2),
            DoseEffectRule(500.0.toBigDecimal(), "镇静", 1),
        ),
        neuros = listOf(
            NeuroDelta(Neurotransmitters.DOPAMINE, -1),
            NeuroDelta(Neurotransmitters.SEROTONIN, -1),
            NeuroDelta(Neurotransmitters.NOREPINEPHRINE, -1),
        ),
    )

    private val hydrochlorothiazide = d(
        key = "hydrochlorothiazide", name = "氢氯噻嗪", colorArgb = 0xFFB39DDB, tabletDose = 25.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药"),
        effectTags = listOf("利尿剂", "噻嗪类"),
        moleculeTag = "苯并噻二嗪类", halfLifeHours = 8.0, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 4.0,
        description = "氢氯噻嗪为噻嗪类利尿剂，抑制远曲小管Na-Cl协同转运、温和排钠利尿，为高血压与轻中度心力衰竭的基础一线药物；长期使用可致低钾、低钠、高尿酸、糖耐量异常与光敏反应，与ACEI、ARB联用降压增强但可致血肌酐升高，磺胺类药物交叉过敏。",
        attributes = listOf(DrugAttribute("降压", 3, 4), DrugAttribute("利尿", 3, 5), DrugAttribute("低钾血症", -1, 2), DrugAttribute("高尿酸血症", -1, 2)),
        rules = listOf(
            DoseEffectRule(25.0.toBigDecimal(), "头晕", 1),
        ),
    )

    private val indapamide = d(
        key = "indapamide", name = "吲达帕胺", colorArgb = 0xFFB39DDB, tabletDose = 2.5.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药"),
        effectTags = listOf("利尿剂", "吲哚类"),
        moleculeTag = "吲哚类利尿剂", halfLifeHours = 18.0, bioavailabilityPct = 93, route = Route.ORAL,
        tmaxHours = 4.0,
        description = "吲达帕胺为噻嗪样利尿剂，兼有轻度钙通道阻滞作用，降压作用持久、对糖脂代谢影响较小，为高血压单药或联合治疗常用药物；可致低钾、低钠与高尿酸，QT间期延长者与联用洋地黄、抗心律失常药时需监测电解质，磺胺过敏者慎用。",
        attributes = listOf(DrugAttribute("降压", 3, 5), DrugAttribute("利尿", 2, 4), DrugAttribute("低钾血症", -1, 2), DrugAttribute("高尿酸血症", -1, 1)),
        rules = listOf(
            DoseEffectRule(2.5.toBigDecimal(), "头晕", 1),
        ),
    )

    private val nifedipine = d(
        key = "nifedipine", name = "硝苯地平", colorArgb = 0xFFB39DDB, tabletDose = 10.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药"),
        effectTags = listOf("钙通道阻滞剂", "二氢吡啶类"),
        moleculeTag = "二氢吡啶类", halfLifeHours = 2.0, bioavailabilityPct = 50, route = Route.ORAL,
        tmaxHours = 1.0,
        substrates = listOf("CYP3A4"),
        description = "硝苯地平为二氢吡啶类钙通道阻滞剂，选择性扩张小动脉、降低外周阻力并解除冠脉痉挛，用于高血压、心绞痛与雷诺综合征；短效制剂舌下或速释可致血压骤降、反射性心动过速与心绞痛加重，控释片宜整片吞服，经CYP3A4代谢、与葡萄柚汁、克拉霉素、利福平相互作用显著。",
        attributes = listOf(DrugAttribute("降压", 3, 5), DrugAttribute("抗心绞痛", 3, 4), DrugAttribute("反射性心动过速", 1, 3), DrugAttribute("直立性低血压", -1, 2)),
        rules = listOf(
            DoseEffectRule(10.0.toBigDecimal(), "头晕", 1),
            DoseEffectRule(10.0.toBigDecimal(), "潮红", 1),
            DoseEffectRule(20.0.toBigDecimal(), "嗜睡", 1),
        ),
    )

    private val amlodipine = d(
        key = "amlodipine", name = "氨氯地平", colorArgb = 0xFFB39DDB, tabletDose = 5.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药"),
        effectTags = listOf("钙通道阻滞剂", "二氢吡啶类"),
        moleculeTag = "二氢吡啶类", halfLifeHours = 40.0, bioavailabilityPct = 64, route = Route.ORAL,
        tmaxHours = 8.0,
        substrates = listOf("CYP3A4"),
        description = "氨氯地平为长效二氢吡啶类钙通道阻滞剂，血管选择性高、降压平稳、半衰期约35小时、每日一次给药，为高血压与稳定型心绞痛的一线药物；常见踝部水肿、面部潮红与头痛，与辛伐他汀、酮康唑、克拉霉素合用相互作用明显，重度主动脉瓣狭窄与心源性休克者禁用。",
        attributes = listOf(DrugAttribute("降压", 3, 5), DrugAttribute("抗心绞痛", 3, 5), DrugAttribute("下肢水肿", 0, 2), DrugAttribute("直立性低血压", -1, 1)),
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "头晕", 1),
            DoseEffectRule(10.0.toBigDecimal(), "嗜睡", 1),
        ),
    )

    private val enalapril = d(
        key = "enalapril", name = "依那普利", colorArgb = 0xFFB39DDB, tabletDose = 10.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药"),
        effectTags = listOf("ACE抑制剂（前药）"),
        moleculeTag = "羧酸类", halfLifeHours = 11.0, bioavailabilityPct = 40, route = Route.ORAL,
        tmaxHours = 3.0,
        description = "依那普利为前体药，在体内水解为依那普利拉而抑制血管紧张素转换酶，扩张小动脉与小静脉、减少醛固酮并逆转心室重构，为高血压、心力衰竭与糖尿病肾病的一线药物；常见干咳与高钾血症，双侧肾动脉狭窄、妊娠与血管神经性水肿史者禁用，与留钾利尿剂、ARB、非甾体抗炎药联用风险增大。",
        attributes = listOf(DrugAttribute("降压", 3, 5), DrugAttribute("心力衰竭治疗", 3, 5), DrugAttribute("肾脏保护", 2, 4), DrugAttribute("干咳", -1, 2), DrugAttribute("高钾血症", -1, 2)),
        rules = listOf(
            DoseEffectRule(10.0.toBigDecimal(), "头晕", 1),
            DoseEffectRule(20.0.toBigDecimal(), "嗜睡", 1),
        ),
    )

    private val losartan = d(
        key = "losartan", name = "氯沙坦", colorArgb = 0xFFB39DDB, tabletDose = 50.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("心血管药"),
        effectTags = listOf("血管紧张素受体阻滞剂"),
        moleculeTag = "联苯四唑类", halfLifeHours = 2.0, bioavailabilityPct = 33, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP2C9", "CYP3A4"),
        description = "氯沙坦选择性阻断血管紧张素II的AT1受体，抑制血管收缩与醛固酮释放、保护心脏与肾脏靶器官，为高血压、心力衰竭与糖尿病肾病减少尿蛋白的一线药物；干咳等不良反应少于ACEI，仍可致高钾血症、肾功能下降与体位性低血压，妊娠、双侧肾动脉狭窄及严重肝功能不全禁用。",
        attributes = listOf(DrugAttribute("降压", 3, 5), DrugAttribute("心力衰竭治疗", 2, 5), DrugAttribute("肾脏保护", 2, 4), DrugAttribute("高钾血症", -1, 2)),
        rules = listOf(
            DoseEffectRule(50.0.toBigDecimal(), "头晕", 1),
        ),
    )

    // ===== v4：静脉麻醉药 =====
    private val propofol = d(
        key = "propofol", name = "丙泊酚", colorArgb = 0xFF455A64, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.NARCOTIC,
        subjectiveTags = listOf("静脉麻醉剂", "镇静剂"),
        effectTags = listOf("GABA受体激动剂"),
        moleculeTag = "烷基酚类", halfLifeHours = 2.0, bioavailabilityPct = 100, route = Route.IV,
        tmaxHours = 0.05,
        vdLiters = 300.0,
        description = "乳剂型静脉麻醉药，起效约 30 秒、苏醒快。治疗窗窄，仅麻醉场景使用；呼吸抑制与低血压是主要风险。",
        attributes = listOf(DrugAttribute("麻醉", 4, 5), DrugAttribute("催眠", 4, 5), DrugAttribute("镇静", 3, 5), DrugAttribute("镇痛", 0, 1), DrugAttribute("依赖性", -1, 2)),
        addictive = true, addictionRatePerHour = 0.015, toleranceGroup = "通用耐受",
        rules = listOf(
            DoseEffectRule(20.0.toBigDecimal(), "镇静", 2),
            DoseEffectRule(40.0.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(60.0.toBigDecimal(), "呼吸抑制", 1),
            DoseEffectRule(100.0.toBigDecimal(), "低血压", 1),
            DoseEffectRule(200.0.toBigDecimal(), "谵妄", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.GABA, 1)),
    )

    // ===== v4：食欲素受体拮抗剂（新型催眠药） =====
    private val suvorexant = d(
        key = "suvorexant", name = "苏沃雷生", colorArgb = 0xFF5C6BC0, tabletDose = 10.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("催眠剂"),
        effectTags = listOf("食欲素受体拮抗剂"),
        moleculeTag = "芳基哌嗪类", halfLifeHours = 12.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP3A4"),
        description = "阻断促醒的食欲素系统而助眠，不作用于 GABA，成瘾与依赖风险低于苯二氮䓬类。",
        attributes = listOf(DrugAttribute("催眠", 3, 5), DrugAttribute("镇静", 2, 4), DrugAttribute("抗焦虑", 0, 1), DrugAttribute("依赖性", -1, 1)),
        addictive = true, addictionRatePerHour = 0.015, toleranceGroup = "通用耐受",
        rules = listOf(
            DoseEffectRule(10.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(15.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(20.0.toBigDecimal(), "嗜睡", 2),
            DoseEffectRule(30.0.toBigDecimal(), "头晕", 1),
        ),
    )

    private val lemborexant = d(
        key = "lemborexant", name = "莱博雷生", colorArgb = 0xFF5C6BC0, tabletDose = 5.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("催眠剂"),
        effectTags = listOf("食欲素受体拮抗剂"),
        moleculeTag = "芳基哌嗪类", halfLifeHours = 17.0, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4"),
        description = "对食欲素 1/2 受体均高选择性拮抗，入睡与睡眠维持双适应证。",
        attributes = listOf(DrugAttribute("催眠", 3, 5), DrugAttribute("镇静", 1, 4), DrugAttribute("抗焦虑", 0, 1), DrugAttribute("依赖性", -1, 1)),
        addictive = true, addictionRatePerHour = 0.015, toleranceGroup = "通用耐受",
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(10.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(10.0.toBigDecimal(), "嗜睡", 2),
        ),
    )

    private val daridorexant = d(
        key = "daridorexant", name = "达利雷生", colorArgb = 0xFF5C6BC0, tabletDose = 25.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("催眠剂"),
        effectTags = listOf("食欲素受体拮抗剂"),
        moleculeTag = "芳基哌嗪类", halfLifeHours = 8.0, bioavailabilityPct = 62, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4"),
        description = "半衰期最短的食欲素拮抗剂，次日残留嗜睡更少。",
        attributes = listOf(DrugAttribute("催眠", 3, 5), DrugAttribute("镇静", 1, 3), DrugAttribute("抗焦虑", 0, 1), DrugAttribute("依赖性", -1, 1)),
        addictive = true, addictionRatePerHour = 0.015, toleranceGroup = "通用耐受",
        rules = listOf(
            DoseEffectRule(25.0.toBigDecimal(), "嗜睡", 1),
            DoseEffectRule(50.0.toBigDecimal(), "镇静", 1),
            DoseEffectRule(50.0.toBigDecimal(), "嗜睡", 2),
        ),
    )

    // ===== v4：策划药（研究化学品/滥用；mephedrone 为 v3 已有条目） =====

    private val mdpv = d(
        key = "mdpv", name = "MDPV", colorArgb = 0xFFAD1457, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("中枢兴奋剂", "欣快剂"),
        effectTags = listOf("多巴胺再摄取抑制剂"),
        moleculeTag = "吡咯烷酮苯基卡西酮", halfLifeHours = 6.0, bioavailabilityPct = 50,
        route = Route.INSUFFLATION, tmaxHours = 1.0,
        description = "最强效的多巴胺再摄取抑制剂之一，渴求与刻板行为（反复无意义动作）极常见。",
        addictive = true, addictionRatePerHour = 0.15, toleranceGroup = "兴奋剂耐受",
        attributes = listOf(DrugAttribute("兴奋", 4, 5), DrugAttribute("欣快", 4, 5), DrugAttribute("致幻", 1, 3), DrugAttribute("精神错乱", -1, 5), DrugAttribute("依赖性", -1, 5)),
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(10.0.toBigDecimal(), "欣快", 2),
            DoseEffectRule(10.0.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(20.0.toBigDecimal(), "焦虑", 2),
            DoseEffectRule(30.0.toBigDecimal(), "谵妄", 1),
            DoseEffectRule(40.0.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.DOPAMINE, 3)),
    )

    private val alphaPvp = d(
        key = "alpha_pvp", name = "α-PVP", colorArgb = 0xFFAD1457, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("中枢兴奋剂", "欣快剂"),
        effectTags = listOf("多巴胺再摄取抑制剂"),
        moleculeTag = "α-吡咯烷苯戊酮", halfLifeHours = 8.0, bioavailabilityPct = 50,
        route = Route.INSUFFLATION, tmaxHours = 1.0,
        description = "「浴盐」类中最常致「兴奋性谵妄」者，可数日不睡、攻击性强，剂量差之毫厘效应天差地别。",
        addictive = true, addictionRatePerHour = 0.18, toleranceGroup = "兴奋剂耐受",
        attributes = listOf(DrugAttribute("兴奋", 4, 5), DrugAttribute("欣快", 4, 5), DrugAttribute("致幻", 2, 4), DrugAttribute("精神错乱", -1, 5), DrugAttribute("依赖性", -1, 5)),
        rules = listOf(
            DoseEffectRule(10.0.toBigDecimal(), "欣快", 2),
            DoseEffectRule(20.0.toBigDecimal(), "心动过速", 1),
            DoseEffectRule(30.0.toBigDecimal(), "焦虑", 2),
            DoseEffectRule(40.0.toBigDecimal(), "谵妄", 2),
            DoseEffectRule(60.0.toBigDecimal(), "高血压", 1),
        ),
        neuros = listOf(NeuroDelta(Neurotransmitters.DOPAMINE, 3)),
    )

    private val twoCB = d(
        key = "2cb", name = "2C-B", colorArgb = 0xFF6A1B9A, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("致幻剂", "共情剂"),
        effectTags = listOf("血清素受体激动剂"),
        moleculeTag = "苯乙胺类（含甲氧基与溴取代）", halfLifeHours = 5.0, bioavailabilityPct = 50,
        route = Route.ORAL, tmaxHours = 2.0,
        description = "兼具致幻与共情效应，剂量翻倍效应骤变；与 MAOI 联用极危险。",
        attributes = listOf(DrugAttribute("致幻", 3, 5), DrugAttribute("兴奋", 2, 4), DrugAttribute("欣快", 3, 4), DrugAttribute("共情", 2, 3), DrugAttribute("依赖性", -1, 0)),
        rules = listOf(
            DoseEffectRule(5.0.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(10.0.toBigDecimal(), "解离", 1),
            DoseEffectRule(15.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(20.0.toBigDecimal(), "解离", 2),
            DoseEffectRule(25.0.toBigDecimal(), "谵妄", 1),
        ),
    )

    private val nbome = d(
        key = "nbome", name = "25I-NBOMe", colorArgb = 0xFF6A1B9A, tabletDose = null,
        doseUnit = DoseUnit.MCG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("致幻剂"),
        effectTags = listOf("血清素受体激动剂"),
        moleculeTag = "N-甲氧基苄基苯乙胺类", halfLifeHours = 6.0, bioavailabilityPct = 20,
        route = Route.SUBLINGUAL, tmaxHours = 1.5,
        description = "舌下含服的「邮票」致幻剂，有效剂量以微克计，过量风险极高，曾多次致死。",
        attributes = listOf(DrugAttribute("致幻", 4, 5), DrugAttribute("兴奋", 2, 4), DrugAttribute("欣快", 0, 3), DrugAttribute("血管收缩", -1, 3), DrugAttribute("依赖性", -1, 0)),
        rules = listOf(
            DoseEffectRule(300.0.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(500.0.toBigDecimal(), "解离", 1),
            DoseEffectRule(700.0.toBigDecimal(), "解离", 2),
            DoseEffectRule(1000.0.toBigDecimal(), "谵妄", 2),
            DoseEffectRule(1500.0.toBigDecimal(), "血清素综合征", 1),
        ),
    )

    private val apb = d(
        key = "apb", name = "5-APB / 6-APB", colorArgb = 0xFF6A1B9A, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.ILLICIT,
        subjectiveTags = listOf("共情剂", "兴奋剂"),
        effectTags = listOf("血清素受体激动剂", "多巴胺再摄取抑制剂"),
        moleculeTag = "氨基丙基苯并呋喃类", halfLifeHours = 10.0, bioavailabilityPct = 50,
        route = Route.ORAL, tmaxHours = 2.0,
        description = "「苯并呋喃版的摇头丸」，作用时间长，体温升高与脱水风险显著。",
        attributes = listOf(DrugAttribute("兴奋", 3, 5), DrugAttribute("欣快", 3, 5), DrugAttribute("共情", 3, 5), DrugAttribute("致幻", 1, 3), DrugAttribute("依赖性", -1, 1)),
        rules = listOf(
            DoseEffectRule(30.0.toBigDecimal(), "欣快", 1),
            DoseEffectRule(60.0.toBigDecimal(), "欣快", 2),
            DoseEffectRule(60.0.toBigDecimal(), "恍惚", 1),
            DoseEffectRule(90.0.toBigDecimal(), "解离", 1),
            DoseEffectRule(120.0.toBigDecimal(), "谵妄", 1),
        ),
    )

    // ===== v4：被滥用药物 =====




    val entries: List<DrugInfo> = listOf(
        ondansetron, granisetron, aprepitant, naloxone, flumazenil,
        cyproheptadine, epinephrine, physostigmine, atropine, esketamine,
        citicoline, phosphatidylserine, norfluoxetine, nordazepam, odesmethyltramadol,
        phenytoin, cefoperazone, metronidazole, clarithromycin, erythromycin,
        ciprofloxacin, rifampin, linezolid, isoniazid, ketoconazole,
        propranolol, metoprolol, bisoprolol, clonidine, methyldopa,
        reserpine, hydrochlorothiazide, indapamide, nifedipine, amlodipine,
        enalapril, losartan, propofol, suvorexant, lemborexant,
        daridorexant, mdpv, alphaPvp, twoCB, nbome,
        apb,
    )
}

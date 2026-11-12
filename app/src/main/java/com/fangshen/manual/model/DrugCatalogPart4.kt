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
    )

    private val metronidazole = d(
        key = "metronidazole", name = "甲硝唑", colorArgb = 0xFF80CBC4, tabletDose = 200.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("双硫仑样反应药", "硝基咪唑类"),
        moleculeTag = "硝基咪唑类", halfLifeHours = 8.0, bioavailabilityPct = 99, route = Route.ORAL,
        tmaxHours = 2.0,
        inhibitors = listOf("CYP3A4"),
    )

    private val clarithromycin = d(
        key = "clarithromycin", name = "克拉霉素", colorArgb = 0xFF80CBC4, tabletDose = 250.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("大环内酯类", "肝药酶抑制剂"),
        moleculeTag = "大环内酯类", halfLifeHours = 4.0, bioavailabilityPct = 55, route = Route.ORAL,
        tmaxHours = 3.0,
        inhibitors = listOf("CYP3A4"),
    )

    private val erythromycin = d(
        key = "erythromycin", name = "红霉素", colorArgb = 0xFF80CBC4, tabletDose = 250.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("大环内酯类", "肝药酶抑制剂"),
        moleculeTag = "大环内酯类", halfLifeHours = 1.5, bioavailabilityPct = 35, route = Route.ORAL,
        tmaxHours = 2.5,
        inhibitors = listOf("CYP3A4"),
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
    )

    private val rifampin = d(
        key = "rifampin", name = "利福平", colorArgb = 0xFF80CBC4, tabletDose = 300.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("利福霉素类", "肝药酶诱导剂"),
        moleculeTag = "利福霉素类", halfLifeHours = 3.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 2.0,
        inducers = listOf("CYP3A4", "CYP2C9", "CYP2C19", "CYP2B6", "CYP2C8"),
    )

    private val linezolid = d(
        key = "linezolid", name = "利奈唑胺", colorArgb = 0xFF80CBC4, tabletDose = 600.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("单胺氧化酶抑制剂", "噁唑烷酮类"),
        moleculeTag = "噁唑烷酮类", halfLifeHours = 6.0, bioavailabilityPct = 100, route = Route.ORAL,
        tmaxHours = 1.5,
        inhibitors = listOf("MAO-A"),
    )

    private val isoniazid = d(
        key = "isoniazid", name = "异烟肼", colorArgb = 0xFF80CBC4, tabletDose = 300.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("抗感染药"),
        effectTags = listOf("单胺氧化酶抑制剂", "抗结核药"),
        moleculeTag = "异烟酸肼类", halfLifeHours = 3.0, bioavailabilityPct = 90, route = Route.ORAL,
        tmaxHours = 1.0,
        inhibitors = listOf("MAO-A"),
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

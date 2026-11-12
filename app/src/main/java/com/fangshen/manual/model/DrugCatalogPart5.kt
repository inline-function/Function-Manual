package com.fangshen.manual.model

import java.math.BigDecimal

/**
 * v5：激素类与内分泌相关药物（雌二醇、戊酸雌二醇、螺内酯、胰岛素、环丙孕酮、
 * 左甲状腺素、泼尼松、地塞米松、睾丸酮）。
 * 这类药物以替代/调节内分泌为主，不纳入成瘾引擎；属性条仅作展示。
 */
object DrugCatalogPart5 {

    private val estradiol = d(
        key = "estradiol", name = "雌二醇", colorArgb = 0xFFE91E63, tabletDose = 1.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("激素类", "雌激素"),
        effectTags = listOf("雌激素受体激动剂"),
        moleculeTag = "雌激素类", halfLifeHours = 14.0, bioavailabilityPct = 3, route = Route.ORAL,
        tmaxHours = 5.0,
        substrates = listOf("CYP3A4"),
        attributes = listOf(
            DrugAttribute("雌激素补充", 4, 5),
            DrugAttribute("缓解绝经期症状", 3, 5),
            DrugAttribute("抗骨质疏松", 3, 4),
            DrugAttribute("血栓风险", -1, 2),
        ),
        description = "雌二醇为主要内源性雌激素，用于绝经期综合征、卵巢功能不足与骨质疏松的替代治疗；口服经肝脏首过效应灭活明显，生物利用度仅约百分之三，透皮或肌注可提高利用度，可致乳腺胀痛、水肿与情绪波动，妊娠、乳腺癌、血栓病史与原因不明阴道出血者禁用。",
    )

    private val estradiol_valerate = d(
        key = "estradiol_valerate", name = "戊酸雌二醇", colorArgb = 0xFFEC407A, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("激素类", "雌激素"),
        effectTags = listOf("雌激素受体激动剂"),
        moleculeTag = "雌激素类", halfLifeHours = 72.0, bioavailabilityPct = 100, route = Route.IM,
        attributes = listOf(
            DrugAttribute("雌激素补充", 4, 5),
            DrugAttribute("缓解绝经期症状", 3, 5),
            DrugAttribute("血栓风险", -1, 2),
        ),
        description = "戊酸雌二醇为雌二醇的长效酯类前体，肌注后缓慢水解释放雌二醇，用于绝经期综合征与卵巢功能不足的替代治疗；油溶液肌注作用持久，可维持数周，常见乳房胀痛、水肿与月经样出血，长期使用须定期评估乳腺与子宫内膜风险。",
    )

    private val spironolactone = d(
        key = "spironolactone", name = "螺内酯", colorArgb = 0xFF26A69A, tabletDose = 20.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("利尿剂", "抗雄激素药"),
        effectTags = listOf("醛固酮受体拮抗剂"),
        moleculeTag = "螺甾内酯类", halfLifeHours = 1.5, bioavailabilityPct = 65, route = Route.ORAL,
        tmaxHours = 3.0,
        substrates = listOf("CYP3A4"),
        attributes = listOf(
            DrugAttribute("保钾利尿", 4, 5),
            DrugAttribute("抗高血压", 3, 4),
            DrugAttribute("抗雄激素", 3, 5),
            DrugAttribute("高钾血症", -1, 3),
        ),
        description = "螺内酯竞争性拮抗醛固酮、保钾利尿，并具抗雄激素作用，用于水肿、高血压、原发性醛固酮增多症与女性多毛、痤疮；其活性代谢物坎利酮半衰期长、作用延迟数日出现，最常见高钾血症，肾功能不全与补钾联用时可致命，男性乳房发育与性功能障碍呈剂量相关。",
    )

    private val insulin = d(
        key = "insulin", name = "胰岛素", colorArgb = 0xFF42A5F5, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("降糖药", "激素类"),
        effectTags = listOf("胰岛素受体激动剂"),
        moleculeTag = "多肽类", halfLifeHours = 2.0, bioavailabilityPct = 70, route = Route.SC,
        tmaxHours = 1.5,
        attributes = listOf(
            DrugAttribute("降血糖", 4, 5),
            DrugAttribute("低血糖风险", -1, 4),
        ),
        description = "胰岛素促进葡萄糖摄取利用与糖原合成，为1型糖尿病与危重高血糖的必需降糖药，皮下注射给药；1国际单位约等于0.0347毫克，本手册以毫克计曲线；过量致低血糖（心悸、出汗、意识丧失），须随身携带糖块，注射部位轮换并监测血糖，肾功能不全与呕吐进食减少者须减量防低血糖。",
    )

    private val cyproterone = d(
        key = "cyproterone", name = "环丙孕酮", colorArgb = 0xFF7E57C2, tabletDose = 50.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("激素类", "抗雄激素药"),
        effectTags = listOf("雄激素受体拮抗剂", "孕激素受体激动剂"),
        moleculeTag = "孕激素类", halfLifeHours = 38.0, bioavailabilityPct = 100, route = Route.ORAL,
        tmaxHours = 3.0,
        attributes = listOf(
            DrugAttribute("抗雄激素", 4, 5),
            DrugAttribute("抑制性欲", 2, 4),
            DrugAttribute("肝毒性", -1, 2),
        ),
        description = "环丙孕酮为强效孕激素兼抗雄激素药，用于前列腺癌、性早熟、女性多毛症与重度痤疮；可抑制性欲与精子生成，男性乳房发育常见，大剂量可致肝损伤与血栓，须定期查肝功能，妊娠与哺乳期、有血栓或肝病史者禁用。",
    )

    private val levothyroxine = d(
        key = "levothyroxine", name = "左甲状腺素", colorArgb = 0xFF9CCC65, tabletDose = 50.toBigDecimal(),
        doseUnit = DoseUnit.MCG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("激素类", "甲状腺药"),
        effectTags = listOf("甲状腺激素受体激动剂"),
        moleculeTag = "甲状腺激素类", halfLifeHours = 168.0, bioavailabilityPct = 60, route = Route.ORAL,
        tmaxHours = 3.0,
        attributes = listOf(
            DrugAttribute("甲状腺激素替代", 4, 5),
            DrugAttribute("促代谢", 2, 4),
            DrugAttribute("心悸风险", -1, 2),
        ),
        description = "左甲状腺素为合成的甲状腺素（T4），用于甲状腺功能减退症的替代治疗，半衰期约七天、起效缓慢、稳态需数周；剂量不足甲减症状不除，过量则心悸、怕热、失眠与心律失常，须空腹服用，与钙片、铁剂、豆制品间隔四小时以上以免吸收受阻，妊娠期需要量通常增加。",
    )

    private val prednisone = d(
        key = "prednisone", name = "泼尼松", colorArgb = 0xFFFFA726, tabletDose = 5.0.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("糖皮质激素", "抗炎药"),
        effectTags = listOf("糖皮质激素受体激动剂"),
        moleculeTag = "糖皮质激素类", halfLifeHours = 1.0, bioavailabilityPct = 70, route = Route.ORAL,
        tmaxHours = 2.0,
        substrates = listOf("CYP3A4"),
        attributes = listOf(
            DrugAttribute("抗炎", 4, 5),
            DrugAttribute("免疫抑制", 4, 5),
            DrugAttribute("抗过敏", 3, 5),
            DrugAttribute("升血糖", -1, 3),
            DrugAttribute("骨质疏松风险", -1, 3),
        ),
        description = "泼尼松为中效糖皮质激素，在肝内活化为泼尼松龙，用于过敏、自身免疫、哮喘与炎症性疾病；长期使用可致向心性肥胖、高血糖、骨质疏松、胃溃疡与免疫力下降，须逐渐减量停药以防肾上腺皮质功能不全，用药期间补钙、监测血糖，活动性感染与消化性溃疡者慎用。",
    )

    private val dexamethasone = d(
        key = "dexamethasone", name = "地塞米松", colorArgb = 0xFFFF7043, tabletDose = 0.75.toBigDecimal(),
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("糖皮质激素", "抗炎药"),
        effectTags = listOf("糖皮质激素受体激动剂"),
        moleculeTag = "糖皮质激素类", halfLifeHours = 3.5, bioavailabilityPct = 80, route = Route.ORAL,
        tmaxHours = 1.5,
        substrates = listOf("CYP3A4"),
        attributes = listOf(
            DrugAttribute("抗炎", 4, 5),
            DrugAttribute("免疫抑制", 4, 5),
            DrugAttribute("消肿降颅压", 3, 5),
            DrugAttribute("升血糖", -1, 3),
            DrugAttribute("精神兴奋", -1, 3),
        ),
        description = "地塞米松为长效强效糖皮质激素，抗炎作用约为泼尼松的六至七倍，用于严重过敏、自身免疫病、脑水肿与抗肿瘤辅助治疗；长期使用可致高血糖、骨质疏松、肌无力与精神症状（欣快或谵妄），需逐渐减量停药，与利福平、苯妥英等肝酶诱导剂联用其血药浓度下降。",
    )

    private val testosterone = d(
        key = "testosterone", name = "睾酮", colorArgb = 0xFF5C6BC0, tabletDose = null,
        doseUnit = DoseUnit.MG, managementClass = ManagementClass.RX,
        subjectiveTags = listOf("激素类", "雄激素"),
        effectTags = listOf("雄激素受体激动剂"),
        moleculeTag = "雄激素类", halfLifeHours = 1.5, bioavailabilityPct = 10, route = Route.TRANSDERMAL,
        tmaxHours = 8.0,
        substrates = listOf("CYP3A4"),
        attributes = listOf(
            DrugAttribute("雄激素替代", 4, 5),
            DrugAttribute("促肌肉合成", 3, 5),
            DrugAttribute("提升性欲", 2, 4),
            DrugAttribute("痤疮", -1, 3),
        ),
        description = "睾丸酮为主要雄激素，用于男性性腺功能减退症的替代治疗；口服经肝脏首过灭活严重、生物利用度低，临床以透皮凝胶或肌注为主；可致痤疮、脱发、前列腺增生与红细胞增多，妊娠、前列腺癌与男性乳腺癌禁用，运动员滥用其促合成作用属兴奋剂行为。",
    )

    val entries: List<DrugInfo> = listOf(
        estradiol, estradiol_valerate, spironolactone, insulin, cyproterone,
        levothyroxine, prednisone, dexamethasone, testosterone,
    )
}

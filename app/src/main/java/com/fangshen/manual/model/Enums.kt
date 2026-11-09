package com.fangshen.manual.model

/** 管理等级（药物监督管理分类） */
enum class ManagementClass(val label: String) {
    HEALTH_FOOD("保健食品"),
    OTC_B("乙类非处方药"),
    OTC_A("甲类非处方药"),
    RX("处方药"),
    CONTROLLED_II("第二类精神药品"),
    CONTROLLED_I("第一类精神药品"),
    NARCOTIC("麻醉药品"),
    ILLICIT("毒品（非医用用途精麻药品）"),
    HAZARDOUS("危险化学品"),
    RESEARCH("研究化学品"),
    SPECIAL("特殊（烟酒槟榔等）"),
    NONE("不适用");

    companion object {
        fun fromName(name: String?): ManagementClass =
            entries.firstOrNull { it.name == name } ?: RX
    }
}

/** 给药途径 */
enum class Route(val label: String) {
    ORAL("口服"),
    SUBLINGUAL("舌下含服"),
    BUCCAL("颊黏膜给药"),
    INSUFFLATION("鼻吸"),
    INHALATION("吸入"),
    IV("静脉注射"),
    IM("肌肉注射"),
    SC("皮下注射"),
    RECTAL("直肠给药"),
    TRANSDERMAL("透皮贴剂"),
    TOPICAL("局部外用"),
    EYE_DROP("滴眼"),
    EAR_DROP("滴耳"),
    NASAL_SPRAY("鼻腔喷雾"),
    OTHER("其他");

    companion object {
        fun fromName(name: String?): Route = entries.firstOrNull { it.name == name } ?: ORAL
    }
}

/** 剂量单位（血药浓度曲线按此分图） */
enum class DoseUnit(val label: String, val shortLabel: String, val axisLabel: String) {
    MCG("微克", "µg", "µg/L"),
    MG("毫克", "mg", "mg/L"),
    G("克", "g", "g/L");

    companion object {
        fun fromName(name: String?): DoseUnit = entries.firstOrNull { it.name == name } ?: MG
    }
}

/** 台账条目类型 */
enum class EntryType(val label: String, val tag: String) {
    DOSE("服药记录", "服药"),
    NOTE("笔记", "笔记"),
    PHOTO("照片", "照片"),
    VOICE("语音", "语音"),
    FILE("文件", "文件"),
    SCHEDULE("日程", "日程");

    companion object {
        fun fromName(name: String?): EntryType = entries.firstOrNull { it.name == name } ?: NOTE
    }
}

/** 效应急迫性 */
enum class EffectUrgency(val label: String) { ACUTE("急性"), CHRONIC("慢性") }

/** 效应主观评价 */
enum class EffectValence(val label: String) { POSITIVE("正面"), NEGATIVE("负面"), NEUTRAL("中立") }

/** 效应程度（由权重换算） */
enum class EffectSeverity(val label: String) {
    MILD("轻度"), MODERATE("中度"), SEVERE("重度");

    companion object {
        // 权重分级：1-3 轻度、3-6 中度、大于等于 7 重度
        fun fromWeight(weight: Int): EffectSeverity = when {
            weight >= 7 -> SEVERE
            weight >= 3 -> MODERATE
            else -> MILD
        }
    }
}

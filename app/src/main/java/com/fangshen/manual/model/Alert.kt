package com.fangshen.manual.model

/**
 * 预警规则：在联用药物或特定画像条件下，给某条效应额外增加权重。
 *
 * 药物匹配值 [drug] / [withDrug] 可以是：药物名（如「吗啡」）、
 * 药物的效应标签（如「选择性血清素再摄取抑制剂」）或主观标签（如「镇痛剂」）。
 * 画像匹配值 [withProfile] 可以是：障碍类别名（如「情感障碍」）或具体条目名（如「双相I型障碍」）。
 */
data class AlertRule(
    val id: Long,
    /** 规则名称，如「阿片+苯二氮卓类呼吸抑制」 */
    val name: String,
    val enabled: Boolean = true,
    /** 主药物匹配 */
    val drug: String,
    /** 联用条件：另一味药物的匹配串（画像规则为 null） */
    val withDrug: String? = null,
    /** 画像条件：类别或具体条目名（联用规则为 null） */
    val withProfile: String? = null,
    /** 触发后增加权重的效应名 */
    val effect: String,
    /** 增加的权重 */
    val weight: Int,
    // —— v5 画像条件（可叠加；为 null 表示不限制）——
    /** 性别条件："MALE" 或 "FEMALE" */
    val conditionSex: String? = null,
    /** 体重下限（kg，含） */
    val conditionWeightMin: Int? = null,
    /** 体重上限（kg，含） */
    val conditionWeightMax: Int? = null,
) {
    /** 是否带有画像条件 */
    val hasCondition: Boolean get() =
        conditionSex != null || conditionWeightMin != null || conditionWeightMax != null

    val isProfileRule: Boolean get() = withProfile != null
}

/** 药物是否匹配某个规则条件（名称、主观标签或效应标签任一相等） */
fun DrugInfo.matches(value: String): Boolean =
    name == value || value in subjectiveTags || value in effectTags
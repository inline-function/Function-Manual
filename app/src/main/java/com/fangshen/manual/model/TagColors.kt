package com.fangshen.manual.model

/**
 * 主观标签 → 颜色（ARGB），用于药物名称旁的小竖条与血药浓度曲线。
 * 颜色由药物的**第一个主观标签**决定；若标签不在内置对照表中（含自定义标签），
 * 小竖条与曲线使用黑色。
 */
object SubjectiveTagColors {
    private val COLORS: Map<String, Long> = mapOf(
        "抗抑郁剂" to 0xFFFFD700L,   // 金色
        "抗焦虑剂" to 0xFF4CAF50L,   // 绿色
        "抗强迫剂" to 0xFFFDD835L,   // 黄色
        "抗多动剂" to 0xFFFF9800L,   // 橙色
        "中枢抑制剂" to 0xFF1976D2L, // 蓝色
        "中枢兴奋剂" to 0xFFE53935L, // 红色
        "心境稳定剂" to 0xFF00BCD4L, // 青色
        "抗精神病剂" to 0xFFA5D6A7L, // 淡绿色
        "解离剂" to 0xFF90CAF9L,     // 淡蓝色
        "迷幻剂" to 0xFF8E24AAL,     // 紫色
        "谵妄剂" to 0xFF795548L,     // 棕色
        "动机增强剂" to 0xFF9E9D24L, // 黄绿色
        "认知增强剂" to 0xFFF57F17L, // 深黄色
        "梦境增强剂" to 0xFFE91E63L, // 粉红色
        "记忆增强剂" to 0xFF00838FL, // 深青色
        "记忆抑制剂" to 0xFF80DEEAL, // 淡青色
        "反解离剂" to 0xFFFFCC80L,   // 浅橙色
        "共情剂" to 0xFFF48FB1L,     // 粉色
        "镇静剂" to 0xFF607D8BL,     // 蓝灰色
        "催眠剂" to 0xFF4527A0L,     // 深紫色
        "镇痛剂" to 0xFFEF9A9AL,     // 淡红色
        "逆转剂" to 0xFFFFFFFFL,     // 白色
        "欣快剂" to 0xFFD81B60L,       // 品红色
    )

    /** 目录中含义相同但写法不同的标签，归一到内置对照表 */
    private val ALIASES: Map<String, String> = mapOf(
        "致幻剂" to "迷幻剂",
        "镇静催眠剂" to "催眠剂",
    )

    /** 内置主观标签的固定顺序（用于药物分组排序） */
    val BUILT_IN_ORDER: List<String> = COLORS.keys.toList()

    /** 标签归一化（同义写法合并），未匹配的按原样返回 */
    fun canonicalOf(tag: String): String = ALIASES[tag] ?: tag

    /** 标签颜色；不在表中返回黑色（null 标签也视为黑色） */
    fun colorOf(tag: String?): Long {
        if (tag.isNullOrBlank()) return 0xFF000000L
        return COLORS[ALIASES[tag] ?: tag] ?: 0xFF000000L
    }

    /** 药物颜色：由第一个主观标签决定，其余为黑 */
    fun colorOf(drug: DrugInfo): Long = colorOf(drug.subjectiveTags.firstOrNull())
}

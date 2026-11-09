package com.fangshen.manual.model

/**
 * 效应定义（领域知识）。
 *
 * @param name 名称
 * @param urgency 急性 / 慢性
 * @param valence 正面 / 负面 / 中立
 * @param inverseOf 反效应名称（与该效应权重互相抵消）
 * @param hidden 隐效应：不单独显示，仅作为某效应的反效应存在
 * @param neurotransmitter 引起该效应的神经递质（为空表示不由神经递质机制引起）
 * @param pack 所属效应包（为空表示独立效应）
 */
data class EffectDef(
    val name: String,
    val urgency: EffectUrgency,
    val valence: EffectValence,
    val inverseOf: String? = null,
    val hidden: Boolean = false,
    val neurotransmitter: String? = null,
    val pack: String? = null,
    val description: String = "",
)

/** 神经递质名称词汇表 */
object Neurotransmitters {
    const val SEROTONIN = "血清素"
    const val DOPAMINE = "多巴胺"
    const val NOREPINEPHRINE = "去甲肾上腺素"
    const val GLUTAMATE = "谷氨酸"
    const val GABA = "GABA"
    const val ACETYLCHOLINE = "乙酰胆碱"
    const val HISTAMINE = "组胺"
    const val ENDORPHIN = "内啡肽"

    /** 常见酶（CYP 家族） */
    val ENZYMES = listOf(
        "CYP1A2", "CYP2B6", "CYP2C8", "CYP2C9", "CYP2C19", "CYP2D6", "CYP2E1", "CYP3A4", "CYP3A5",
        "MAO-A", "MAO-B",
    )

    val ALL = listOf(
        SEROTONIN, DOPAMINE, NOREPINEPHRINE, GLUTAMATE, GABA, ACETYLCHOLINE, HISTAMINE, ENDORPHIN,
    )
}

/**
 * 神经递质偏差 → 效应：神经递质权重偏离基准过多会引发效应。
 * 键为神经递质名称，值为 (偏高时的效应名, 偏低时的效应名)。
 */
val NEURO_DEVIATION_EFFECTS: Map<String, Pair<String?, String?>> = mapOf(
    Neurotransmitters.SEROTONIN to ("血清素综合征" to "抑郁"),
    Neurotransmitters.DOPAMINE to ("欣快" to "抑郁"),
    Neurotransmitters.NOREPINEPHRINE to ("心动过速" to "嗜睡"),
    Neurotransmitters.GLUTAMATE to ("焦虑" to "解离"),
    Neurotransmitters.GABA to ("镇静" to "焦虑"),
    Neurotransmitters.ACETYLCHOLINE to (null to "谵妄"),
    Neurotransmitters.HISTAMINE to (null to "嗜睡"),
    Neurotransmitters.ENDORPHIN to ("愉悦" to null),
)

/** 效应目录：全部领域内效应、效应包及其成员。 */
object EffectCatalog {

    val PACKS: Map<String, List<String>> = mapOf(
        "血清素综合征" to listOf("恶心", "嗜睡", "心动过速", "谵妄", "出汗", "寒战", "肌阵挛", "高血压"),
        "抗胆碱能综合征" to listOf("口干", "便秘", "尿潴留", "视物模糊", "心动过速", "嗜睡", "谵妄", "潮红"),
        "阿片类中毒" to listOf("瞳孔缩小", "呼吸抑制", "镇静", "欣快", "便秘"),
    )

    private val defs: List<EffectDef> = listOf(
        // —— 正面效应 ——
        EffectDef(
            "愉悦", EffectUrgency.ACUTE, EffectValence.POSITIVE,
            inverseOf = "抑郁", neurotransmitter = Neurotransmitters.DOPAMINE,
            description = "这是一种由内而外散发的积极情绪体验，标志着抑郁情绪的显著缓解。它不仅仅是简单的开心，更是一种精神重压减轻后的释然与轻快感。这种主观愉悦能帮助您重新找回对生活的热情与动力，是抗抑郁治疗中极为重要的正向心理反馈。",
        ),
        EffectDef(
            "自信", EffectUrgency.ACUTE, EffectValence.POSITIVE,
            description = "这是一种对自我能力与价值深信不疑的笃定体验。您会明显感到做事更有把握，面对他人与挑战时不再轻易退缩，内心升腾起一种“我能行”的力量感。它能让低落时被压垮的自我评价重新回升，是心理状态改善的重要信号。",
        ),
        EffectDef(
            "欣快", EffectUrgency.ACUTE, EffectValence.POSITIVE,
            neurotransmitter = Neurotransmitters.DOPAMINE,
            description = "这是一种由多巴胺显著上升带来的、极为强烈的“爽”感与精神振奋。它比普通的愉悦更为猛烈，能让人在瞬间体验到极致的满足与生理性快感。这种强烈的正向反馈能迅速将人从低落中拔起，带来一种巅峰般的积极情绪体验，是药物治疗中极具冲击力的正向感受。",
        ),
        EffectDef(
            "乐观", EffectUrgency.CHRONIC, EffectValence.POSITIVE,
            inverseOf = "抑郁",
            description = "这是一种看向未来时总带着期待的明亮心境。在它的作用下，您会更容易相信好事情终将发生，即便身处低谷也能保留一丝希望的光亮。这种长期倾向能为漫长的康复之路提供持续的牵引力。",
        ),
        EffectDef(
            "镇静", EffectUrgency.ACUTE, EffectValence.POSITIVE,
            inverseOf = "焦虑", neurotransmitter = Neurotransmitters.GABA,
            description = "这是一种与“焦虑”完全相反的安宁体验。它能有效抚平内心的惶恐、不安与过度警觉，就像给焦躁的神经系统盖上了一层温暖的毯子。在这种效应下，您的主观感受会变得平稳、安定，心跳与呼吸也随之平缓。它能帮助您从焦灼的状态中抽离，重新获得内心的踏实与从容。",
        ),
        EffectDef(
            "释然", EffectUrgency.ACUTE, EffectValence.POSITIVE,
            inverseOf = "强迫",
            description = "这是一种精神重负被彻底卸下后的轻松体验，它与“强迫”带来的紧绷、纠结和无法自控截然相反。当这种效应产生时，您会感到脑海中的执念与压力烟消云散，内心恢复久违的平静与宽慰。它能让紧绷的神经彻底放松，帮助您找回顺其自然、不再被内耗困扰的松弛感。",
        ),
        EffectDef(
            "镇吐", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            inverseOf = "恶心", hidden = true,
            description = "隐效应：抑制恶心，与恶心互相抵消，不单独显示。",
        ),
        EffectDef(
            "专注", EffectUrgency.ACUTE, EffectValence.POSITIVE,
            inverseOf = "分心",
            description = "这是一种与“注意力涣散”截然相反的清晰状态。当大脑的认知功能得到改善，您会主观感受到思绪不再飘忽不定，而是能够平稳地聚焦于当下。这种体验能帮助您有效屏蔽外界的纷扰，轻松进入心流状态，做事更有效率，重新找回对自我意识和生活节奏的掌控感。",
        ),
        EffectDef(
            "善记", EffectUrgency.ACUTE, EffectValence.POSITIVE,
            inverseOf = "健忘",
            description = "这是一种大脑如同海绵般高效吸收的畅快体验。主观上会感到思维清晰敏捷，信息存储牢固且提取迅速。无论是学习新知识还是回忆过往细节，都显得游刃有余。这种记忆力提升能带来强烈的掌控感与自信心，让人在应对日常事务时感到轻松且得心应手。",
        ),
        // —— 负面效应 ——
        EffectDef(
            "抑郁", EffectUrgency.CHRONIC, EffectValence.NEGATIVE,
            inverseOf = "乐观",
            description = "这是一种情绪陷入深重泥沼的灰暗体验。它像一层厚重的乌云笼罩在心头，让主观感受变得沉重、迟缓且缺乏生机。在这种状态下，曾经感兴趣的事物都会失去色彩，内心充满无力感与沮丧，仿佛被困在情绪的低谷中，难以凭借自身的力量轻松挣脱。",
        ),
        EffectDef(
            "焦虑", EffectUrgency.CHRONIC, EffectValence.NEGATIVE,
            inverseOf = "镇静",
            description = "这是一种内心被无形之手紧攥的悬空感。它让人处于过度警觉与惶恐之中，主观上总觉得有不好的事情将要发生，却又难以名状。这种体验伴随着内心的焦躁不安与思绪的翻江倒海，仿佛一根紧绷的弦随时可能断裂，让人无法获得片刻的踏实与宁静。",
        ),
        EffectDef(
            "强迫", EffectUrgency.CHRONIC, EffectValence.NEGATIVE,
            inverseOf = "释然",
            description = "这是一种思绪与行为被强行绑架的拉扯感。主观上会反复涌现出不想要的念头，或者被迫去执行某些无意义的动作，且难以自控。这种体验带来的是深度的精神内耗与纠结，仿佛大脑里有个声音在不停施压，让人陷入想停却停不下来的痛苦循环。",
        ),
        EffectDef(
            "恶心", EffectUrgency.ACUTE, EffectValence.NEGATIVE,
            inverseOf = "镇吐", pack = "血清素综合征",
            description = "这是一种胃部翻江倒海、强烈排斥的不适体验。主观上会感到上腹部沉重、胃液翻涌，伴随着一阵阵想要呕吐的冲动。这种生理上的抗议不仅让人食欲全无，还会引起全身的乏力与头晕，仿佛身体在试图将某种不适感强行排出体外。",
        ),
        EffectDef(
            "谵妄", EffectUrgency.ACUTE, EffectValence.NEGATIVE,
            neurotransmitter = Neurotransmitters.ACETYLCHOLINE,
            description = "这是一种大脑陷入混乱与失去方向的失控体验。主观上会感到思维变得毫无逻辑、支离破碎，外界的声音与画面可能变得扭曲。情绪容易起伏不定，甚至可能出现错觉或幻觉，让人分不清现实与虚幻，内心充满极度的迷茫、惊恐与不知所措。",
        ),
        EffectDef(
            "自杀冲动", EffectUrgency.ACUTE, EffectValence.NEGATIVE,
            description = "这是一种出现伤害自己念头或冲动的危险状态，必须立即寻求专业帮助。它可能毫无征兆地袭来，将人拖入极度绝望的深渊，主观上仿佛被死亡的念头紧紧缠住。请务必立刻联系医生、家人或急救机构。",
        ),
        EffectDef(
            "消极", EffectUrgency.ACUTE, EffectValence.NEGATIVE,
            inverseOf = "向生",
            description = "需高度警惕的异常状态，常现于用药初期。与纯粹抑郁不同，患者可能在体验到某种“愉悦”或精力恢复的同时，涌出强烈的自我毁灭动机。这种“虚假好转”伴随的危险冲动极具欺骗性，仿佛被一种无法抗拒的绝望念头驱使，必须立即寻求专业干预。",
        ),
        EffectDef(
            "分心", EffectUrgency.CHRONIC, EffectValence.NEGATIVE,
            inverseOf = "专注",
            description = "这是一种思绪如同脱缰野马般四处乱窜的失控感。主观上很难将精神聚焦在某一件事情上，外界的一点点风吹草动都能轻易带走注意力。这种体验让人感到大脑像是一团迷雾，做事频频走神、效率低下，难以进入专注状态，从而产生一种深深的无力与挫败感。",
        ),
        EffectDef(
            "口干", EffectUrgency.ACUTE, EffectValence.NEGATIVE,
            pack = "抗胆碱能综合征",
            description = "这是一种唾液分泌明显减少的不适体验。口腔内总是干涩发黏，吞咽与说话都变得费劲，即使不停喝水也只能短暂缓解。它常是抗胆碱能药物作用下的身体抗议。",
        ),
        EffectDef(
            "便秘", EffectUrgency.CHRONIC, EffectValence.NEGATIVE,
            pack = "抗胆碱能综合征",
            description = "这是一种排便变得困难而迟缓的困扰。粪便在肠道中停留过久，每次如厕都需要额外用力，腹部也随之胀满沉重。长期的肠道滞涩会让人坐立不安，影响食欲与心情。",
        ),
        EffectDef(
            "尿潴留", EffectUrgency.ACUTE, EffectValence.NEGATIVE,
            pack = "抗胆碱能综合征",
            description = "这是一种膀胱充盈却无法顺畅排出的焦急体验。小腹胀痛难忍，排尿时只能断断续续，甚至完全解不出来，主观上仿佛水路被什么东西堵住了。这是需要尽快处理的药物副作用。",
        ),
        EffectDef(
            "视物模糊", EffectUrgency.ACUTE, EffectValence.NEGATIVE,
            pack = "抗胆碱能综合征",
            description = "这是一种看东西发虚、对不准焦的朦胧体验。眼前的文字与轮廓都像蒙上了一层薄雾，越是想看清越是吃力。它让人阅读困难、行动迟疑，是药物影响眼睛调节能力的常见表现。",
        ),
        EffectDef(
            "高血压", EffectUrgency.ACUTE, EffectValence.NEGATIVE,
            pack = "血清素综合征",
            description = "这是一种血压异常升高的危险状态。头胀、心悸、后颈发紧随之而来，主观上能感到血液在血管里猛烈冲撞。若不及时干预，可能对心脑血管造成持续伤害。",
        ),
        EffectDef(
            "呼吸抑制", EffectUrgency.ACUTE, EffectValence.NEGATIVE,
            pack = "阿片类中毒",
            description = "这是一种呼吸变浅变慢、仿佛被扼住喉咙的危急体验。每一次吸气都变得短促而费力，胸闷感不断加重，主观上像有什么重物压在胸口。这是阿片类药物过量最危险的信号，必须立即就医。",
        ),
        EffectDef(
            "失眠", EffectUrgency.CHRONIC, EffectValence.NEGATIVE,
            inverseOf = "嗜睡",
            description = "这是一种入睡或维持睡眠格外困难的折磨。夜深人静时头脑反而越来越清醒，辗转反侧直到天亮，第二天的疲惫又被原样累积。长期的睡眠缺口会一点点蚕食白天的精力与情绪。",
        ),
        // —— 中立效应 ——
        EffectDef(
            "解离", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            neurotransmitter = Neurotransmitters.GLUTAMATE, inverseOf = "敏知",
            description = "这是一种与现实世界“断开连接”的抽离感。仿佛在自己和外界之间隔了一层透明的玻璃，主观上会觉得周围的环境变得不真实，自己的思想、身体甚至声音都像是不属于自己，像是在旁观一场关于自己的电影，带来一种恍惚的隔离感。",
        ),
        EffectDef(
            "恍惚", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            inverseOf = "敏知",
            description = "这是一种意识像蒙上了毛玻璃般的朦胧体验。大脑的反应变得迟缓，注意力难以集中，对周围正在发生的事情感觉模模糊糊、似真似幻。在这种状态下，人容易发呆、走神，时间感变得混乱，仿佛整个人处于半梦半醒之间，缺乏清醒的掌控感。",
        ),
        EffectDef(
            "敏知", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            description = "这是一种五感被无限放大的沉浸式体验。就像世界的色彩、声音与触感都被调高了饱和度：味觉变得丰富鲜明，听觉能捕捉到微小的细节，触觉也更为敏锐。这种体验能让您更深刻地感受到生活的质感与生机，但有时也可能因为信息过载而显得过于刺眼或喧闹。",
        ),
        EffectDef(
            "心动过速", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            neurotransmitter = Neurotransmitters.NOREPINEPHRINE, pack = "血清素综合征",
            description = "这是一种心脏明显加快跳动的体验。胸口能感到有力的搏动，有时连耳膜都跟着嗡嗡作响，主观上像身体被调到了更高的档位。它本身不一定致命，却常是更严重反应的前奏。",
        ),
        EffectDef(
            "嗜睡", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            inverseOf = "失眠", pack = "抗胆碱能综合征",
            description = "这是一种白天也无法抵挡的浓重困意。眼皮像坠了铅，做事时意识一阵阵发沉，随时都可能栽进睡眠里。连续的瞌睡会让日常安排彻底停摆。",
        ),
        EffectDef(
            "出汗", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            pack = "血清素综合征",
            description = "这是一种不受控制地大量出汗的体验。稍一活动就浑身湿透，手心脚心也在不停地渗汗，主观上像身体的散热开关被卡在了最大档。它既不舒服，也时刻提醒着身体的异常。",
        ),
        EffectDef(
            "寒战", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            pack = "血清素综合征",
            description = "这是一种不自主地发抖打颤的体验。牙齿打架、肌肉一阵阵收缩，明明盖着被子也暖不过来，主观上像被凉意从里向外渗透。它常是体温调节失衡的信号。",
        ),
        EffectDef(
            "肌阵挛", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            pack = "血清素综合征",
            description = "这是一种肌肉突然短暂抽动的惊悚体验。手脚或躯干毫无先兆地猛抽一下，动作幅度不大却让人心头一紧，主观上像身体被什么拽了一下。它往往在意识清醒时发生，提示神经系统异常。",
        ),
        EffectDef(
            "潮红", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            pack = "抗胆碱能综合征",
            description = "这是一种皮肤突然阵阵发热发红的体验。热浪从面部蔓延到脖颈，肤色肉眼可见地变红，主观上像有一团火在皮肤下烧。它来去突然，常伴随心跳加快。",
        ),
        EffectDef(
            "瞳孔缩小", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            pack = "阿片类中毒",
            description = "这是一种瞳孔显著缩小的异常体验。旁人可见黑眼仁缩成针尖大小，视觉随之变暗、看近处反而费劲。它是阿片类药物过量的典型体征，需高度警惕。",
        ),
        EffectDef(
            "镇痛", EffectUrgency.ACUTE, EffectValence.NEUTRAL,
            description = "这是疼痛被药物缓解后的松弛体验。主观上感到身体或内脏的痛感信号被调低，紧绷感随之消退，但并不会带来情绪上的欣快或亢奋。镇痛效应本身是中性的躯体感受变化。",
        ),
        EffectDef(
            "肝损伤", EffectUrgency.ACUTE, EffectValence.NEGATIVE,
            description = "这是肝脏代谢负荷过重或毒性损伤时发出的警报。轻度表现为恶心、乏力与食欲下降，重度可出现皮肤巩膜发黄、出血倾向与意识模糊（肝性脑病）。对乙酰氨基酚过量、酒精与某些草药是常见诱因，出现警示信号应立即就医。",
        ),
    )

    /** 效应包说明 */
    val PACK_DESCRIPTIONS: Map<String, String> = mapOf(
        "血清素综合征" to "这是多种药物（尤其是SSRI类抗抑郁药）导致血清素过度蓄积而引发的危急状态。轻度时表现为焦虑不安、恶心、出汗、寒战与心动过速，中度可出现恍惚、阵挛与体温上升，重度则会发展为难治的强直、高血压甚至癫痫。若怀疑发生，请立即就医。",
        "抗胆碱能综合征" to "这是抗胆碱能药物过量而引发的全身反应。轻度表现为口干、便秘、视物模糊、潮红与心动过速，中度加重为坐立不安、烦躁乃至谵妄，重度可导致昏迷、癫痫与低血压。出现明显症状应尽快联系医生。",
        "阿片类中毒" to "这是阿片类药物过量抑制中枢神经系统而引发的危急状态。典型表现包括针尖样瞳孔、呼吸变浅变慢、嗜睡乃至昏迷，并常伴有欣快与便秘。呼吸抑制是其最危险的信号，一旦出现必须立即送医抢救。",
    )

    fun packDescription(name: String): String = PACK_DESCRIPTIONS[name] ?: ""

    val defMap: Map<String, EffectDef> = defs.associateBy { it.name }

    fun def(name: String): EffectDef? = defMap[name]

    /** 全部已知效应名（独立效应 + 效应包），供预警规则选用 */
    val allEffectNames: List<String> get() = defMap.keys.toList() + PACKS.keys

    /** 获取某效应所属的效应包（任一） */
    fun packOf(name: String): String? = PACKS.entries.firstOrNull { (_, members) -> members.contains(name) }?.key

    /** 效应是否为某效应包的成员 */
    fun isPackMember(name: String): Boolean = PACKS.values.any { it.contains(name) }

    /** 首页快捷效应按钮（按行分组） */
    val QUICK_GROUP_TOP = listOf("血清素综合征", "抗胆碱能综合征", "自信")
    val QUICK_GROUP_MIDDLE = listOf("乐观", "恶心", "抑郁", "解离")
    val QUICK_GROUP_BOTTOM = listOf("谵妄", "欣快", "心动过速")

    fun quickButtonInfo(name: String): String = when (name) {
        "血清素综合征" -> "效应包：一系列效应的集合"
        "抗胆碱能综合征" -> "效应包：一系列效应的集合"
        "自信" -> "正面急性效应"
        "乐观" -> "慢性正面效应，与抑郁互为逆效应"
        "恶心" -> "急性负面效应"
        "抑郁" -> "慢性负面效应"
        "解离" -> "急性中立效应"
        "谵妄" -> "由神经递质引起的负面效应"
        "欣快" -> "由神经递质引起的正面效应"
        "心动过速" -> "由神经递质引起的中立效应"
        else -> ""
    }
}

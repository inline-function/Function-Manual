package com.fangshen.manual.core

import com.fangshen.manual.model.DrugInfo

/**
 * 计算某药物的代谢酶受其他药物影响时的箭头后缀：
 * 底物酶被其他药物抑制 -> ↑；被诱导 -> ↓；既有抑制又有诱导 -> ↑↓。
 *
 * 该提示仅为示意（酶抑制/诱导的强弱与时间过程未建模），不能作为用药依据。
 */
fun enzymeArrowsSuffix(target: DrugInfo, others: List<DrugInfo>): String {
    if (target.substrates.isEmpty()) return ""
    val substrates = target.substrates.toHashSet()
    val inhibited = others.any { other ->
        other.id != target.id && other.inhibitors.any { it in substrates }
    }
    val induced = others.any { other ->
        other.id != target.id && other.inducers.any { it in substrates }
    }
    return when {
        inhibited && induced -> "↑↓"
        inhibited -> "↑"
        induced -> "↓"
        else -> ""
    }
}

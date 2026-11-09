package com.fangshen.manual.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.fangshen.manual.engine.EffectEngine
import com.fangshen.manual.model.EffectSeverity
import com.fangshen.manual.model.EffectValence

/**
 * 效应取色：首页效应标签与详情抽屉的权重文本共用同一套配色，
 * 保证“重度正面=橙”之类的程度颜色在两处一致。效应包按程度取粉色系。
 */
@Composable
fun effectGroupColors(group: EffectEngine.EffectGroup): Pair<Color, Color> {
    val isSevereNegative = group.severity == EffectSeverity.SEVERE &&
        group.def?.valence == EffectValence.NEGATIVE
    val isNeutral = group.def?.valence == EffectValence.NEUTRAL
    val bg = when {
        // 效应包：轻度很淡的粉色、中度粉色、重度粉红色
        group.isPack && group.severity == EffectSeverity.MILD -> Color(0xFFFCE4EC)
        group.isPack && group.severity == EffectSeverity.MODERATE -> Color(0xFFF48FB1)
        group.isPack && group.severity == EffectSeverity.SEVERE -> Color(0xFFEC407A)
        // 重度：正面橙、负面红、中性棕
        isSevereNegative -> Color(0xFFEF5350)
        group.severity == EffectSeverity.SEVERE && group.def?.valence == EffectValence.POSITIVE -> Color(0xFFFF9800)
        isNeutral && group.severity == EffectSeverity.SEVERE -> Color(0xFF8D6E63)
        // 中度：正面绿、负面黄、中性黄绿
        group.severity == EffectSeverity.MODERATE && group.def?.valence == EffectValence.POSITIVE -> Color(0xFF66BB6A)
        group.severity == EffectSeverity.MODERATE && group.def?.valence == EffectValence.NEGATIVE -> Color(0xFFFFD54F)
        isNeutral && group.severity == EffectSeverity.MODERATE -> Color(0xFFC0CA33)
        // 轻度：正面很淡的蓝、负面很淡的灰、中性很淡的黄
        group.severity == EffectSeverity.MILD && group.def?.valence == EffectValence.POSITIVE -> Color(0xFFBBDEFB)
        group.severity == EffectSeverity.MILD && group.def?.valence == EffectValence.NEGATIVE -> Color(0xFFE0E0E0)
        isNeutral && group.severity == EffectSeverity.MILD -> Color(0xFFFFF9C4)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    // 深色底（重度负面红 / 重度中性棕 / 重度效应包粉红）配白字，其余深色字
    val fg = if (isSevereNegative ||
        (isNeutral && group.severity == EffectSeverity.SEVERE) ||
        (group.isPack && group.severity == EffectSeverity.SEVERE)
    ) Color.White else Color(0xFF1D1B20)
    return bg to fg
}

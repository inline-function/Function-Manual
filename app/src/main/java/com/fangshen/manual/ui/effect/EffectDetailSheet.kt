package com.fangshen.manual.ui.effect

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fangshen.manual.engine.EffectEngine
import com.fangshen.manual.model.EffectCatalog
import com.fangshen.manual.model.EffectDef
import com.fangshen.manual.model.EffectUrgency
import com.fangshen.manual.model.EffectValence
import com.fangshen.manual.ui.common.effectGroupColors
import com.fangshen.manual.ui.home.BottomModalPanel

/** 副标题蓝字 */
private val SubtitleBlue = androidx.compose.ui.graphics.Color(0xFF1E88E5)

/**
 * 效应详情页面（底部弹层）：
 * 独立效应 / 效应包成员：标题、类别副标题（蓝字）、描述、权重文本（颜色与程度一致）。
 * 效应包：同样布局，额外在底部以圆角卡片列出包内效应（与主页效应芯片同一样式，
 * 带方向箭头、急性效应闪烁），点击可再拉出该效应的详情。
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun EffectDetailSheet(
    name: String,
    effectResult: EffectEngine.Result,
    visible: Boolean,
    onDismiss: () -> Unit,
    onShowMember: (String) -> Unit = {},
) {
    val group = effectResult.groups.firstOrNull { it.name == name }
    val def: EffectDef? = EffectCatalog.def(name)
    val pack = EffectCatalog.PACKS[name]
    val isPackMember = pack == null && EffectCatalog.packOf(name) != null

    BottomModalPanel(visible = visible, onDismiss = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 14.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                val subTitle = when {
                    pack != null -> "效应包"
                    isPackMember -> "效应包成员·" + (def?.run { urgency.label + valence.label + "效应" } ?: "")
                    else -> def?.run { urgency.label + valence.label + "效应" } ?: "独立效应"
                }
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = SubtitleBlue,
                )
                val description = when {
                    pack != null -> EffectCatalog.packDescription(name)
                    else -> def?.description ?: ""
                }
                if (description.isNotBlank()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                val weight = group?.weight ?: 0
                if (weight > 0 && group != null) {
                    val severity = group.severity
                    val weightColor = effectGroupColors(group).first
                    val sourceText = group.sourceWeights.entries
                        .sortedByDescending { it.value }
                        .joinToString("、") { "${it.key}+${it.value}" }
                    Text(
                        text = "${severity.label}：$sourceText",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = weightColor,
                    )
                } else {
                    Text(
                        text = "当前权重：0（未触发）",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (pack != null && group?.members?.isNotEmpty() == true) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        for (member in group.members) {
                            EffectMemberChip(
                                group = member,
                                onClick = { onShowMember(member.name) },
                            )
                        }
                    }
                }
                Text(
                    text = "以上为示意推算，置信度很低，仅为记录提供参考。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

/**
 * 效应包成员芯片：与主页效应芯片样式一致（方向箭头 + 名称，不显示权重），
 * 急性效应闪烁，颜色随效应正中负与轻中重程度变化。
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EffectMemberChip(
    group: EffectEngine.EffectGroup,
    onClick: () -> Unit,
) {
    val prefix = when (group.def?.valence) {
        EffectValence.POSITIVE -> "↑ "
        EffectValence.NEGATIVE -> "↓ "
        else -> "+ "
    }
    val isAcute = group.def?.urgency == EffectUrgency.ACUTE
    val blinkAlpha by if (isAcute) {
        rememberInfiniteTransition(label = "member_blink").animateFloat(
            initialValue = 0.35f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 900),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "member_blink_alpha",
        )
    } else {
        remember { mutableFloatStateOf(1f) }
    }
    val (bg, fg) = effectGroupColors(group)
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg,
        contentColor = fg,
        modifier = Modifier
            .alpha(blinkAlpha)
            .combinedClickable { onClick() },
    ) {
        Text(
            text = prefix + group.name,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(7.dp),
        )
    }
}

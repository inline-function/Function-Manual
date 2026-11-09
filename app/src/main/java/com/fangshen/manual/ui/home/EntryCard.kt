package com.fangshen.manual.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.fangshen.manual.core.formatDoseBD
import com.fangshen.manual.core.formatEventTime
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.model.DoseUnit
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.Route
import java.math.BigDecimal

/**
 * 用药记录卡片：左侧圆角色条（颜色取自药物资料）+ 四角信息，上下间隙 3dp。
 * 单击高亮图像中的该药物曲线，长按弹出确认删除对话框。
 * [nameSuffix] 为代谢酶受其他药物影响的箭头后缀（↑/↓/↑↓）。
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EntryCard(
    entry: EntryEntity,
    drug: DrugInfo?,
    now: Long,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    nameSuffix: String = "",
) {
    val barColor = if (entry.type == "DOSE") {
        drug?.let { Color(com.fangshen.manual.model.SubjectiveTagColors.colorOf(it)) } ?: MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE7E0EC),
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 1.5.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(if (entry.type == "DOSE") 40.dp else 28.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(barColor),
            )
            if (entry.type == "DOSE") {
                val arrowAlpha = arrowBlinkState(nameSuffix)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = drug?.name ?: entry.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        if (nameSuffix.isNotEmpty()) {
                            Text(
                                text = annotatedArrows(nameSuffix),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.graphicsLayer {
                                    alpha = arrowAlpha?.value ?: 1f
                                },
                            )
                        }
                    }
                    Text(
                        text = formatEventTime(entry.eventTime, now),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = formatDoseBD(entry.doseAmount?.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO) + " " + doseUnitShort(entry.doseUnit),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = Route.fromName(entry.route).label,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = entry.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = formatEventTime(entry.eventTime, now),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = "[" + entryTypeLabel(entry.type) + "]",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** 代谢酶箭头：↑ 蓝色、↓ 红色，其余字符（如空格）随正文色 */
private fun annotatedArrows(suffix: String) = buildAnnotatedString {
    suffix.forEach { ch ->
        when (ch) {
            '↑' -> withStyle(SpanStyle(color = ArrowUp)) { append(ch) }
            '↓' -> withStyle(SpanStyle(color = ArrowDown)) { append(ch) }
            else -> append(ch)
        }
    }
}

/**
 * 箭头闪烁状态：没有箭头时返回 null（恒为不透明）。
 * 以 [State] 返回并在 graphicsLayer 的绘制阶段读取，避免每帧重组卡片上的药名文本。
 */
@Composable
private fun arrowBlinkState(suffix: String): androidx.compose.runtime.State<Float>? =
    if (suffix.isEmpty()) {
        null
    } else {
        rememberInfiniteTransition(label = "arrow_blink").animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 900),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "arrow_blink_alpha",
        )
    }

private val ArrowUp = Color(0xFF1E88E5)
private val ArrowDown = Color(0xFFE53935)

private fun doseUnitShort(name: String?): String =
    runCatching { DoseUnit.valueOf(name ?: "MG") }.getOrDefault(DoseUnit.MG).shortLabel

private fun entryTypeLabel(type: String): String = when (type) {
    "NOTE" -> "笔记"
    "PHOTO" -> "照片"
    "VOICE" -> "语音"
    "FILE" -> "文件"
    "SCHEDULE" -> "日程"
    "DOSE" -> "服药"
    else -> "条目"
}

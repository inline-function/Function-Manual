package com.fangshen.manual.ui.prescription

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fangshen.manual.engine.EffectEngine
import com.fangshen.manual.model.EffectCatalog
import com.fangshen.manual.model.EffectSeverity
import com.fangshen.manual.model.EffectUrgency
import com.fangshen.manual.model.EffectValence
import com.fangshen.manual.ui.common.effectGroupColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 处方卡片与长截图共用的整页渲染：标题（粗体，略大于正文）、
 * 药物行（圆角小竖条 + 名称 + 酶箭头 + 给药途径 + 剂量）、
 * 估算效应的流式布局（和主页的风格一致）。
 */
@Composable
fun PrescriptionForCapture(data: PrescriptionShotData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RxTitle(data.name)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (data.rows.isEmpty()) {
                    Text("暂未填写药物", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    data.rows.forEach { row -> RxDrugRow(row) }
                }
            }
        }
        // 适应症
        if (data.indications.isNotEmpty()) {
            Text(
                text = "适应症：" + data.indications.joinToString("、"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        // 估算效应（流式布局，和主页一致；截图静态绘制）
        RxEffectFlow(data.effects, animate = false)
        Spacer(Modifier.height(2.dp))
        Text(
            text = "由「芳沈手册」生成 · " + SimpleDateFormat("yyyy年M月d日", Locale.getDefault())
                .format(Date()),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** 卡片/截图左上角的标题：粗体，字号略大于正文 */
@Composable
fun RxTitle(title: String) {
    Text(
        text = title.ifEmpty { "处方" },
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
    )
}

/**
 * 单行药物：圆角小竖条 + 名称 + 酶箭头（↑↓=该药的底物酶被其他药抑制/诱导）
 * + 给药途径 + 剂量。和“服药”抽屉里待服用行的样式一致。
 */
@Composable
fun RxDrugRow(row: RxRowResolved) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(row.colorArgb)),
        )
        Text(
            text = row.name + if (row.arrows.isEmpty()) "" else "  " + row.arrows,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
        if (row.routeLabel.isNotEmpty()) {
            Text(
                text = row.routeLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (row.dose.isNotBlank()) {
            Text(
                text = row.dose,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/**
 * 估算效应的流式布局：和首页效应标签同一套配色与闪烁规则——
 * 权重映射成 轻度/中度/重度，正面绿/橙、负面黄/红、效应包粉色；急性效应闪烁。
 * animate=false 供长截图使用（静态绘制，避免停在中途透明度）。
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RxEffectFlow(
    effects: List<PrescriptionEffect>,
    animate: Boolean = true,
) {
    if (effects.isEmpty()) {
        HintText("暂无可估算的合用效应")
        return
    }
    val groups = remember(effects) {
        effects
            .filter { it.weight != 0 }
            .map { e ->
                val isPack = EffectCatalog.PACKS.containsKey(e.name)
                EffectEngine.EffectGroup(
                    name = e.name,
                    weight = e.weight,
                    severity = EffectSeverity.fromWeight(e.weight),
                    isPack = isPack,
                    def = EffectCatalog.def(e.name),
                    members = emptyList(),
                    sources = emptyList(),
                )
            }
            .sortedWith(
                compareByDescending<EffectEngine.EffectGroup> { it.isPack }
                    .thenBy { if (it.def?.urgency == EffectUrgency.ACUTE) 0 else 1 }
                    .thenByDescending { it.severity },
            )
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        groups.forEach { group ->
            val prefix = when {
                group.isPack -> "◆ "
                group.def?.valence == EffectValence.POSITIVE -> "↑ "
                group.def?.valence == EffectValence.NEGATIVE -> "↓ "
                else -> "+ "
            }
            val isAcute = animate && (group.isPack || group.def?.urgency == EffectUrgency.ACUTE)
            val blinkAlpha by if (isAcute) {
                rememberInfiniteTransition(label = "rx_effect_blink").animateFloat(
                    initialValue = 0.35f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 900),
                        repeatMode = RepeatMode.Reverse,
                    ),
                    label = "rx_effect_blink_alpha",
                )
            } else {
                remember { mutableFloatStateOf(1f) }
            }
            val (bg, fg) = effectGroupColors(group)
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = bg,
                contentColor = fg,
                modifier = Modifier.alpha(blinkAlpha),
            ) {
                Text(
                    text = prefix + group.name,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(7.dp),
                )
            }
        }
    }
}

/** 编辑页“效果与相互作用”卡片：效应、酶的诱导与抑制、警示 */
@Composable
fun AnalysisCard(analysis: PrescriptionAnalysis) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "同时服用的效应（按当前剂量估算）",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            if (analysis.effectLines.isEmpty()) {
                HintText("填写药物与剂量后在此估算合用效应")
            } else {
                analysis.effectLines.forEach { line -> BulletLine(line) }
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = "酶的诱导与抑制",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            if (analysis.enzymeLines.isEmpty()) {
                HintText("暂无明显的酶相互作用")
            } else {
                analysis.enzymeLines.forEach { line -> BulletLine(line) }
            }
            if (analysis.warnings.isNotEmpty()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "相互作用警示",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error,
                )
                analysis.warnings.forEach { line -> BulletLine(line) }
            }
            if (analysis.unmatched.isNotEmpty()) {
                Spacer(Modifier.height(2.dp))
                HintText("目录中找不到：" + analysis.unmatched.joinToString("、") + "（其效应未计入）")
            }
        }
    }
}

@Composable
fun BulletLine(text: String) {
    Text(
        text = "·  " + text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun HintText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

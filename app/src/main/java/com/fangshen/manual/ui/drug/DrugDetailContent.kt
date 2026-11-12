package com.fangshen.manual.ui.drug

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxHeight
import com.fangshen.manual.model.DrugAttribute
import com.fangshen.manual.model.attributeLevelLabel
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fangshen.manual.core.formatDoseBD
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.SubjectiveTagColors
import com.fangshen.manual.ui.components.Tag

/**
 * 药物资料整页渲染：药物详情页与“长截图”共用这一份。
 * modifier 由调用方决定是否可滚动；内部自带 16dp 边距与背景。
 */
@Composable
fun DrugDetailContent(drug: DrugInfo, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // 顶部名称条（详情页 topBar 与长截图共用同一样式）
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(5.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(SubjectiveTagColors.colorOf(drug))),
            )
            Text(
                text = drug.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = "${drug.managementClass.label} · ${drug.route.label}给药 · 半衰期 ${drug.halfLifeHours} 小时",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (drug.description != null) {
            SectionCard("简介") {
                Text(
                    text = drug.description,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        if (drug.attributes.isNotEmpty()) {
            SectionCard("属性") {
                drug.attributes.forEach { attr -> AttributeRangeBar(attr) }
                Text(
                    text = "属性区间为该药在不同剂量/适应症下的示意范围，仅供参考。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (drug.addictive) {
            SectionCard("成瘾性") {
                InfoRow("生理成瘾性", "有")
                InfoRow("游标移动速度", "${drug.addictionRatePerHour} /小时")
                drug.toleranceGroup?.let { InfoRow("交叉耐药组", it) }
                Text(
                    text = "成瘾游标线随给药持续向当前浓度移动；停药后低于临界值时将提示戒断风险。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        SectionCard("基本信息") {
            InfoRow("监督管理分类", drug.managementClass.label)
            InfoRow("给药途径", drug.route.label)
            InfoRow("计量单位", drug.doseUnit.label)
            drug.tabletDose?.let {
                InfoRow("规格", "${formatDoseBD(it)} ${drug.doseUnit.shortLabel}")
            }
        }
        // 标签：固定按 主观 → 效果 → 分子 排序
        if (
            drug.subjectiveTags.isNotEmpty() ||
            drug.effectTags.isNotEmpty() ||
            drug.moleculeTag.isNotBlank()
        ) {
            SectionCard("标签") {
                if (drug.subjectiveTags.isNotEmpty()) {
                    TagRow("主观标签", drug.subjectiveTags, SubjectiveTagYellow)
                }
                if (drug.effectTags.isNotEmpty()) {
                    TagRow("效果标签", drug.effectTags, EffectTagBlue)
                }
                if (drug.moleculeTag.isNotBlank()) {
                    TagRow("分子标签", listOf(drug.moleculeTag), MoleculeTagPink)
                }
            }
        }
        SectionCard("药代动力学") {
            InfoRow("消除半衰期", "${drug.halfLifeHours} 小时")
            InfoRow("生物利用度", "${drug.bioavailabilityPct}%")
            drug.tmaxHours?.let { InfoRow("达峰时间", "${it} 小时") }
            Text(
                text = "以下浓度曲线为单室 Bateman 模型的示意推算，固定 V = 50 L，" +
                    "未考虑蛋白结合、代谢产物与非线性蓄积，置信度很低，仅供参考。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        SectionCard("酶") {
            if (drug.substrates.isNotEmpty()) InfoRow("底物", drug.substrates.joinToString("、"))
            if (drug.inhibitors.isNotEmpty()) InfoRow("抑制剂", drug.inhibitors.joinToString("、"))
            if (drug.inducers.isNotEmpty()) InfoRow("诱导剂", drug.inducers.joinToString("、"))
            if (
                drug.substrates.isEmpty() && drug.inhibitors.isEmpty() && drug.inducers.isEmpty()
            ) {
                Text(
                    text = "无",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        SectionCard("效应线") {
            if (drug.rules.isEmpty()) {
                Text(
                    text = "无",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                drug.rules.forEach { rule ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    ) {
                        Text(
                            text = "剂量 > ${formatDoseBD(rule.threshold)} ${drug.doseUnit.shortLabel}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = "${rule.effectName} ${if (rule.weight > 0) "+" else ""}${rule.weight}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.padding(bottom = 24.dp))
    }
}

@Composable
fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
            content()
        }
    }
}

/** 标签背景色：主观标签淡黄、效果标签淡蓝、分子标签淡粉 */
val SubjectiveTagYellow = Color(0xFFFFF9C4)
val EffectTagBlue = Color(0xFFBBDEFB)
val MoleculeTagPink = Color(0xFFF8BBD0)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagRow(title: String, tags: List<String>, color: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(top = 4.dp),
        ) {
            tags.forEach { tag -> Tag(text = tag, color = color) }
        }
    }
}

/**
 * 属性范围条：7 段（负/无/正极轻..正极重），命中区间着色——负段红、无及正段蓝。
 */
@Composable
fun AttributeRangeBar(attr: DrugAttribute, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = attr.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = attributeLevelLabel(attr.from) + " — " + attributeLevelLabel(attr.to),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(10.dp),
        ) {
            (-1..5).forEach { level ->
                val hit = level in attr.from..attr.to
                val color = when {
                    !hit -> MaterialTheme.colorScheme.surfaceVariant
                    level < 0 -> Color(0xFFD32F2F)
                    else -> Color(0xFF1E5FA8)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(color),
                )
            }
        }
    }
}

@Composable
fun InfoRow(key: String, value: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = key,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(96.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

package com.fangshen.manual.ui.common

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrugPickCard(
    drug: DrugInfo,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val hasTablet = drug.tabletDose?.let { it > java.math.BigDecimal.ZERO } == true
    // 分类按药物标签中的第一个标签（如“抗精神病剂”“心境稳定剂”“抗抑郁剂”）
    val classify = drug.subjectiveTags.firstOrNull() ?: drug.managementClass.label
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
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
                    .height(40.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(com.fangshen.manual.model.SubjectiveTagColors.colorOf(drug))),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = drug.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = classify + if (hasTablet) "" else " · 无单片剂量，请长按输入",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = if (hasTablet) formatDoseBD(drug.tabletDose!!) + " " + drug.doseUnit.shortLabel else "—",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = drug.route.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}



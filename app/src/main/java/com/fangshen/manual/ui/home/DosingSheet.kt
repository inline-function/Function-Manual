package com.fangshen.manual.ui.home

import com.fangshen.manual.ui.common.DrugPickCard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.fangshen.manual.ui.home.BottomModalPanel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fangshen.manual.core.enzymeArrowsSuffix
import com.fangshen.manual.core.formatDoseBD
import com.fangshen.manual.model.DrugInfo
import java.math.BigDecimal

/**
 * 服药抽屉：从底部弹出，填充四分之三屏高（顶部留四分之一空位），禁用拖拽手势，
 * 只能点击外部阴影或对话框按钮关闭。内容自上而下：搜索框、药物卡片列表、
 * 待服用列表、确认服用按钮。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DosingSheet(
    viewModel: HomeViewModel,
    visible: Boolean,
    onDismiss: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    var customDrug by remember { mutableStateOf<DrugInfo?>(null) }
    val screenHeight = LocalConfiguration.current.screenHeightDp

    BottomModalPanel(visible = visible, onDismiss = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height((screenHeight * 3 / 4).dp),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = "服药",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 17.dp, bottom = 8.dp),
            )
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("搜索药物") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )

            val filtered = state.drugs.filter {
                query.isBlank() || it.name.contains(query.trim(), ignoreCase = true) ||
                    it.subjectiveTags.any { tag -> tag.contains(query.trim(), ignoreCase = true) }
            }

            // 药物列表
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(filtered, key = { it.id }) { drug ->
                    DrugPickCard(
                        drug = drug,
                        onClick = {
                            val perTablet = drug.tabletDose
                            if (perTablet != null && perTablet > java.math.BigDecimal.ZERO) {
                                viewModel.takeTablet(drug)
                            } else {
                                customDrug = drug
                            }
                        },
                        onLongClick = { customDrug = drug },
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(vertical = 4.dp),
            )
            Text(
                text = "待服用",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 2.dp),
            )

            // 待服用列表（行高间隙 2dp）
            val queuedDrugs = viewModel.doseQueue.mapNotNull { item ->
                state.drugs.firstOrNull { it.id == item.drugId }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(viewModel.doseQueue, key = { it.id }) { item ->
                    val drug = state.drugs.firstOrNull { it.id == item.drugId }
                    if (drug != null) {
                        QueueRow(
                            drug = drug,
                            tablets = item.tablets,
                            customAmount = item.customAmount,
                            suffix = enzymeArrowsSuffix(
                                drug,
                                queuedDrugs.filter { it.id != drug.id },
                            ),
                            onRemove = { viewModel.removeQueueItem(item) },
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.confirmQueue(onDone = onDismiss) },
                enabled = viewModel.doseQueue.isNotEmpty(),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            ) {
                Text("确认服用")
            }
        }
        }
    }

    customDrug?.let { drug ->
        CustomDoseDialog(
            drugName = drug.name,
            onConfirm = { amount ->
                viewModel.addCustomDose(drug, amount)
                customDrug = null
            },
            onDismiss = { customDrug = null },
        )
    }
}

@Composable
private fun QueueRow(
    drug: DrugInfo,
    tablets: Int,
    customAmount: BigDecimal?,
    suffix: String,
    onRemove: () -> Unit,
) {
    val summary = when {
        customAmount != null -> formatDoseBD(customAmount) + " " + drug.doseUnit.shortLabel
        tablets > 0 && drug.tabletDose?.let { it > java.math.BigDecimal.ZERO } == true -> {
            val per = drug.tabletDose!!
            tablets.toString() + "t*" + formatDoseBD(per) + drug.doseUnit.shortLabel +
                "=" + formatDoseBD(per.multiply(tablets.toBigDecimal())) + drug.doseUnit.shortLabel
        }
        else -> tablets.toString() + "片"
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(com.fangshen.manual.model.SubjectiveTagColors.colorOf(drug))),
        )
        Text(
            text = drug.name + suffix,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = drug.route.label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = summary,
            style = MaterialTheme.typography.bodyMedium,
        )
        IconButton(onClick = onRemove, modifier = Modifier.size(40.dp)) {
            Icon(Icons.Rounded.Close, contentDescription = "移除")
        }
    }
}

@Composable
private fun CustomDoseDialog(
    drugName: String,
    onConfirm: (BigDecimal) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val amount = runCatching { BigDecimal(text.trim()) }.getOrNull()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("输入剂量") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = drugName)
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("剂量（数字）") },
                    singleLine = true,
                    isError = text.isNotEmpty() && (amount == null || amount <= BigDecimal.ZERO),
                    supportingText = { Text("必填，须大于 0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { amount?.let(onConfirm) },
                enabled = amount != null && amount > BigDecimal.ZERO,
            ) { Text("加入待服用") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}

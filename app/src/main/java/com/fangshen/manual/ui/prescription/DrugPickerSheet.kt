package com.fangshen.manual.ui.prescription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.core.formatDoseBD
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.ui.common.DrugPickCard
import com.fangshen.manual.ui.home.BottomModalPanel
import java.math.BigDecimal

/**
 * 处方编辑用的药库选择抽屉：与主页"服药"抽屉同一套药物卡片，
 * 支持按名称/标签搜索全部内置药物，点击添加（自带单片剂量），
 * 长按或选择无单片剂量的药物时弹出手动剂量输入框。
 */
@Composable
fun DrugPickerSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onPick: (name: String, dose: String) -> Unit,
) {
    val context = LocalContext.current
    val catalog by (context.applicationContext as FangShenApplication)
        .drugRepository.drugs
        .collectAsStateWithLifecycle(emptyList())
    var query by remember { mutableStateOf("") }
    var doseFor by remember { mutableStateOf<DrugInfo?>(null) }
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
                    text = "从药库添加药物（" + catalog.count { !it.hiddenFromCatalog } + " 种）",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 17.dp, bottom = 8.dp),
                )
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("搜索药物、类别或标签") },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )
                val filtered = catalog
                    .filter { !it.hiddenFromCatalog }
                    .filter {
                    query.isBlank() ||
                        it.name.contains(query.trim(), ignoreCase = true) ||
                        it.subjectiveTags.any { tag -> tag.contains(query.trim(), ignoreCase = true) } ||
                        it.moleculeTag.contains(query.trim(), ignoreCase = true)
                    }
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
                                if (perTablet != null && perTablet > BigDecimal.ZERO) {
                                    onPick(
                                        drug.name,
                                        formatDoseBD(perTablet) + " " + drug.doseUnit.label,
                                    )
                                } else {
                                    doseFor = drug
                                }
                            },
                            onLongClick = { doseFor = drug },
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Button(onClick = onDismiss) { Text("完成") }
                }
            }
        }
    }

    doseFor?.let { drug ->
        PrescriptionDoseDialog(
            drugName = drug.name,
            unitLabel = drug.doseUnit.label,
            onConfirm = { doseText ->
                onPick(drug.name, doseText)
                doseFor = null
            },
            onDismiss = { doseFor = null },
        )
    }
}

/** 无单片剂量（或长按自定义）时输入处方的剂量文本，如 "37.5 mg"。 */
@Composable
private fun PrescriptionDoseDialog(
    drugName: String,
    unitLabel: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("输入剂量") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = drugName, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = text,
                    onValueChange = { v -> text = v.filter { it.isDigit() || it == '.' } },
                    label = { Text("剂量（单位：" + unitLabel + "）") },
                    placeholder = { Text("如 37.5") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cleaned = text.trim()
                    if (cleaned.isNotEmpty()) onConfirm(cleaned + " " + unitLabel)
                },
            ) { Text("添加") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}

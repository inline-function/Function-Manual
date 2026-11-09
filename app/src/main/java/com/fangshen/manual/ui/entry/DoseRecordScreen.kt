package com.fangshen.manual.ui.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fangshen.manual.core.formatDose
import com.fangshen.manual.core.formatEventTime
import com.fangshen.manual.model.Route
import com.fangshen.manual.ui.components.IconTextRow
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseRecordScreen(
    onBack: () -> Unit,
    viewModel: DoseRecordViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    ScaffoldContainer(title = "记录服药", onBack = onBack) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            state.error?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            val drugs = state.drugs
            val selected = drugs.firstOrNull { it.id == state.selectedDrugId }
            Box {
                var expanded by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = selected?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("药物") },
                    singleLine = true,
                    isError = state.selectedDrugId == null,
                    supportingText = { if (state.selectedDrugId == null) Text("必选") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Rounded.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    drugs.forEach { drug ->
                        DropdownMenuItem(
                            text = { Text(drug.name) },
                            onClick = {
                                viewModel.selectDrug(drug.id)
                                expanded = false
                            },
                        )
                    }
                }
            }
            if (selected != null) {
                Text(
                    text = "分类：${selected.managementClass.label} · 单位：${selected.doseUnit.label} · " +
                        "半衰期 ${selected.halfLifeHours} 小时",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            OutlinedTextField(
                value = state.amountText,
                onValueChange = viewModel::setAmount,
                label = { Text("剂量（${selected?.doseUnit?.label ?: "单位"}）") },
                singleLine = true,
                isError = state.amountText.toBigDecimalOrNull()?.let { it <= java.math.BigDecimal.ZERO } != false,
                supportingText = { Text("必填，须大于 0") },
                modifier = Modifier.fillMaxWidth(),
            )
            Box {
                var expanded by remember { mutableStateOf(false) }
                val route = Route.fromName(state.routeName)
                OutlinedTextField(
                    value = route.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("给药途径") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Rounded.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    Route.entries.forEach { r ->
                        DropdownMenuItem(
                            text = { Text(r.label) },
                            onClick = {
                                viewModel.setRoute(r.name)
                                expanded = false
                            },
                        )
                    }
                }
            }
            OutlinedTextField(
                value = formatEventTime(state.eventTime),
                onValueChange = {},
                readOnly = true,
                label = { Text("服药时间") },
                singleLine = true,
                trailingIcon = {
                    TextButton(onClick = { showDatePicker = true }) { Text("修改") }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            if (state.warnings.isNotEmpty()) {
                state.warnings.forEach { warning ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Warning, contentDescription = null)
                                Text(
                                    text = "相互作用提示（${warning.kind.label}：${warning.enzyme}）",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(start = 8.dp),
                                )
                            }
                            Text(
                                text = warning.message,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
            Button(
                onClick = { viewModel.save(onDone = onBack) },
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("保存记录")
            }
            Text(
                text = "相互作用检查基于代谢酶的示意规则（抑制剂使共用酶的药物浓度升高、诱导剂使其降低、" +
                    "共用底物方向不明），并非完整药物相互作用数据库，只能作为提示。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    if (showDatePicker) {
        val init = Calendar.getInstance().apply { timeInMillis = state.eventTime }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.eventTime,
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { dateMillis ->
                        val cal = Calendar.getInstance().apply {
                            timeInMillis = dateMillis
                            val saved = Calendar.getInstance().apply { timeInMillis = state.eventTime }
                            set(Calendar.HOUR_OF_DAY, saved.get(Calendar.HOUR_OF_DAY))
                            set(Calendar.MINUTE, saved.get(Calendar.MINUTE))
                        }
                        viewModel.setTime(cal.timeInMillis)
                    }
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("下一步") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("取消") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
    if (showTimePicker) {
        val init = Calendar.getInstance().apply { timeInMillis = state.eventTime }
        val timePickerState = rememberTimePickerState(
            initialHour = init.get(Calendar.HOUR_OF_DAY),
            initialMinute = init.get(Calendar.MINUTE),
            is24Hour = true,
        )
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("选择时间") },
            confirmButton = {
                TextButton(onClick = {
                    val cal = Calendar.getInstance().apply {
                        timeInMillis = state.eventTime
                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        set(Calendar.MINUTE, timePickerState.minute)
                    }
                    viewModel.setTime(cal.timeInMillis)
                    showTimePicker = false
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("取消") }
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldContainer(
    title: String,
    onBack: () -> Unit,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit,
) {
    androidx.compose.material3.Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text(title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { innerPadding -> content(innerPadding) }
}

package com.fangshen.manual.ui.drug

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import com.fangshen.manual.model.DoseUnit
import com.fangshen.manual.model.ManagementClass
import com.fangshen.manual.model.Neurotransmitters
import com.fangshen.manual.model.Route

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DrugEditScreen(
    onBack: () -> Unit,
    viewModel: DrugEditViewModel,
) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    var tagText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(if (form.id == 0L) "添加药物" else "编辑药物") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            form.error?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            OutlinedTextField(
                value = form.name,
                onValueChange = { v -> viewModel.update { it.copy(name = v) } },
                label = { Text("名称") },
                singleLine = true,
                isError = form.name.isBlank(),
                supportingText = { if (form.name.isBlank()) Text("必填") },
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = form.tabletDose,
                    onValueChange = { v -> viewModel.update { it.copy(tabletDose = v) } },
                    label = { Text("规格剂量") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                EnumDropdown(
                    label = "单位",
                    selected = form.doseUnit,
                    options = DoseUnit.entries.map { it.name to it.label },
                    onSelected = { v -> viewModel.update { it.copy(doseUnit = v) } },
                    modifier = Modifier.weight(1f),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                EnumDropdown(
                    label = "监督管理分类",
                    selected = form.managementClass,
                    options = ManagementClass.entries.map { it.name to it.label },
                    onSelected = { v -> viewModel.update { it.copy(managementClass = v) } },
                    modifier = Modifier.weight(1f),
                )
                EnumDropdown(
                    label = "给药途径",
                    selected = form.route,
                    options = Route.entries.map { it.name to it.label },
                    onSelected = { v -> viewModel.update { it.copy(route = v) } },
                    modifier = Modifier.weight(1f),
                )
            }

            OutlinedTextField(
                value = form.moleculeTag,
                onValueChange = { v -> viewModel.update { it.copy(moleculeTag = v) } },
                label = { Text("分子标签") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = form.halfLifeHours,
                    onValueChange = { v -> viewModel.update { it.copy(halfLifeHours = v) } },
                    label = { Text("消除半衰期（小时）") },
                    singleLine = true,
                    isError = form.halfLifeHours.toDoubleOrNull()?.let { it <= 0 } != false,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = form.bioavailabilityPct,
                    onValueChange = { v -> viewModel.update { it.copy(bioavailabilityPct = v) } },
                    label = { Text("生物利用度（%）") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }

            OutlinedTextField(
                value = form.tmaxHours,
                onValueChange = { v -> viewModel.update { it.copy(tmaxHours = v) } },
                label = { Text("达峰时间（小时，可留空）") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            ChipListEditor(
                title = "主观标签",
                chips = form.subjectiveTags,
                onRemove = { tag -> viewModel.update { it.copy(subjectiveTags = it.subjectiveTags - tag) } },
                onAdd = { tag -> viewModel.update { it.copy(subjectiveTags = it.subjectiveTags + tag) } },
            )
            ChipListEditor(
                title = "效果标签",
                chips = form.effectTags,
                onRemove = { tag -> viewModel.update { it.copy(effectTags = it.effectTags - tag) } },
                onAdd = { tag -> viewModel.update { it.copy(effectTags = it.effectTags + tag) } },
            )

            SectionTitle("酶")
            ChipListEditor(
                title = "底物",
                chips = form.substrates,
                suggestions = Neurotransmitters.ENZYMES,
                onRemove = { tag -> viewModel.update { it.copy(substrates = it.substrates - tag) } },
                onAdd = { tag -> viewModel.update { it.copy(substrates = it.substrates + tag) } },
            )
            ChipListEditor(
                title = "抑制剂",
                chips = form.inhibitors,
                suggestions = Neurotransmitters.ENZYMES,
                onRemove = { tag -> viewModel.update { it.copy(inhibitors = it.inhibitors - tag) } },
                onAdd = { tag -> viewModel.update { it.copy(inhibitors = it.inhibitors + tag) } },
            )
            ChipListEditor(
                title = "诱导剂",
                chips = form.inducers,
                suggestions = Neurotransmitters.ENZYMES,
                onRemove = { tag -> viewModel.update { it.copy(inducers = it.inducers - tag) } },
                onAdd = { tag -> viewModel.update { it.copy(inducers = it.inducers + tag) } },
            )

            SectionTitle("效应线")
            form.rules.forEachIndexed { index, row ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = row.threshold,
                        onValueChange = { v -> viewModel.update { f ->
                            f.copy(rules = f.rules.toMutableList().also { it[index] = it[index].copy(threshold = v) })
                        } },
                        label = { Text("阈值") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    OutlinedTextField(
                        value = row.effectName,
                        onValueChange = { v -> viewModel.update { f ->
                            f.copy(rules = f.rules.toMutableList().also { it[index] = it[index].copy(effectName = v) })
                        } },
                        label = { Text("效应名") },
                        singleLine = true,
                        modifier = Modifier.weight(1.2f),
                    )
                    OutlinedTextField(
                        value = row.weight,
                        onValueChange = { v -> viewModel.update { f ->
                            f.copy(rules = f.rules.toMutableList().also { it[index] = it[index].copy(weight = v) })
                        } },
                        label = { Text("权重") },
                        singleLine = true,
                        modifier = Modifier.weight(0.7f),
                    )
                    IconButton(onClick = {
                        viewModel.update { f -> f.copy(rules = f.rules.toMutableList().also { it.removeAt(index) }) }
                    }) {
                        Icon(Icons.Rounded.Close, contentDescription = "删除规则")
                    }
                }
            }
            TextButton(onClick = {
                viewModel.update { it.copy(rules = it.rules + DoseRuleRow()) }
            }) { Text("+ 添加效应线") }

            Button(
                onClick = {
                    viewModel.save(
                        onSuccess = { _ -> onBack() },
                        onError = { message -> viewModel.update { it.copy(error = message) } },
                    )
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("保存")
            }
            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("取消")
            }
            androidx.compose.foundation.layout.Spacer(Modifier.padding(bottom = 24.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ChipListEditor(
    title: String,
    chips: List<String>,
    suggestions: List<String> = emptyList(),
    onRemove: (String) -> Unit,
    onAdd: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            chips.forEach { chip ->
                InputChip(
                    selected = false,
                    onClick = {},
                    label = { Text(chip) },
                    trailingIcon = {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = "移除",
                            modifier = Modifier
                                .padding(2.dp)
                                .clickable { onRemove(chip) },
                        )
                    },
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("输入并添加") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = {
                if (text.isNotBlank()) {
                    onAdd(text.trim())
                    text = ""
                }
            }) { Text("添加") }
        }
        if (suggestions.isNotEmpty() && chips.size < suggestions.size) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                suggestions.filter { it !in chips }.take(10).forEach { suggestion ->
                    AssistChip(
                        onClick = { onAdd(suggestion) },
                        label = { Text(suggestion, style = MaterialTheme.typography.labelSmall) },
                    )
                }
            }
        }
    }
}

@Composable
private fun EnumDropdown(
    label: String,
    selected: String,
    options: List<Pair<String, String>>,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val displayLabel = options.firstOrNull { it.first == selected }?.second ?: selected
    Box(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = displayLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Rounded.ArrowDropUp
                        else Icons.Rounded.ArrowDropDown,
                        contentDescription = null,
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { (key, text) ->
                androidx.compose.material3.DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        onSelected(key)
                        expanded = false
                    },
                )
            }
        }
    }
}

package com.fangshen.manual.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * 报告编辑页：结构化表单（与药物编辑页同一风格），
 * 保存时由 ViewModel 序列化为 .rp 的 JSON。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportEditScreen(
    fileName: String,
    onBack: () -> Unit,
    viewModel: ReportEditViewModel,
) {
    val form by viewModel.form.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "编辑：${form.name.ifEmpty { fileName }}",
                        fontWeight = FontWeight.Medium,
                    )
                },
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

            SectionTitle("报告头")
            OutlinedTextField(
                value = form.name,
                onValueChange = { v -> viewModel.update { it.copy(name = v) } },
                label = { Text("报告名称") },
                singleLine = true,
                isError = form.name.isBlank(),
                supportingText = { if (form.name.isBlank()) Text("必填") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = form.date,
                onValueChange = { v -> viewModel.update { it.copy(date = v) } },
                label = { Text("报告日期") },
                singleLine = true,
                placeholder = { Text("如 2026年11月8日") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = form.location,
                onValueChange = { v -> viewModel.update { it.copy(location = v) } },
                label = { Text("地点") },
                singleLine = true,
                placeholder = { Text("如 市第七人民医院") },
                modifier = Modifier.fillMaxWidth(),
            )

            SectionTitle("报告者")
            form.characters.forEachIndexed { index, row ->
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = row.name,
                        onValueChange = { v -> viewModel.update { f ->
                            f.copy(characters = f.characters.toMutableList().also {
                                it[index] = it[index].copy(name = v)
                            })
                        } },
                        label = { Text("姓名") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    OutlinedTextField(
                        value = row.note,
                        onValueChange = { v -> viewModel.update { f ->
                            f.copy(characters = f.characters.toMutableList().also {
                                it[index] = it[index].copy(note = v)
                            })
                        } },
                        label = { Text("备注（可选）") },
                        singleLine = true,
                        modifier = Modifier.weight(1.4f),
                    )
                    IconButton(onClick = { viewModel.removeCharacter(index) }) {
                        Icon(Icons.Rounded.Close, contentDescription = "删除该报告者")
                    }
                }
            }
            TextButton(onClick = { viewModel.addCharacter() }) {
                Text("+ 添加报告者")
            }

            SectionTitle("前言")
            OutlinedTextField(
                value = form.preface,
                onValueChange = { v -> viewModel.update { it.copy(preface = v) } },
                label = { Text("前言") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )

            SectionTitle("正文片段")
            form.fragments.forEachIndexed { index, row ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = (index + 1).toString() + " · " +
                                if (row is FragmentRow.DialogRow) "对话片段" else "场景片段",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f),
                        )
                        IconButton(onClick = { viewModel.removeFragment(index) }) {
                            Icon(Icons.Rounded.Close, contentDescription = "删除该片段")
                        }
                    }
                    when (row) {
                        is FragmentRow.SceneRow -> {
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                OutlinedTextField(
                                    value = row.time,
                                    onValueChange = { v -> viewModel.updateFragment(index, row.copy(time = v)) },
                                    label = { Text("时间") },
                                    singleLine = true,
                                    placeholder = { Text("如 06:15") },
                                    modifier = Modifier.weight(1f),
                                )
                                OutlinedTextField(
                                    value = row.place,
                                    onValueChange = { v -> viewModel.updateFragment(index, row.copy(place = v)) },
                                    label = { Text("地点/场景") },
                                    singleLine = true,
                                    placeholder = { Text("如 家中的厨房") },
                                    modifier = Modifier.weight(1.4f),
                                )
                            }
                            OutlinedTextField(
                                value = row.description,
                                onValueChange = { v -> viewModel.updateFragment(index, row.copy(description = v)) },
                                label = { Text("场景描述（内联可用 **粗体** *斜体* ~~删除线~~ 与反引号代码）") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                            )
                        }
                        is FragmentRow.DialogRow -> {
                            row.items.forEachIndexed { itemIndex, item ->
                                when (item) {
                                    is DialogItemRow.SayRow -> Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.fillMaxWidth(),
                                    ) {
                                        Row(
                                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        ) {
                                            OutlinedTextField(
                                                value = item.name,
                                                onValueChange = { v ->
                                                    viewModel.updateItem(index, itemIndex, item.copy(name = v))
                                                },
                                                label = { Text("说话人") },
                                                singleLine = true,
                                                modifier = Modifier.weight(1f),
                                            )
                                            IconButton(onClick = { viewModel.removeItem(index, itemIndex) }) {
                                                Icon(Icons.Rounded.Close, contentDescription = "删除该句")
                                            }
                                        }
                                        OutlinedTextField(
                                            value = item.content,
                                            onValueChange = { v ->
                                                viewModel.updateItem(index, itemIndex, item.copy(content = v))
                                            },
                                            label = { Text("内容") },
                                            modifier = Modifier.fillMaxWidth(),
                                            minLines = 1,
                                        )
                                    }
                                    is DialogItemRow.EventRow -> Row(
                                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        OutlinedTextField(
                                            value = item.content,
                                            onValueChange = { v ->
                                                viewModel.updateItem(index, itemIndex, item.copy(content = v))
                                            },
                                            label = { Text("居中事件（如：她睡着了）") },
                                            modifier = Modifier.weight(1f),
                                        )
                                        IconButton(onClick = { viewModel.removeItem(index, itemIndex) }) {
                                            Icon(Icons.Rounded.Close, contentDescription = "删除该事件")
                                        }
                                    }
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(onClick = {
                                    viewModel.addSay(index, form.name.ifBlank { form.characters.firstOrNull()?.name ?: "" })
                                }) { Text("+ 对话") }
                                TextButton(onClick = { viewModel.addEvent(index) }) { Text("+ 事件") }
                            }
                        }
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = { viewModel.addSceneFragment() }) { Text("+ 场景片段") }
                TextButton(onClick = { viewModel.addDialogFragment() }) { Text("+ 对话片段") }
            }
            SectionTitle("结语")
            OutlinedTextField(
                value = form.conclusion,
                onValueChange = { v -> viewModel.update { it.copy(conclusion = v) } },
                label = { Text("结语") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )

            Spacer(Modifier.height(4.dp))
            Button(
                onClick = { viewModel.save(onBack) },
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
            ) { Text("保存") }
            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
            ) { Text("取消") }
            Spacer(Modifier.padding(bottom = 24.dp))
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

package com.fangshen.manual.ui.prescription

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.model.Prescription
import com.fangshen.manual.model.PrescriptionDrug
import com.fangshen.manual.ui.theme.FangShenTheme
import com.fangshen.manual.util.LongCapture
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PrescriptionEditScreen(
    displayName: String,
    onBack: () -> Unit,
    viewModel: PrescriptionEditViewModel,
) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    val analysis by viewModel.analysis.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val captureScope = rememberCoroutineScope()
    var showPicker by remember { mutableStateOf(false) }
    val catalog by (context.applicationContext as FangShenApplication)
        .drugRepository.drugs
        .collectAsStateWithLifecycle(emptyList())
    var indText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (form.name.isEmpty() && form.displayName.isEmpty()) "新处方"
                        else form.name.ifEmpty { displayName },
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        captureScope.launch {
                            val parsed = Prescription(
                                name = form.name.ifEmpty { displayName },
                                drugs = form.drugs
                                    .filter { it.name.isNotBlank() }
                                    .map { PrescriptionDrug(it.name, it.dose) },
                                indications = form.indications,
                            )
                            val catalog = (context.applicationContext as FangShenApplication)
                                .drugRepository.drugs.first()
                            val shot = buildShotData(catalog, parsed)
                            LongCapture.capture(context, "处方_" + shot.name) {
                                FangShenTheme { PrescriptionForCapture(shot) }
                            }
                        }
                    }) {
                        Icon(Icons.Rounded.PhotoCamera, contentDescription = "保存到相册")
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

            SectionTitle("处方名称")
            OutlinedTextField(
                value = form.name,
                onValueChange = { v -> viewModel.update { it.copy(name = v) } },
                label = { Text("例如：加州火箭燃料、氟奥合剂") },
                singleLine = true,
                isError = form.name.isBlank(),
                supportingText = { if (form.name.isBlank()) Text("必填") },
                modifier = Modifier.fillMaxWidth(),
            )

            SectionTitle("药物组合（${form.drugs.size}）")
            form.drugs.forEachIndexed { index, row ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = row.name,
                        onValueChange = { v -> viewModel.update { f ->
                            f.copy(drugs = f.drugs.toMutableList().also { it[index] = it[index].copy(name = v) })
                        } },
                        label = { Text("药物名称") },
                        singleLine = true,
                        modifier = Modifier.weight(1.4f),
                    )
                    OutlinedTextField(
                        value = row.dose,
                        onValueChange = { v -> viewModel.update { f ->
                            f.copy(drugs = f.drugs.toMutableList().also { it[index] = it[index].copy(dose = v) })
                        } },
                        label = { Text("剂量") },
                        singleLine = true,
                        placeholder = { Text("如 50 mg") },
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = { viewModel.removeDrug(index) }) {
                        Icon(Icons.Rounded.Close, contentDescription = "删除该药物")
                    }
                }
            }
            OutlinedButton(
                onClick = { showPicker = true },
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 6.dp),
                )
                Text("从药库添加药物（共 " + catalog.size + " 种，可搜索标签）")
            }
            if (showPicker) {
                DrugPickerSheet(
                    visible = true,
                    onDismiss = { showPicker = false },
                    onPick = { name, dose ->
                        viewModel.addDrug(name, dose)
                        showPicker = false
                    },
                )
            }

            SectionTitle("适应症")
            if (form.indications.isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    form.indications.forEach { text ->
                        InputChip(
                            selected = false,
                            onClick = { viewModel.removeIndication(text) },
                            label = { Text(text) },
                            trailingIcon = {
                                Icon(
                                    Icons.Rounded.Close,
                                    contentDescription = "点击整个条目可移除",
                                    modifier = Modifier.padding(end = 2.dp),
                                )
                            },
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = indText,
                    onValueChange = { indText = it },
                    label = { Text("添加适应症") },
                    singleLine = true,
                    placeholder = { Text("如 双相情感障碍") },
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = {
                    viewModel.addIndication(indText)
                    indText = ""
                }) { Text("添加") }
            }

            SectionTitle("效果与相互作用")
            AnalysisCard(analysis)

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

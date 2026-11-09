package com.fangshen.manual.ui.prescription

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fangshen.manual.ui.theme.FangShenTheme
import com.fangshen.manual.util.LongCapture
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionListScreen(
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    viewModel: PrescriptionListViewModel,
) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    var showCreate by remember { mutableStateOf(false) }
    var actionTarget by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val captureScope = rememberCoroutineScope()

    // 从编辑页返回时刷新
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refresh()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("处方", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreate = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "添加新的处方")
            }
        },
    ) { innerPadding ->
        if (items.isEmpty() && !loading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Text(
                    text = "还没有处方。点右下角 + 创建您的药物组合。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        LazyColumn(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            items(items = items, key = { it.displayName }) { summary ->
                PrescriptionCard(
                    summary = summary,
                    onClick = { onEdit(summary.displayName) },
                    onLongClick = { actionTarget = summary.displayName },
                )
            }
        }
    }

    if (showCreate) {
        CreatePrescriptionDialog(
            onDismiss = { showCreate = false },
            onConfirm = { name ->
                showCreate = false
                viewModel.create(name) { displayName -> onEdit(displayName) }
            },
        )
    }

    actionTarget?.let { name ->
        PrescriptionActionDialog(
            onEdit = { actionTarget = null; onEdit(name) },
            onScreenshot = {
                actionTarget = null
                captureScope.launch {
                    val shot = viewModel.renderForScreenshot(name) ?: return@launch
                    LongCapture.capture(context, "处方_" + shot.name.ifEmpty { name }) {
                        FangShenTheme { PrescriptionForCapture(shot) }
                    }
                }
            },
            onDelete = { actionTarget = null; viewModel.delete(name) },
            onShare = { actionTarget = null; viewModel.share(name) },
            onDismiss = { actionTarget = null },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
private fun PrescriptionCard(
    summary: PrescriptionSummary,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            RxTitle(summary.name.ifEmpty { summary.displayName })
            summary.rows.forEach { row -> RxDrugRow(row) }
            RxEffectFlow(summary.effects)
        }
    }
}

@Composable
private fun CreatePrescriptionDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新建处方") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("处方名称") },
                singleLine = true,
                supportingText = { Text("例如：加州火箭燃料、氟奥合剂") },
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name) },
                enabled = name.isNotBlank(),
            ) { Text("创建并编辑") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}

@Composable
private fun PrescriptionActionDialog(
    onEdit: () -> Unit,
    onScreenshot: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("处方操作") },
        text = {
            Column {
                TextButton(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
                    Text("编辑", textAlign = androidx.compose.ui.text.style.TextAlign.Start)
                }
                TextButton(onClick = onScreenshot, modifier = Modifier.fillMaxWidth()) {
                    Text("截图保存到相册", textAlign = androidx.compose.ui.text.style.TextAlign.Start)
                }
                TextButton(onClick = onShare, modifier = Modifier.fillMaxWidth()) {
                    Text("分享", textAlign = androidx.compose.ui.text.style.TextAlign.Start)
                }
                TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                    Text("删除", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}

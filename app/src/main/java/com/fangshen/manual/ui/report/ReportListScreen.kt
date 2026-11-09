package com.fangshen.manual.ui.report

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.rememberCoroutineScope
import com.fangshen.manual.ui.theme.FangShenTheme
import com.fangshen.manual.util.LongCapture
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportListScreen(
    onOpenReport: (String) -> Unit,
    onEditReport: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: ReportListViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showCreate by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val captureScope = rememberCoroutineScope()

    // 从阅读/编辑页返回时刷新列表（新建的报告才能显示）
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refresh()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    // 长按选中的报告（弹“删除 / 编辑 / 分享”对话框）
    var actionTarget by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("报告", fontWeight = FontWeight.SemiBold) },
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
                Icon(Icons.Rounded.Add, contentDescription = "添加新的报告")
            }
        },
    ) { innerPadding ->
        if (state.reports.isEmpty() && !state.loading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Text(
                    text = "还没有报告。点右下角 + 写一份吧。",
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
            items(items = state.reports, key = { it.fileName }) { summary ->
                ReportCard(
                    summary = summary,
                    onClick = { onOpenReport(summary.fileName) },
                    onLongClick = { actionTarget = summary.fileName },
                )
            }
        }
    }

    if (showCreate) {
        CreateReportDialog(
            onDismiss = { showCreate = false },
            onConfirm = { name ->
                showCreate = false
                viewModel.create(name) { fileName -> onEditReport(fileName) }
            },
        )
    }

    actionTarget?.let { fileName ->
        ReportActionDialog(
            onOpen = { actionTarget = null; onOpenReport(fileName) },
            onEdit = { actionTarget = null; onEditReport(fileName) },
            onScreenshot = {
                actionTarget = null
                captureScope.launch {
                    val report = viewModel.parseForScreenshot(fileName) ?: return@launch
                    LongCapture.capture(context, "报告_" + report.head.name.ifEmpty { fileName }) {
                        FangShenTheme { ReportForCapture(report) }
                    }
                }
            },
            onDelete = { actionTarget = null; viewModel.delete(fileName) },
            onShare = { actionTarget = null; viewModel.share(fileName) },
            onDismiss = { actionTarget = null },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReportCard(
    summary: ReportSummary,
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
                .heightIn(min = 96.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // 左上：报告名
            Text(
                text = summary.name.ifEmpty { summary.fileName },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // 左下：前言
            Text(
                text = summary.preface.ifEmpty { "（无前言）" },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun CreateReportDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新建报告") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("报告名") },
                singleLine = true,
                isError = name.isNotBlank() && name.isBlank(),
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
private fun ReportActionDialog(
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onScreenshot: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("报告操作") },
        text = {
            Column {
                TextButton(onClick = onOpen, modifier = Modifier.fillMaxWidth()) {
                    Text("阅读", textAlign = androidx.compose.ui.text.style.TextAlign.Start)
                }
                TextButton(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
                    Text("编辑", textAlign = androidx.compose.ui.text.style.TextAlign.Start)
                }
                TextButton(onClick = onShare, modifier = Modifier.fillMaxWidth()) {
                    Text("分享", textAlign = androidx.compose.ui.text.style.TextAlign.Start)
                }
                TextButton(onClick = onScreenshot, modifier = Modifier.fillMaxWidth()) {
                    Text("截图保存到相册", textAlign = androidx.compose.ui.text.style.TextAlign.Start)
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

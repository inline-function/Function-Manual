package com.fangshen.manual.ui.settings

import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.core.formatEventTime
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.ui.components.ConfirmDialog
import com.fangshen.manual.ui.components.EmptyState
import com.fangshen.manual.ui.entry.ScheduleListViewModel
import com.fangshen.manual.ui.profile.DisorderCategory
import com.fangshen.manual.ui.profile.DisorderRecord
import com.fangshen.manual.ui.profile.DISORDER_DESCRIPTIONS
import com.fangshen.manual.ui.profile.ProfileViewModel
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.fangshen.manual.ui.navigation.Routes
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleListScreen(
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: ScheduleListViewModel,
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    var pendingDelete by remember { mutableStateOf<EntryEntity?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("日程", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate(Routes.entryNew("SCHEDULE")) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "新建日程")
            }
        },
    ) { innerPadding ->
        if (items.isEmpty()) {
            EmptyState(
                message = "暂无日程。点击右下角按钮新建日程。",
                modifier = Modifier.padding(innerPadding),
            )
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items.forEach { entry ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    androidx.compose.foundation.layout.Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Column(modifier = Modifier.padding(end = 8.dp)) {
                            Text(
                                text = entry.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = formatEventTime(entry.eventTime),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = if (entry.notify) "已设置提醒" else "无提醒",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        IconButton(onClick = { pendingDelete = entry }) {
                            Icon(Icons.Rounded.Delete, contentDescription = "删除")
                        }
                    }
                }
            }
        }
    }

    pendingDelete?.let { entry ->
        ConfirmDialog(
            title = "删除日程",
            message = "确认删除「${entry.title}」吗？",
            confirmText = "删除",
            onConfirm = {
                pendingDelete = null
                viewModel.delete(entry, onDone = {})
            },
            onDismiss = { pendingDelete = null },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var addingTo by remember { mutableStateOf<String?>(null) }
    var pendingDelete by remember { mutableStateOf<Pair<String, DisorderRecord>?>(null) }
    var infoFor by remember { mutableStateOf<String?>(null) }

    SimpleScaffold(title = "个人画像", onBack = onBack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "精神障碍统计",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "共 ${state.total} 项 · 涉及 ${state.usedCategories} / 11 个类别",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "点选具体障碍条目即记入统计；未列出的条目可用卡片右上角 + 自行添加。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            state.groups.forEach { group ->
                DisorderCategoryCard(
                    group = group,
                    onToggle = { item -> viewModel.toggleItem(group.name, item) },
                    onAdd = { addingTo = group.name },
                    onDelete = { record -> pendingDelete = group.name to record },
                    onShowInfo = { item -> infoFor = item },
                )
            }
        }
    }

    infoFor?.let { name ->
        DisorderInfoDialog(
            name = name,
            description = DISORDER_DESCRIPTIONS[name],
            onDismiss = { infoFor = null },
        )
    }
    addingTo?.let { category ->
        AddDisorderDialog(
            category = category,
            onDismiss = { addingTo = null },
            onConfirm = { name, note ->
                viewModel.addRecord(category, name, note)
                addingTo = null
            },
        )
    }
    pendingDelete?.let { (category, record) ->
        ConfirmDialog(
            title = "删除记录",
            message = "确认删除「${record.name}」吗？",
            confirmText = "删除",
            onConfirm = {
                viewModel.deleteRecord(category, record)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null },
        )
    }
}

/** 细分障碍条目：圆角胶囊，点击选中、长按弹出简介 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DisorderChip(
    item: String,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick,
        ),
    ) {
        Text(
            text = item,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
        )
    }
}

/** 单个障碍类别卡片：标题、记录数徽标、细分条目 chips、自定义记录 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DisorderCategoryCard(
    group: DisorderCategory,
    onToggle: (String) -> Unit,
    onAdd: () -> Unit,
    onDelete: (DisorderRecord) -> Unit,
    onShowInfo: (String) -> Unit,
) {
    Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (group.count == 0) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    },
                    contentColor = if (group.count == 0) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    },
                ) {
                    Text(
                        text = group.count.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                    )
                }
                IconButton(onClick = onAdd) {
                    Icon(Icons.Rounded.Add, contentDescription = "添加" + group.name + "的自定义记录")
                }
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                group.items.forEach { item ->
                    DisorderChip(
                        item = item,
                        selected = item in group.selected,
                        onClick = { onToggle(item) },
                        onLongClick = { onShowInfo(item) },
                    )
                }
            }
            group.custom.forEach { record ->
                DisorderRecordRow(record = record, onDelete = onDelete)
            }
        }
    }
}

/** 长按障碍条目时弹出的简介对话框 */
@Composable
private fun DisorderInfoDialog(
    name: String,
    description: String?,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(name, fontWeight = FontWeight.SemiBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = description ?: "暂无该障碍的简介。",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "简介仅供了解，不构成诊断依据；诊断与治疗请遵精神科医师。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("关闭") }
        },
    )
}

/** 自定义记录行：名称、备注、日期，右侧删除 */
@Composable
private fun DisorderRecordRow(
    record: DisorderRecord,
    onDelete: (DisorderRecord) -> Unit,
) {
    Row(verticalAlignment = Alignment.Top) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = record.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
            if (record.note.isNotEmpty()) {
                Text(
                    text = record.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (record.time > 0L) {
                Text(
                    text = remember(record.time) {
                        SimpleDateFormat("yyyy年M月d日", Locale.getDefault()).format(Date(record.time))
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        IconButton(onClick = { onDelete(record) }) {
            Icon(Icons.Rounded.Delete, contentDescription = "删除")
        }
    }
}

/** 添加自定义障碍记录：名称必填、备注可选 */
@Composable
private fun AddDisorderDialog(
    category: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, note: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加到$category") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "该类别下未列出的障碍可在此自行添加。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("障碍名称（如：重度抑郁障碍）") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("备注（可选）") },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onConfirm(name, note) },
                enabled = name.isNotBlank(),
            ) { Text("添加") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}
class DataManagementViewModel(
    application: Application,
) : AndroidViewModel(application) {

    /** 整份数据的 JSON 文本（服药记录 + 个人画像 + 自定义药物） */
    suspend fun exportJson(): String = withContext(Dispatchers.IO) {
        getApplication<FangShenApplication>().jsonDataStore.exportJsonString()
    }

    /** 写出导出到外部私有目录 export/ 并返回文件 */
    suspend fun exportFile(): File = withContext(Dispatchers.IO) {
        getApplication<FangShenApplication>().jsonDataStore.writeExportCopy()
    }

    fun clearAllEntries(onDone: () -> Unit) {
        viewModelScope.launch {
            val app = getApplication<FangShenApplication>()
            app.entryRepository.deleteAll()
            onDone()
        }
    }

    fun clearAllUserData(onDone: () -> Unit) {
        viewModelScope.launch {
            val app = getApplication<FangShenApplication>()
            app.entryRepository.deleteAll()
            app.profileRepository.clear()
            onDone()
        }
    }

    companion object {
        fun provideFactory(app: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DataManagementViewModel(app) as T
    }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataManagementScreen(
    onBack: () -> Unit,
    viewModel: DataManagementViewModel,
) {
    var clearEntries by remember { mutableStateOf(false) }
    var clearAll by remember { mutableStateOf(false) }
    var exportTip by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    SimpleScaffold(title = "数据管理", onBack = onBack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DataCard(
                title = "导出数据（JSON）",
                description = "把服药记录、个人画像与自定义药物导出为 JSON 文件，" +
                    "存放在应用外部私有目录，可通过系统分享发送出去。",
                buttonText = "导出并分享",
                onClick = {
                    scope.launch {
                        val file = viewModel.exportFile()
                        val uri = runCatching {
                            FileProvider.getUriForFile(
                                context,
                                context.packageName + ".fileprovider",
                                file,
                            )
                        }.getOrNull()
                        if (uri == null) {
                            exportTip = "导出失败：无法生成分享链接"
                            return@launch
                        }
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/json"
                            putExtra(Intent.EXTRA_TITLE, "芳沈手册数据导出")
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        runCatching {
                            context.startActivity(Intent.createChooser(intent, "分享数据"))
                        }.onFailure { exportTip = "导出成功，但没有可用分享应用" }
                    }
                },
            )
            DataCard(
                title = "复制数据到剪切板",
                description = "整份 JSON 文本直接复制到系统剪切板，可粘贴到任意应用。",
                buttonText = "复制到剪切板",
                onClick = {
                    scope.launch {
                        val json = viewModel.exportJson()
                        val clipboard = context
                            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(
                            ClipData.newPlainText("芳沈手册数据", json),
                        )
                        exportTip = "已复制 " + json.length + " 字符到剪切板"
                    }
                },
            )
            if (exportTip != null) {
                Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = exportTip!!,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
            DataCard(
                title = "清空台账记录",
                description = "删除全部服药、笔记、照片、语音、文件与日程条目。药物目录与自定义药物保留。",
                buttonText = "清空记录",
                onClick = { clearEntries = true },
            )
            DataCard(
                title = "清空全部本地数据",
                description = "删除台账记录与个人画像数据。自定义药物保留。",
                buttonText = "清空全部",
                onClick = { clearAll = true },
            )
        }
    }

    if (clearEntries) {
        ConfirmDialog(
            title = "清空记录",
            message = "确认删除全部台账条目吗？此操作不可恢复。",
            confirmText = "清空",
            onConfirm = {
                clearEntries = false
                viewModel.clearAllEntries(onDone = {})
            },
            onDismiss = { clearEntries = false },
        )
    }
    if (clearAll) {
        ConfirmDialog(
            title = "清空全部数据",
            message = "确认删除全部台账条目与个人画像数据吗？此操作不可恢复。",
            confirmText = "清空",
            onConfirm = {
                clearAll = false
                viewModel.clearAllUserData(onDone = {})
            },
            onDismiss = { clearAll = false },
        )
    }
}

@Composable
private fun DataCard(
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit,
) {
    Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            androidx.compose.material3.OutlinedButton(
                onClick = onClick,
                shape = RoundedCornerShape(50),
            ) { Text(buttonText) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    SimpleScaffold(title = "关于", onBack = onBack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "芳沈手册",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "版本 1.0.0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "一款面向精神科病人的药物手册类应用：记录服药与各类台账条目，" +
                            "示意性地推算血药浓度与效应权重变化，并提供代谢酶层面的相互作用提示。",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "全部推算均为示意级模型（单室 Bateman 方程、效应线规则、简单权重抵消），" +
                            "未考虑临床药理学的完整复杂性，置信度很低。应用的核心价值是记录本身。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisclaimerScreen(onBack: () -> Unit) {
    SimpleScaffold(title = "免责声明", onBack = onBack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "重要提示：请先阅读",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "本应用不是医疗器械或医疗软件，不提供任何诊断、治疗或用药建议。" +
                            "应用内的血药浓度曲线、效应权重与相互作用提示全部是" +
                            "示意级别的简化模型，存在大量假设（固定表观分布容积、线性药代动力学、" +
                            "简单权重抵消等），置信度很低，仅可作为记录的视觉辅助，" +
                            "绝不能作为调整剂量、停药或合用药物的依据。",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "用药请务必遵医嘱。如出现严重不适、自杀冲动或中毒表现，请立即寻求专业医疗帮助。",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
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

package com.fangshen.manual.ui.docs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.fangshen.manual.data.DocEntry
import com.fangshen.manual.data.DocsRepository
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** 文档库：按种类分组的通俗读物列表（内置 + 用户自建） */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocsScreen(
    onBack: () -> Unit,
    onOpenDoc: (String) -> Unit,
) {
    val app = LocalContext.current.applicationContext as com.fangshen.manual.FangShenApplication
    val repo = remember { DocsRepository(app, app.jsonDataStore) }
    val storeState by app.jsonDataStore.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var editing by remember { mutableStateOf<DocEntry?>(null) }
    var deleting by remember { mutableStateOf<DocEntry?>(null) }

    val docs = remember(storeState) {
        repo.all().sortedBy { doc ->
            val catIndex = repo.categories().indexOf(doc.category)
            catIndex * 1000 + doc.order
        }
    }
    val categories = remember(storeState) { repo.categories() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("文档", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Rounded.ArrowBack, contentDescription = "返回") }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editing = DocEntry(
                    id = "", category = "", title = "", subtitle = "", preface = "", content = "",
                    order = 0, isCustom = true,
                )
            }) {
                Icon(Icons.Rounded.Add, contentDescription = "新建文档")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            categories.forEach { category ->
                val inCategory = docs.filter { it.category == category }
                if (inCategory.isEmpty()) return@forEach
                item(key = "cat_$category") {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 14.dp, start = 4.dp),
                    )
                }
                items(inCategory, key = { it.id }) { doc ->
                    DocCard(
                        doc = doc,
                        onClick = { onOpenDoc(doc.id) },
                        onEdit = if (doc.isCustom) ({ editing = doc }) else null,
                        onDelete = if (doc.isCustom) ({ deleting = doc }) else null,
                    )
                }
            }
            item { Box(modifier = Modifier.padding(24.dp)) {} }
        }
    }

    editing?.let { doc ->
        DocEditDialog(
            initial = doc,
            categories = categories,
            onDismiss = { editing = null },
            onConfirm = { category, title, subtitle, content ->
                scope.launch(Dispatchers.IO) {
                    repo.saveCustom(doc.id, category, title, subtitle, content)
                }
                editing = null
            },
        )
    }
    deleting?.let { doc ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("删除文档") },
            text = { Text("确认删除「${doc.title}」吗？") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch(Dispatchers.IO) { repo.deleteCustom(doc.id) }
                    deleting = null
                }) { Text("删除") }
            },
            dismissButton = { TextButton(onClick = { deleting = null }) { Text("取消") } },
        )
    }
}

@Composable
private fun DocCard(
    doc: DocEntry,
    onClick: () -> Unit,
    onEdit: (() -> Unit)?,
    onDelete: (() -> Unit)?,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(doc.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (doc.subtitle.isNotBlank()) {
                    Text(
                        doc.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (onEdit != null) {
                IconButton(onClick = onEdit) { Icon(Icons.Rounded.Edit, contentDescription = "编辑") }
            }
            if (onDelete != null) {
                IconButton(onClick = onDelete) { Icon(Icons.Rounded.Delete, contentDescription = "删除") }
            }
        }
    }
}

/** 新建/编辑自建文档：标题、分类（可选择或填写新分类）、副标题、正文 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DocEditDialog(
    initial: DocEntry,
    categories: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (category: String, title: String, subtitle: String, content: String) -> Unit,
) {
    var title by remember { mutableStateOf(initial.title) }
    var category by remember { mutableStateOf(initial.category.ifBlank { categories.first() }) }
    var subtitle by remember { mutableStateOf(initial.subtitle) }
    var content by remember { mutableStateOf(initial.content) }
    var menuExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial.id.isBlank()) "新建文档" else "编辑文档") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("标题") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                ExposedDropdownMenuBox(
                    expanded = menuExpanded,
                    onExpandedChange = { menuExpanded = it },
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("分类（可选已有或输入新分类）") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded) },
                    )
                    ExposedDropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        categories.forEach { name ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = { category = name; menuExpanded = false },
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("副标题（可空）") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("正文（支持标题/引用/列表/表格/粗体/[[跳转]]）") },
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isBlank()) return@TextButton
                    onConfirm(category.trim(), title.trim(), subtitle.trim(), content)
                },
                enabled = title.isNotBlank(),
            ) { Text("保存") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
    )
}

package com.fangshen.manual.ui.drug

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.ManagementClass
import com.fangshen.manual.model.SubjectiveTagColors
import com.fangshen.manual.ui.theme.FangShenTheme
import com.fangshen.manual.util.LongCapture
import com.fangshen.manual.ui.components.Tag
import com.fangshen.manual.ui.navigation.Routes

/** 未归类（没有主观标签的药物）的分组名 */
private const val UNGROUPED = "未分类"

/** 未分类分组的小竖条颜色（灰色：表示没有主观标签，区别于“不在表中”的黑色） */
private val UngroupedGray = Color(0xFFB8B3C4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrugListScreen(
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: DrugListViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    // 长按选中的药物（弹操作对话框）
    var actionTarget by remember { mutableStateOf<DrugInfo?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("药物管理", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate(Routes.drugEdit("new")) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "添加药物")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::setQuery,
                placeholder = { Text("搜索药物、类别或分子标签") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            if (state.visible.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                ) {
                    Text(
                        text = "没有匹配的药物",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            // 按第一个主观标签分组：内置标签按固定顺序，其余按名称，无标签归入“未分类”
            val groups = remember(state.visible) { groupByFirstTag(state.visible) }
            LazyColumn(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 16.dp, end = 16.dp, bottom = 96.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                groups.forEach { group ->
                    item(key = "header_" + group.label) { TagGroupHeader(group) }
                    items(items = group.drugs, key = { it.id }) { drug ->
                        DrugRow(
                            drug = drug,
                            onClick = { onNavigate(Routes.drugDetail(drug.id)) },
                            onLongClick = { actionTarget = drug },
                        )
                    }
                }
            }
        }
    }

    actionTarget?.let { drug ->
        DrugActionDialog(
            drug = drug,
            onEdit = {
                actionTarget = null
                onNavigate(Routes.drugEdit(drug.id))
            },
            onShare = {
                actionTarget = null
                viewModel.shareDrugJson(drug)
            },
            onScreenshot = {
                actionTarget = null
                val target = drug
                LongCapture.capture(context, "药物_" + target.name) {
                    FangShenTheme { DrugDetailContent(target) }
                }
            },
            onDelete = {
                actionTarget = null
                viewModel.deleteDrug(drug) { }
            },
            onDismiss = { actionTarget = null },
        )
    }
}

@Composable
private fun DrugActionDialog(
    drug: DrugInfo,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onScreenshot: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(drug.name) },
        text = {
            Column {
                // 内置药物不支持编辑/删除
                if (!drug.isBuiltin) {
                    TextButton(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "编辑",
                            textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                        )
                    }
                    TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                        Text("删除", color = MaterialTheme.colorScheme.error)
                    }
                } else {
                    Text(
                        text = "内置药物，不支持编辑与删除",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
                TextButton(onClick = onScreenshot, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "截图保存到相册",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                    )
                }
                TextButton(onClick = onShare, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "分享为 .json",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}

/** 分组：标签、颜色、组内药物 */
private data class TagGroup(
    val label: String,
    val color: Color,
    val drugs: List<DrugInfo>,
)

private fun groupByFirstTag(drugs: List<DrugInfo>): List<TagGroup> {
    val buckets = LinkedHashMap<String, MutableList<DrugInfo>>()
    for (drug in drugs) {
        val raw = drug.subjectiveTags.firstOrNull()
        val key = if (raw == null) UNGROUPED else SubjectiveTagColors.canonicalOf(raw)
        buckets.getOrPut(key) { mutableListOf() } += drug
    }
    val known = SubjectiveTagColors.BUILT_IN_ORDER.filter { buckets.containsKey(it) }
    val others = buckets.keys
        .filter { it != UNGROUPED && it !in SubjectiveTagColors.BUILT_IN_ORDER }
        .sorted()
    val order = known + others + (if (buckets.containsKey(UNGROUPED)) listOf(UNGROUPED) else emptyList())
    return order.map { label ->
        TagGroup(
            label = label,
            color = if (label == UNGROUPED) UngroupedGray else Color(SubjectiveTagColors.colorOf(label)),
            drugs = buckets[label]!!,
        )
    }
}

@Composable
private fun TagGroupHeader(group: TagGroup) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 6.dp, bottom = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .width(4.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(group.color),
        )
        Text(
            text = group.label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
private fun DrugRow(drug: DrugInfo, onClick: () -> Unit, onLongClick: () -> Unit) {
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                // 名称左侧圆角小竖条：颜色取自第一个主观标签
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .width(4.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(SubjectiveTagColors.colorOf(drug))),
                    )
                    Text(
                        text = drug.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Text(
                    text = ManagementClass.fromName(drug.managementClass.name).label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                drug.subjectiveTags.forEach { Tag(it, color = SubjectiveTagYellow) }
                drug.effectTags.forEach { Tag(it, color = EffectTagBlue) }
                if (drug.moleculeTag.isNotBlank()) {
                    Tag(drug.moleculeTag, color = MoleculeTagPink)
                }
            }
            Text(
                text = "半衰期 ${drug.halfLifeHours} 小时 · 生物利用度 ${drug.bioavailabilityPct}% · " +
                    "${drug.route.label}给药",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}


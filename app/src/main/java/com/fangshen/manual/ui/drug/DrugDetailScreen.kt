package com.fangshen.manual.ui.drug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Card
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fangshen.manual.model.SubjectiveTagColors
import com.fangshen.manual.ui.components.ConfirmDialog
import com.fangshen.manual.ui.components.EmptyState
import com.fangshen.manual.ui.navigation.Routes
import androidx.compose.material3.Surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrugDetailScreen(
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: DrugDetailViewModel,
) {
    val drug by viewModel.uiState.collectAsStateWithLifecycle()
    var showDelete by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .width(5.dp)
                                .height(22.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    Color(drug?.let { SubjectiveTagColors.colorOf(it) } ?: 0xFF000000L),
                                ),
                        )
                        Text(drug?.name ?: "药物详情")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    val d = drug
                    if (d != null && !d.isBuiltin) {
                        IconButton(onClick = { onNavigate(Routes.drugEdit(d.id)) }) {
                            Icon(Icons.Rounded.Edit, contentDescription = "编辑")
                        }
                        IconButton(onClick = { showDelete = true }) {
                            Icon(Icons.Rounded.Delete, contentDescription = "删除")
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        val d = drug
        if (d == null) {
            EmptyState(
                message = "正在加载药物信息…",
                modifier = Modifier.padding(innerPadding),
            )
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            InteractionWarningCards(viewModel = viewModel)
            InteractionKnowledgeCards(viewModel = viewModel)
            DrugDetailContent(drug = d)
            if (!d.isBuiltin) {
                OutlinedButton(
                    onClick = { showDelete = true },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("删除自定义药物")
                }
            }
            Spacer(Modifier.padding(bottom = 12.dp))
        }
    }

    if (showDelete) {
        ConfirmDialog(
            title = "删除药物",
            message = "确认删除「${drug?.name ?: ""}」吗？其效应线规则也会一并删除。",
            confirmText = "删除",
            onConfirm = {
                showDelete = false
                viewModel.delete(onDone = onBack)
            },
            onDismiss = { showDelete = false },
        )
    }
}

/** 联用预警：近 7 天有在服的其他药物且存在机制冲突时以黄色条卡片提示 */
@Composable
private fun InteractionWarningCards(viewModel: DrugDetailViewModel) {
    val warnings by viewModel.interactions.collectAsStateWithLifecycle()
    if (warnings.isEmpty()) return
    warnings.forEach { w ->
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF3E0),
                contentColor = Color(0xFF5D4037),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Warning,
                        contentDescription = null,
                        tint = Color(0xFFEF6C00),
                        modifier = Modifier.padding(end = 6.dp).size(18.dp),
                    )
                    Text(
                        text = w.kind.label + "：" + w.drugName + " 与 " + w.otherDrugName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                if (w.enzyme.isNotBlank()) {
                    Text("相关机制：" + w.enzyme, style = MaterialTheme.typography.bodySmall)
                }
                Text(w.message, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


/** v3.1：联用知识——不依赖当前是否在服，凡涉及本药（标签或名称命中）的内置机制配对与
 * 用户自定义配对都展示。两侧关键词渲染为圆角矩形标签卡片。 */
@Composable
private fun InteractionKnowledgeCards(viewModel: DrugDetailViewModel) {
    val items by viewModel.knowledge.collectAsStateWithLifecycle()
    if (items.isEmpty()) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { k ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3E0),
                    contentColor = Color(0xFF5D4037),
                ),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        val tint = Color(0xFFE65100)
                        RoundedChip(k.chipA, tint)
                        Text("与", style = MaterialTheme.typography.bodySmall)
                        RoundedChip(k.chipB, tint)
                    }
                    Text(
                        text = k.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = stripKnownPrefix(k.message, k.chipA, k.chipB),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
private fun stripKnownPrefix(message: String, chipA: String, chipB: String): String {
    var m = message.trim()
    for (pair in listOf(chipA to chipB, chipB to chipA)) {
        val prefix = pair.first + " 与 " + pair.second + " 联用："
        if (m.startsWith(prefix)) return m.removePrefix(prefix)
    }
    return m
}

/** 圆角矩形标签：关键词以卡片背景突出 */
@Composable
private fun RoundedChip(text: String, tint: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = tint.copy(alpha = 0.12f),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = tint,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

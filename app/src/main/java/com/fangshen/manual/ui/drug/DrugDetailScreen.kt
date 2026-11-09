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

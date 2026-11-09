package com.fangshen.manual.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.fangshen.manual.model.ReportCharacter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportReaderScreen(
    fileName: String,
    onBack: () -> Unit,
    viewModel: ReportReaderViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    // 当前展开备注的角色（报告者说明弹窗）
    var noteCharacter by remember { mutableStateOf<ReportCharacter?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        (state as? ReaderState.Success)?.report?.head?.name?.ifEmpty { fileName }
                            ?: fileName,
                        fontWeight = FontWeight.SemiBold,
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
        when (val s = state) {
            ReaderState.Loading -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize().padding(innerPadding),
            ) { CircularProgressIndicator() }

            is ReaderState.Error -> Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(s.message, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
                Text(
                    "请在列表长按该报告选择编辑，修正格式后再阅读。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            is ReaderState.Success -> ReportBody(
                report = s.report,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onCharacterClick = { noteCharacter = it },
            )
        }
    }

    noteCharacter?.let { c ->
        AlertDialog(
            onDismissRequest = { noteCharacter = null },
            title = { Text(c.name) },
            text = { Text(c.note.orEmpty()) },
            confirmButton = {
                TextButton(onClick = { noteCharacter = null }) { Text("知道了") }
            },
        )
    }
}

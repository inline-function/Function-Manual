package com.fangshen.manual.ui.docs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fangshen.manual.data.DocsRepository
import com.fangshen.manual.ui.LocalApplication
import com.fangshen.manual.ui.markdown.MarkdownDoc

/** 文档阅读页：主/次标题 + 前言引用块 + 正文（锚点与跨文档跳转） */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocReaderScreen(
    docId: String,
    onBack: () -> Unit,
    onOpenDoc: (String) -> Unit,
) {
    val app = LocalApplication.current
    val repo = remember { DocsRepository(app) }
    val doc = remember(docId) { repo.get(docId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(doc?.title ?: "文档", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Rounded.ArrowBack, contentDescription = "返回") }
                },
            )
        },
    ) { padding ->
        if (doc == null) {
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
                Text("文档不存在", style = MaterialTheme.typography.bodyLarge)
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)

                .padding(horizontal = 16.dp)

        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    doc.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                if (doc.subtitle.isNotBlank()) {
                    Text(
                        doc.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            MarkdownDoc(
                content = (if (doc.preface.isNotBlank()) "> " + doc.preface.replace("\n", "\n> ") + "\n\n" else "") + doc.content,
                onDocJump = { id, heading -> onOpenDoc(id) },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
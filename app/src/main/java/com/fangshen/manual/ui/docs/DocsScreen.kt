package com.fangshen.manual.ui.docs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
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
import com.fangshen.manual.data.DocEntry
import com.fangshen.manual.data.DocsRepository
import com.fangshen.manual.ui.LocalApplication
import com.fangshen.manual.ui.theme.TopBarTeal

/** 文档库：按固定种类分组的通俗读物列表 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocsScreen(
    onBack: () -> Unit,
    onOpenDoc: (String) -> Unit,
) {
    val app = LocalApplication.current
    val repo = remember { DocsRepository(app) }
    val docs = remember { repo.all().sortedBy { DocsRepository.CATEGORIES.indexOf(it.category) * 100 + it.order } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("文档", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Rounded.ArrowBack, contentDescription = "返回") }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            DocsRepository.CATEGORIES.forEach { category ->
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
                items(inCategory, key = { it.id }) { doc -> DocCard(doc, onClick = { onOpenDoc(doc.id) }) }
            }
            item { Column(modifier = Modifier.padding(24.dp)) {} }
        }
    }
}

@Composable
private fun DocCard(doc: DocEntry, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(doc.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            if (doc.subtitle.isNotBlank()) {
                Text(
                    doc.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
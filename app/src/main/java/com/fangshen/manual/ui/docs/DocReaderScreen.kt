package com.fangshen.manual.ui.docs

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fangshen.manual.data.DocsRepository
import com.fangshen.manual.ui.LocalApplication
import com.fangshen.manual.ui.markdown.MarkdownDoc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** 文档阅读页：主/次标题 + 前言引用块 + 正文（锚点与跨文档跳转） */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocReaderScreen(
    docId: String,
    onBack: () -> Unit,
    onOpenDoc: (String) -> Unit,
) {
    val app = LocalContext.current.applicationContext as com.fangshen.manual.FangShenApplication
    val repo = remember { DocsRepository(app) }
    val doc = remember(docId) { repo.get(docId) }
    val view = LocalView.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(doc?.title ?: "文档", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Rounded.ArrowBack, contentDescription = "返回") }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            val msg = saveDocScreenshot(context, view, doc?.title ?: "文档")
                            snackbar.showSnackbar(msg)
                        }
                    }) { Icon(Icons.Rounded.PhotoCamera, contentDescription = "保存截图到相册") }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
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

/** 截取当前页面位图并保存到相册 Pictures/芳沈手册 */
private suspend fun saveDocScreenshot(context: Context, view: android.view.View, title: String): String =
    withContext(Dispatchers.IO) {
        runCatching {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            val name = "芳沈手册_" + title.replace(Regex("[/\\:*?\"<>|]"), "_") +
                "_" + System.currentTimeMillis() + ".png"
            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/芳沈手册")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }
            val uri = context.contentResolver.insert(collection, values)
                ?: return@withContext "保存失败：无法创建文件"
            context.contentResolver.openOutputStream(uri)?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            } ?: return@withContext "保存失败：无法写入"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                context.contentResolver.update(uri, values, null, null)
            }
            "截图已保存到相册：Pictures/芳沈手册/$name"
        }.getOrElse { "保存失败：${it.message ?: "未知错误"}" }
    }

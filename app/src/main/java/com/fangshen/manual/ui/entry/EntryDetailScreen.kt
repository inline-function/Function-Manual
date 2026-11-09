package com.fangshen.manual.ui.entry

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fangshen.manual.core.formatDuration
import com.fangshen.manual.core.formatFullDateTime
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.model.DoseUnit
import com.fangshen.manual.model.EntryType
import com.fangshen.manual.model.Route
import com.fangshen.manual.ui.components.ConfirmDialog
import com.fangshen.manual.ui.components.EmptyState
import com.fangshen.manual.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryDetailScreen(
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: EntryDetailViewModel,
) {
    val entry by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showDelete by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(EntryType.fromName(entry?.type).label) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { entry?.let { onNavigate(Routes.entryEdit(it.id)) } }) {
                        Icon(Icons.Rounded.Edit, contentDescription = "编辑")
                    }
                    IconButton(onClick = { showDelete = true }) {
                        Icon(Icons.Rounded.Delete, contentDescription = "删除")
                    }
                },
            )
        },
    ) { innerPadding ->
        val e = entry
        if (e == null) {
            EmptyState(message = "正在加载条目…", modifier = Modifier.padding(innerPadding))
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = e.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${EntryType.fromName(e.type).label} · ${formatFullDateTime(e.eventTime)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (e.type == "DOSE") {
                val unit = DoseUnit.fromName(e.doseUnit)
                Text(
                    text = "剂量：${com.fangshen.manual.core.formatDoseBD(e.doseAmount?.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO)} ${unit.shortLabel}（${Route.fromName(e.route).label}）",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            if (e.type == "SCHEDULE") {
                Text(
                    text = if (e.notify) "已设置到点提醒" else "未设置提醒",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            e.content?.takeIf { it.isNotBlank() }?.let { content ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
            e.attachmentUri?.let { uri ->
                AttachmentViewer(
                    uri = uri,
                    type = e.type,
                    durationMs = e.durationMs,
                    context = context,
                )
            }
            Box(Modifier.padding(bottom = 24.dp))
        }
    }

    if (showDelete) {
        ConfirmDialog(
            title = "删除条目",
            message = "确认删除「${entry?.title ?: ""}」吗？",
            confirmText = "删除",
            onConfirm = {
                showDelete = false
                viewModel.delete(onDone = onBack)
            },
            onDismiss = { showDelete = false },
        )
    }
}

@Composable
private fun AttachmentViewer(
    uri: String,
    type: String,
    durationMs: Long?,
    context: Context,
) {
    val parsed = Uri.parse(uri)
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            val displayName = remember(uri) {
                runCatching {
                    context.contentResolver.query(parsed, null, null, null, null)?.use { cursor ->
                        val idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                        if (idx >= 0 && cursor.moveToFirst()) cursor.getString(idx) else null
                    } ?: parsed.lastPathSegment
                }.getOrNull() ?: parsed.lastPathSegment
            }
            when (type) {
                "PHOTO" -> {
                    val bitmap = remember(uri) {
                        runCatching {
                            android.graphics.ImageDecoder.decodeBitmap(
                                android.graphics.ImageDecoder.createSource(context.contentResolver, parsed),
                            ) { decoder, _, _ ->
                                decoder.setTargetSize(1080, 1080)
                            }
                        }.getOrNull()
                    }
                    bitmap?.let {
                        androidx.compose.foundation.Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "照片",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                        )
                    }
                }
                "VOICE" -> {
                    durationMs?.let { Text("时长：${formatDuration(it)}") }
                    var player: MediaPlayer? by remember { mutableStateOf(null) }
                    var playing by remember { mutableStateOf(false) }
                    DisposableEffect(uri) {
                        onDispose {
                            player?.release()
                        }
                    }
                    Button(onClick = {
                        if (playing) {
                            player?.pause()
                            playing = false
                        } else {
                            try {
                                val p = MediaPlayer().apply {
                                    setDataSource(context, parsed)
                                    prepare()
                                    setOnCompletionListener { playing = false }
                                }
                                p.start()
                                player = p
                                playing = true
                            } catch (e: Exception) {
                                playing = false
                            }
                        }
                    }) {
                        Icon(
                            imageVector = if (playing) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
                            contentDescription = null,
                        )
                        Text(if (playing) "停止" else "播放", modifier = Modifier.padding(start = 8.dp))
                    }
                }
                else -> {
                    TextButton(onClick = {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                            setDataAndType(parsed, context.contentResolver.getType(parsed))
                            addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        runCatching { context.startActivity(intent) }
                    }) {
                        Text("打开文件：${displayName ?: "未知"}")
                    }
                }
            }
        }
    }
}

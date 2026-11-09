package com.fangshen.manual.ui.entry

import android.Manifest
import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fangshen.manual.core.formatDuration
import com.fangshen.manual.core.formatEventTime
import com.fangshen.manual.model.EntryType
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryEditScreen(
    onBack: () -> Unit,
    viewModel: EntryEditViewModel,
) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val app = context.applicationContext as Application

    // 通知权限（日程提醒需要）
    val notifPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { }
    fun requestNotifPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            notifPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // 语音录制
    var hasMicPermission by remember { mutableStateOf(false) }
    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted -> hasMicPermission = granted }
    LaunchedEffect(Unit) {
        hasMicPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
        } else true
        if (!hasMicPermission) micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
    var recorder: MediaRecorder? by remember { mutableStateOf(null) }
    var recording by remember { mutableStateOf(false) }
    var recordingFile: File? by remember { mutableStateOf(null) }
    var recordStartMs by remember { mutableStateOf(0L) }
    var previewPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var previewPlaying by remember { mutableStateOf(false) }
    LaunchedEffect(form.attachmentUri) {
        // 音频预览：停止播放
        previewPlayer?.release()
        previewPlayer = null
        previewPlaying = false
    }
    DisposableEffect(Unit) {
        onDispose {
            recorder?.stop()
            recorder?.release()
            previewPlayer?.release()
        }
    }

    EntryScaffold(
        title = EntryType.fromName(viewModel.type).label,
        onBack = onBack,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            form.error?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            OutlinedTextField(
                value = form.title,
                onValueChange = { v -> viewModel.update { it.copy(title = v) } },
                label = { Text("标题") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = form.content,
                onValueChange = { v -> viewModel.update { it.copy(content = v) } },
                label = { Text("内容") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            )

            when (EntryType.fromName(viewModel.type)) {
                EntryType.VOICE -> {
                    Text(
                        text = if (form.durationMs != null) {
                            "已录制 ${formatDuration(form.durationMs!!)}"
                        } else {
                            "点击麦克风开始录音"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = {
                                if (recording) {
                                    recorder?.stop()
                                    recorder?.release()
                                    recorder = null
                                    recording = false
                                    val ms = System.currentTimeMillis() - recordStartMs
                                    viewModel.setDuration(ms)
                                    recordingFile?.let { file ->
                                        viewModel.update { it.copy(attachmentUri = file.toURI().toString()) }
                                    }
                                } else {
                                    try {
                                        val dir = File(app.filesDir, "voice").apply { mkdirs() }
                                        val file = File(dir, "voice_${System.currentTimeMillis()}.aac")
                                        val r = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                            MediaRecorder(app)
                                        } else {
                                            @Suppress("DEPRECATION")
                                            MediaRecorder()
                                        }
                                        r.setAudioSource(MediaRecorder.AudioSource.MIC)
                                        r.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
                                        r.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                                        r.setOutputFile(file.absolutePath)
                                        r.prepare()
                                        r.start()
                                        recorder = r
                                        recording = true
                                        recordStartMs = System.currentTimeMillis()
                                        recordingFile = file
                                    } catch (e: Exception) {
                                        recording = false
                                        recorder = null
                                        viewModel.update { it.copy(error = "录音失败：${e.message}") }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(50),
                            enabled = hasMicPermission,
                        ) {
                            Icon(
                                imageVector = if (recording) Icons.Rounded.Stop else Icons.Rounded.Mic,
                                contentDescription = if (recording) "停止" else "录音",
                            )
                            Text(
                                text = if (recording) "停止录音" else "开始录音",
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                        form.attachmentUri?.let { uri ->
                            TextButton(onClick = {
                                if (previewPlaying) {
                                    previewPlayer?.pause()
                                    previewPlaying = false
                                } else {
                                    try {
                                        val player = MediaPlayer().apply {
                                            setDataSource(context, Uri.parse(uri))
                                            prepare()
                                            setOnCompletionListener {
                                                previewPlaying = false
                                            }
                                        }
                                        player.start()
                                        previewPlayer = player
                                        previewPlaying = true
                                    } catch (e: Exception) {
                                        previewPlaying = false
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = if (previewPlaying) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
                                    contentDescription = null,
                                )
                                Text("试听", modifier = Modifier.padding(start = 4.dp))
                            }
                        }
                    }
                    if (!hasMicPermission) {
                        Text(
                            text = "需要麦克风权限才能录音。请授予 RECORD_AUDIO 权限。",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                        TextButton(onClick = { micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }) {
                            Text("重新请求权限")
                        }
                    }
                }
                EntryType.SCHEDULE -> {
                    OutlinedTextField(
                        value = formatEventTime(form.eventTime),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("日程时间") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "到点提醒",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = "在日程时间发送通知提醒",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Switch(
                            checked = form.notify,
                            onCheckedChange = { v ->
                                viewModel.update { it.copy(notify = v) }
                                if (v) requestNotifPermission()
                            },
                        )
                    }
                    DateTimePickers(
                        timeMillis = form.eventTime,
                        onTimeChange = { millis -> viewModel.update { it.copy(eventTime = millis) } },
                    )
                }
                else -> {
                    form.attachmentUri?.let { uri ->
                        AttachmentSummary(
                            uri = uri,
                            type = viewModel.type,
                            context = context,
                        )
                    }
                }
            }
            Button(
                onClick = { viewModel.save(form.title, onDone = onBack) },
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("保存")
            }
            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("取消")
            }
            Box(Modifier.padding(bottom = 24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimePickers(
    timeMillis: Long,
    onTimeChange: (Long) -> Unit,
) {
    var showDate by remember { mutableStateOf(false) }
    var showTime by remember { mutableStateOf(false) }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TextButton(onClick = { showDate = true }) { Text("选择日期") }
        TextButton(onClick = { showTime = true }) { Text("选择时间") }
    }
    if (showDate) {
        val state = androidx.compose.material3.rememberDatePickerState(
            initialSelectedDateMillis = timeMillis,
        )
        androidx.compose.material3.DatePickerDialog(
            onDismissRequest = { showDate = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { onTimeChange(it) }
                    showDate = false
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showDate = false }) { Text("取消") }
            },
        ) {
            androidx.compose.material3.DatePicker(state = state)
        }
    }
    if (showTime) {
        val cal = java.util.Calendar.getInstance().apply { timeInMillis = timeMillis }
        val state = androidx.compose.material3.rememberTimePickerState(
            initialHour = cal.get(java.util.Calendar.HOUR_OF_DAY),
            initialMinute = cal.get(java.util.Calendar.MINUTE),
            is24Hour = true,
        )
        androidx.compose.material3.TimePickerDialog(
            onDismissRequest = { showTime = false },
            confirmButton = {
                TextButton(onClick = {
                    val newCal = java.util.Calendar.getInstance().apply {
                        timeInMillis = timeMillis
                        set(java.util.Calendar.HOUR_OF_DAY, state.hour)
                        set(java.util.Calendar.MINUTE, state.minute)
                    }
                    onTimeChange(newCal.timeInMillis)
                    showTime = false
                }) { Text("确定") }
            },
            title = { Text("选择时间") },
            dismissButton = {
                TextButton(onClick = { showTime = false }) { Text("取消") }
            },
        ) {
            androidx.compose.material3.TimePicker(state = state)
        }
    }
}

@Composable
private fun AttachmentSummary(
    uri: String,
    type: String,
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
            Text(
                text = "附件：" + (displayName ?: "未知"),
                style = MaterialTheme.typography.bodyMedium,
            )
            if (type == "PHOTO") {
                val bitmap = remember(uri) {
                    runCatching {
                        android.graphics.ImageDecoder.decodeBitmap(
                            android.graphics.ImageDecoder.createSource(context.contentResolver, parsed),
                        ) { decoder, _, _ ->
                            decoder.setTargetSize(720, 720)
                        }
                    }.getOrNull()
                }
                bitmap?.let {
                    androidx.compose.foundation.Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "照片预览",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EntryScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit,
) {
    androidx.compose.material3.Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            androidx.compose.material3.TopAppBar(
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

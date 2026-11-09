package com.fangshen.manual.util

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * 把给定 JSON 文本写入缓存目录并经 FileProvider 以 application/json 分享。
 */
object FileShare {

    fun shareJson(context: Context, displayName: String, content: String, chooserTitle: String) {
        val dir = File(context.cacheDir, "shared").apply { mkdirs() }
        val file = File(dir, displayName)
        file.writeText(content)
        val authority = "${context.packageName}.fileprovider"
        val uri: Uri = FileProvider.getUriForFile(context, authority, file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        // 从 Application 等非 Activity 上下文启动需要 NEW_TASK
        val chooser = Intent.createChooser(intent, chooserTitle)
            .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        context.startActivity(chooser)
    }
}

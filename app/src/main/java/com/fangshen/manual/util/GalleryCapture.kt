package com.fangshen.manual.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 把位图写入系统相册 Pictures/芳沈手册（Android 10+ scoped storage）。
 * LongCapture 完成离屏渲染后调用本对象落盘。
 */
object GalleryCapture {

    private const val TAG = "GalleryCapture"

    /** 把位图写入相册；成功返回 true */
    fun saveToGallery(context: Context, bitmap: Bitmap, label: String): Boolean = runCatching {
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val name = label.replace(Regex("[\\/:*?\"<>|]"), "_").trim().ifEmpty { "view" }
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "${name}_${stamp}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/芳沈手册")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: return@runCatching false
        resolver.openOutputStream(uri)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val done = ContentValues().apply { put(MediaStore.Images.Media.IS_PENDING, 0) }
            resolver.update(uri, done, null, null)
        }
        // 主动通知媒体扫描，让系统相册立刻刷新出这张图
        scanForGallery(context, uri)
        true
    }.getOrElse { t ->
        Log.e(TAG, "写入相册失败", t)
        false
    }

    /** 按 MediaStore uri 查出真实路径，再让 MediaScanner 扫描该文件 */
    private fun scanForGallery(context: Context, uri: Uri) {
        runCatching {
            context.contentResolver.query(
                uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null,
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val path = cursor.getString(0)
                    if (!path.isNullOrEmpty()) {
                        MediaScannerConnection.scanFile(
                            context, arrayOf(path), arrayOf("image/png"),
                        ) { _, _ -> }
                    }
                }
            }
        }
    }
}

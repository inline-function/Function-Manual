package com.fangshen.manual.data

import android.content.Context
import java.io.File

/**
 * 处方仓库：JSON 文件存于外部私有目录 fangshen/prescriptions/，
 * 可直接通过 FileProvider 分享。
 */
class PrescriptionRepository(private val context: Context) {

    private val dir: File
        get() {
            val external = context.getExternalFilesDir(null) ?: context.filesDir
            return File(external, "fangshen/prescriptions").apply { if (!exists()) mkdirs() }
        }

    fun listFiles(): List<File> =
        dir.listFiles { f -> f.isFile && f.name.endsWith(".json") }
            ?.sortedByDescending { it.lastModified() } ?: emptyList()

    fun displayName(file: File): String = file.name.removeSuffix(".json")

    fun read(displayName: String): String? = fileOf(displayName).let { f ->
        if (f.isFile) runCatching { f.readText() }.getOrNull() else null
    }

    fun write(displayName: String, content: String) {
        fileOf(displayName).writeText(content)
    }

    fun exists(displayName: String): Boolean = fileOf(displayName).isFile

    fun delete(displayName: String): Boolean = fileOf(displayName).delete()

    fun fileOf(displayName: String): File {
        val safe = displayName.trim().replace('/', '_').replace('\\', '_')
        return File(dir, "$safe.json")
    }
}

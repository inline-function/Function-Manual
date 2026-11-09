package com.fangshen.manual.data

import android.content.Context
import java.io.File

/**
 * 报告文件仓库：.rp 文件统一存放在外部私有目录 fangshen/reports/ 下，
 * 与 data.json 同级，可直接通过 FileProvider 分享。
 */
class ReportRepository(private val context: Context) {

    private val dir: File
        get() {
            val external = context.getExternalFilesDir(null)
                ?: context.filesDir
            return File(external, "fangshen/reports").apply { if (!exists()) mkdirs() }
        }

    /** 列出全部报告文件，按修改时间倒序 */
    fun listFiles(): List<File> =
        dir.listFiles { f -> f.isFile && f.name.endsWith(".rp") }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()

    /** 文件显示名（不含 .rp 后缀） */
    fun displayName(file: File): String = file.name.removeSuffix(".rp")

    /** 报告内容；文件不存在返回 null */
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
        return File(dir, "$safe.rp")
    }

    companion object {
        /** 已存在的报告名重复时自动追加序号 */
        fun uniqueName(base: String, exists: (String) -> Boolean): String {
            if (!exists(base)) return base
            var i = 2
            while (exists("$base（$i）")) i++
            return "$base（$i）"
        }
    }
}

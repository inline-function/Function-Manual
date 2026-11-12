package com.fangshen.manual.data

import android.content.Context
import com.fangshen.manual.model.CustomDoc
import org.json.JSONObject

/**
 * 文档库：五元素（种类/主标题/次标题/前言/内容）文档，打包在 assets/docs/docs.json。
 * v5：新增内置分类「认知行为疗法」，并支持用户自建分类与自建文档（持久化在 JsonDataStore）。
 */
data class DocEntry(
    val id: String,
    val category: String,
    val title: String,
    val subtitle: String,
    val preface: String,
    val content: String,
    val order: Int = 0,
    val isCustom: Boolean = false,
)

class DocsRepository(
    private val context: Context,
    private val store: JsonDataStore? = null,
) {

    companion object {
        const val CUSTOM_PREFIX = "custom:"

        /** 内置分类（顺序即展示顺序） */
        val BUILTIN_CATEGORIES = listOf(
            "药代动力学",
            "药效动力学",
            "药物化学",
            "精神病理学",
            "认知行为疗法",
            "精神分析学",
        )

        // 兼容旧引用
        val CATEGORIES: List<String> get() = BUILTIN_CATEGORIES
    }

    /** 用户自建文档（来自 JsonDataStore 的当前快照） */
    val customDocs: List<CustomDoc> get() = store?.state?.value?.customDocs ?: emptyList()

    /** 全部分类：内置在前，用户新建分类按字母序追加 */
    fun categories(): List<String> {
        val customCats = customDocs.map { it.category }.filter { it !in BUILTIN_CATEGORIES }.distinct()
        return BUILTIN_CATEGORIES + customCats
    }

    fun all(): List<DocEntry> = builtinAll() + customDocs.map {
        DocEntry(
            id = CUSTOM_PREFIX + it.id,
            category = it.category,
            title = it.title,
            subtitle = it.subtitle,
            preface = "",
            content = it.content,
            order = it.order,
            isCustom = true,
        )
    }

    private fun builtinAll(): List<DocEntry> = runCatching {
        val json = context.assets.open("docs/docs.json").bufferedReader().use { it.readText() }
        val array = JSONObject(json).getJSONArray("docs")
        List(array.length()) { i ->
            val o = array.getJSONObject(i)
            DocEntry(
                id = o.getString("id"),
                category = o.optString("category", ""),
                title = o.optString("title", ""),
                subtitle = o.optString("subtitle", ""),
                preface = o.optString("preface", ""),
                content = o.optString("content", ""),
                order = o.optInt("order", 0),
            )
        }
    }.getOrDefault(emptyList())

    fun byCategory(category: String): List<DocEntry> =
        all().filter { it.category == category }.sortedBy { it.order }

    /** 读取单篇：id 精确匹配，其次按标题匹配（[[文档标题]] 跳转） */
    fun get(id: String): DocEntry? = all().firstOrNull { it.id == id || it.title == id }

    /** 新建/更新自建文档 */
    suspend fun saveCustom(
        rawId: String?,
        category: String,
        title: String,
        subtitle: String,
        content: String,
    ): String = store?.write { data ->
        val id = rawId?.removePrefix(CUSTOM_PREFIX)?.ifBlank { null } ?: ("doc" + System.currentTimeMillis())
        val now = System.currentTimeMillis()
        val others = data.customDocs.filter { it.id != id }
        val order = (others.filter { it.category == category }.maxOfOrNull { it.order } ?: 0) + 1
        data.copy(customDocs = others + CustomDoc(id, category, title, subtitle, content, order, now)) to (CUSTOM_PREFIX + id)
    } ?: ""

    suspend fun deleteCustom(id: String) = store?.write { data ->
        val key = id.removePrefix(CUSTOM_PREFIX)
        data.copy(customDocs = data.customDocs.filter { it.id != key }) to Unit
    }

}

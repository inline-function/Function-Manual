package com.fangshen.manual.data

import android.content.Context
import org.json.JSONObject

/**
 * 文档库：五元素（种类/主标题/次标题/前言/内容）文档，打包在 assets/docs.json。
 * 种类固定为药代动力学、药效动力学、精神病理学、精神分析学、药物化学。
 */
data class DocEntry(
    val id: String,
    val category: String,
    val title: String,
    val subtitle: String,
    val preface: String,
    val content: String,
    val order: Int = 0,
)

class DocsRepository(private val context: Context) {

    companion object {
        val CATEGORIES = listOf(
            "药代动力学",
            "药效动力学",
            "精神病理学",
            "精神分析学",
            "药物化学",
        )
    }

    fun all(): List<DocEntry> = runCatching {
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

    fun get(id: String): DocEntry? = all().firstOrNull { it.id == id }
}
package com.fangshen.manual.model

/** 用户自建文档：分类可复用内置分类，也可填新的分类名 */
data class CustomDoc(
    val id: String,
    val category: String,
    val title: String,
    val subtitle: String = "",
    val content: String,
    val order: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
)

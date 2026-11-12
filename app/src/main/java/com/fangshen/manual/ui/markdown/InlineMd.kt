package com.fangshen.manual.ui.markdown

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

/**
 * 内联 Markdown 子集：**粗体**、*斜体*、~~删除线~~、`代码`。
 * 不解析任何 HTML；其余文本原样保留。报告与文档共用。
 */
fun parseInlineMd(text: String): AnnotatedString = buildAnnotatedString {
    var i = 0
    while (i < text.length) {
        when {
            text.startsWith("**", i) -> {
                val end = text.indexOf("**", i + 2)
                if (end > i + 2) {
                    append(wrap(text.substring(i + 2, end), SpanStyle(fontWeight = FontWeight.Bold)))
                    i = end + 2
                } else { append('*'); i++ }
            }
            text.startsWith("~~", i) -> {
                val end = text.indexOf("~~", i + 2)
                if (end > i + 2) {
                    append(wrap(text.substring(i + 2, end), SpanStyle(textDecoration = TextDecoration.LineThrough)))
                    i = end + 2
                } else { append('~'); i++ }
            }
            text[i] == MD_CODE -> {
                val end = text.indexOf(MD_CODE, i + 1)
                if (end > i + 1) {
                    append(wrap(text.substring(i + 1, end), SpanStyle(fontWeight = FontWeight.Medium)))
                    i = end + 1
                } else { append(MD_CODE); i++ }
            }
            text[i] == '*' -> {
                val end = text.indexOf('*', i + 1)
                if (end > i + 1) {
                    append(wrap(text.substring(i + 1, end), SpanStyle(fontStyle = FontStyle.Italic)))
                    i = end + 1
                } else { append('*'); i++ }
            }
            else -> {
                val next = nextMarker(text, i)
                append(text.substring(i, next))
                i = next
            }
        }
    }
}

private const val MD_CODE: Char = '`'

private fun nextMarker(text: String, from: Int): Int {
    var min = text.length
    for (m in listOf("**", "~~", "*", MD_CODE.toString())) {
        val idx = text.indexOf(m, from)
        if (idx in from until min) min = idx
    }
    return min
}

/** 嵌套层级只支持一次：内部不再展开标记 */
private fun wrap(s: String, style: SpanStyle): AnnotatedString = buildAnnotatedString {
    pushStyle(style)
    append(s)
    pop()
}

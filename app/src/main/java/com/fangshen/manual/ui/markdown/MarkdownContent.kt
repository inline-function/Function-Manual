package com.fangshen.manual.ui.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.text.ClickableText
import kotlinx.coroutines.launch

/**
 * Markdown 子集：##/### 标题、> 引用、-/+ 列表、| 表格、空行分段；
 * 内联 **粗体** *斜体* ~~删除线~~ `代码`；跳转 [文本](#本页标题) 与 [[文档]]、[[文档#标题]]。
 */
sealed interface MdBlock {
    data class Heading(val level: Int, val text: String) : MdBlock
    data class Paragraph(val segments: List<MdInline>) : MdBlock
    data class Quote(val text: String) : MdBlock
    data class Bullet(val items: List<List<MdInline>>) : MdBlock
    data class Table(val header: List<List<MdInline>>, val rows: List<List<List<MdInline>>>) : MdBlock
    data object Spacer : MdBlock
}

sealed interface MdInline {
    data class Plain(val text: String) : MdInline
    /** 同页标题锚点 */
    data class Anchor(val text: String, val heading: String) : MdInline
    /** 跳到另一篇文档（可带标题） */
    data class DocJump(val text: String, val docId: String, val heading: String?) : MdInline
}

fun parseMdBlocks(content: String): List<MdBlock> {
    val blocks = mutableListOf<MdBlock>()
    val lines = content.split("\n")
    var i = 0
    while (i < lines.size) {
        val line = lines[i]
        when {
            line.isBlank() -> { if (blocks.isNotEmpty()) blocks += MdBlock.Spacer; i++ }
            line.startsWith("### ") -> { blocks += MdBlock.Heading(3, line.removePrefix("### ").trim()); i++ }
            line.startsWith("## ") -> { blocks += MdBlock.Heading(2, line.removePrefix("## ").trim()); i++ }
            line.startsWith("# ") -> { blocks += MdBlock.Heading(2, line.removePrefix("# ").trim()); i++ }
            line.startsWith(">") -> {
                val sb = StringBuilder()
                while (i < lines.size && lines[i].startsWith(">")) {
                    if (sb.isNotEmpty()) sb.append("\n")
                    sb.append(lines[i].removePrefix(">").trim())
                    i++
                }
                if (sb.isNotBlank()) blocks += MdBlock.Quote(sb.toString())
            }
            line.startsWith("- ") || line.startsWith("+ ") -> {
                val items = mutableListOf<List<MdInline>>()
                while (i < lines.size && (lines[i].startsWith("- ") || lines[i].startsWith("+ "))) {
                    items += parseInlineSegments(lines[i].substring(2).trim())
                    i++
                }
                blocks += MdBlock.Bullet(items)
            }
            line.startsWith("|") -> {
                val rows = mutableListOf<List<String>>()
                while (i < lines.size && lines[i].startsWith("|")) {
                    rows += lines[i].trim().trim('|').split("|").map { it.trim() }
                    i++
                }
                // |:---| 对齐行整行只由 : - 空白组成，丢掉
                val cleaned = rows.filterIndexed { idx, r ->
                    !(idx == 1 && r.all { it.isBlank() || it.all { c -> c == ':' || c == '-' } })
                }
                if (cleaned.isNotEmpty()) {
                    blocks += MdBlock.Table(
                        header = cleaned.first().map { parseInlineSegments(it) },
                        rows = cleaned.drop(1).map { row -> row.map { parseInlineSegments(it) } },
                    )
                }
            }
            else -> {
                val sb = StringBuilder()
                while (i < lines.size && lines[i].isNotBlank() && !lines[i].startsWith("#") &&
                    !lines[i].startsWith(">") && !lines[i].startsWith("- ") &&
                    !lines[i].startsWith("+ ") && !lines[i].startsWith("|")) {
                    if (sb.isNotEmpty()) sb.append("\n")
                    sb.append(lines[i])
                    i++
                }
                blocks += MdBlock.Paragraph(parseInlineSegments(sb.toString()))
            }
        }
    }
    return blocks
}

private val jumpRegex = Regex("\\[\\[([^\\]]+)\\]\\]")
private val anchorRegex = Regex("\\[([^\\]]+)\\]\\(#([^)]+)\\)")

fun parseInlineSegments(text: String): List<MdInline> {
    val out = mutableListOf<MdInline>()
    var pos = 0
    while (pos < text.length) {
        val jump = jumpRegex.find(text, pos)
        val anchor = anchorRegex.find(text, pos)
        val next = listOfNotNull(jump, anchor).minByOrNull { it.range.first } ?: null
        if (next == null) {
            if (pos < text.length) out += MdInline.Plain(text.substring(pos))
            break
        }
        if (next.range.first > pos) out += MdInline.Plain(text.substring(pos, next.range.first))
        if (next == jump) {
            val inner = next.groupValues[1]
            val parts = inner.split("#", limit = 2)
            out += if (parts.size == 2) MdInline.DocJump(parts[0], parts[0], parts[1])
            else MdInline.DocJump(inner, inner, null)
        } else {
            out += MdInline.Anchor(next.groupValues[1], next.groupValues[2])
        }
        pos = next.range.last + 1
    }
    return out.filter { it !is MdInline.Plain || it.text.isNotEmpty() }
}

/**
 * 文档正文渲染：LazyColumn 逐块渲染；
 * 同页锚点点击滚动到对应标题，跨文档跳转交给 [onDocJump]。
 */
@Composable
fun MarkdownDoc(
    content: String,
    onDocJump: (docId: String, heading: String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val blocks = remember(content) { parseMdBlocks(content) }
    val headingIndexes = remember(blocks) {
        val map = mutableMapOf<String, Int>()
        blocks.forEachIndexed { index, b -> if (b is MdBlock.Heading) map[b.text] = index }
        map
    }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LazyColumn(state = listState, modifier = modifier.fillMaxWidth()) {
        blocks.forEachIndexed { index, block ->
            when (block) {
                is MdBlock.Heading -> item(key = "h$index") {
                    Text(
                        text = block.text,
                        style = if (block.level == 2) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 6.dp),
                    )
                }
                is MdBlock.Paragraph -> item(key = "p$index") {
                    InlineLine(
                        segments = block.segments,
                        onAnchor = { h ->
                            headingIndexes[h]?.let { idx ->
                                scope.launch { listState.animateScrollToItem(idx) }
                            }
                        },
                        onDocJump = onDocJump,
                    )
                }
                is MdBlock.Quote -> item(key = "q$index") {
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                        // 竖条必须 fillMaxHeight：空 Box 无内容时高度为 0，竖条会整个消失
                        Box(modifier = Modifier
                            .padding(end = 12.dp)
                            .fillMaxHeight()
                            .width(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colorScheme.outlineVariant)) {}
                        Text(
                            text = parseInlineMd(block.text),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                is MdBlock.Bullet -> item(key = "b$index") {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                        block.items.forEach { item ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text("·  ", style = MaterialTheme.typography.bodyLarge)
                                InlineLine(
                                    segments = item,
                                    onAnchor = { h -> headingIndexes[h]?.let { idx ->
                                        scope.launch { listState.animateScrollToItem(idx) }
                                    } },
                                    onDocJump = onDocJump,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                }
                is MdBlock.Table -> item(key = "t$index") {
                    MdTableView(
                        table = block,
                        onAnchor = { h -> headingIndexes[h]?.let { idx ->
                            scope.launch { listState.animateScrollToItem(idx) }
                        } },
                        onDocJump = onDocJump,
                    )
                }
                MdBlock.Spacer -> item(key = "s$index") { Box(modifier = Modifier.height(8.dp)) {} }
            }
        }
    }
}

/**
 * 一段内联内容（纯文本 + 锚点 + 文档跳转）渲染成单个 Text：
 * 旧实现把每段放进 Row 内的独立 Text，多个 Text 之间不会换行，
 * 长文本会溢出右缘（“文字乱走”）。合并为一个 AnnotatedString 即可正常折行，
 * 跳转通过 StringAnnotation 在点击处定位。
 */
@Composable
private fun InlineLine(
    segments: List<MdInline>,
    onAnchor: (String) -> Unit,
    onDocJump: (String, String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val linkColor = MaterialTheme.colorScheme.primary
    val styled = remember(segments, linkColor) {
        buildAnnotatedString {
            segments.forEach { seg ->
                when (seg) {
                    is MdInline.Plain -> append(parseInlineMd(seg.text))
                    is MdInline.Anchor -> {
                        pushStringAnnotation(tag = TAG_ANCHOR, annotation = seg.heading)
                        pushStyle(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline))
                        append(parseInlineMd(seg.text))
                        pop()
                        pop()
                    }
                    is MdInline.DocJump -> {
                        pushStringAnnotation(tag = TAG_JUMP, annotation = seg.docId + "#" + (seg.heading ?: ""))
                        pushStyle(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline))
                        append(parseInlineMd(seg.text))
                        pop()
                        pop()
                    }
                }
            }
        }
    }
    ClickableText(
        text = styled,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.fillMaxWidth(),
        onClick = { offset ->
            val anchorHit = styled.getStringAnnotations(TAG_ANCHOR, offset, offset).firstOrNull()
            if (anchorHit != null) {
                onAnchor(anchorHit.item)
            } else {
                styled.getStringAnnotations(TAG_JUMP, offset, offset).firstOrNull()?.let { anno ->
                    val (doc, heading) = anno.item.split("#", limit = 2)
                        .let { p -> p[0] to p.getOrNull(1)?.takeIf { h -> h.isNotEmpty() } }
                    onDocJump(doc, heading)
                }
            }
        },
    )
}

private const val TAG_ANCHOR = "md-anchor"
private const val TAG_JUMP = "md-jump"

@Composable
private fun MdTableView(
    table: MdBlock.Table,
    onAnchor: (String) -> Unit,
    onDocJump: (String, String?) -> Unit,
) {
    val cols = maxOf(table.header.size, table.rows.maxOfOrNull { it.size } ?: 0).coerceAtLeast(1)
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        // IntrinsicSize.Min + fillMaxHeight：同行单元格等高，不再“格高不一”
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            table.header.forEachIndexed { col, cell ->
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(8.dp)) {
                    InlineLine(cell, onAnchor, onDocJump)
                }
            }
            repeat(cols - table.header.size) {
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant)) {}
            }
        }
        HorizontalDivider()
        table.rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                row.forEachIndexed { col, cell ->
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(8.dp)) {
                        InlineLine(cell, onAnchor, onDocJump)
                    }
                }
                repeat(cols - row.size) { Box(modifier = Modifier.weight(1f).fillMaxHeight()) {} }
            }
            HorizontalDivider()
        }
    }
}
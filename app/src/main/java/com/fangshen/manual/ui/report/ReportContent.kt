package com.fangshen.manual.ui.report

import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.material3.Surface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.fangshen.manual.model.DialogItem
import com.fangshen.manual.model.Report
import com.fangshen.manual.model.ReportCharacter
import com.fangshen.manual.model.ReportFragment
import com.fangshen.manual.model.flatFragments
import com.fangshen.manual.model.hasNote
import com.fangshen.manual.model.titleText
import com.fangshen.manual.ui.markdown.parseInlineMd

/** 整篇报告的正文渲染：阅读页与“长截图”共用这一份 */
@Composable
fun ReportBody(
    report: Report,
    modifier: Modifier = Modifier,
    onCharacterClick: (ReportCharacter) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val head = report.head
        // 1. 标题：置顶、粗体、比正文大
        Text(
            text = head.name.ifEmpty { "（未命名）" },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        // 2. 报告日期
        if (head.date.isNotBlank()) {
            Text(
                text = "报告日期：${head.date}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        // 3. 报告者：有备注的名字渲染为蓝色超链接，点击弹窗
        if (head.characters.isNotEmpty()) {
            CharactersRow(head.characters, onCharacterClick)
        }
        // 4. 前言：灰色竖条 + 灰色文本
        if (head.preface.isNotBlank()) {
            QuoteBlock(text = head.preface, label = "前言")
        }
        // 5. 正文片段：场景片段为圆角卡片，对话片段为 QQ 式气泡
        report.flatFragments().forEach { fragment ->
            when (fragment) {
                is ReportFragment.Scene -> SceneCard(fragment)
                is ReportFragment.Dialog -> DialogBlock(fragment, author = head.name.ifBlank { head.characters.firstOrNull()?.name ?: "" })
            }
        }
        // 6. 结语：同前言
        if (head.conclusion.isNotBlank()) {
            QuoteBlock(text = head.conclusion, label = "结语")
        }
        Spacer(Modifier.height(24.dp))
    }
}

/** 长截图专用：整篇报告以纯背景容器整页渲染（不被屏幕高度裁剪） */
@Composable
fun ReportForCapture(report: Report) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        ReportBody(report, onCharacterClick = {})
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharactersRow(
    characters: List<ReportCharacter>,
    onClick: (ReportCharacter) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = "报告者：",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        characters.forEachIndexed { index, c ->
            if (index > 0) {
                Text(
                    text = "、",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            if (c.hasNote) {
                val annotated = buildAnnotatedString {
                    withStyle(SpanStyle(color = NoteBlue, textDecoration = TextDecoration.Underline)) {
                        append(c.name)
                    }
                }
                Text(
                    text = annotated,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { onClick(c) },
                )
            } else {
                Text(
                    text = c.name,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

/** 前言/结语块：左侧灰色竖条（矩形、不圆角）+ 右侧灰色文本 */
@Composable
fun QuoteBlock(text: String, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .fillMaxHeight()
                .width(4.dp)
                .background(QuoteBar),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = QuoteGray,
        )
    }
}

val NoteBlue = Color(0xFF1E88E5)
val QuoteGray = Color(0xFF9E9E9E)
val QuoteBar = Color(0xFFBDBDBD)

/** 场景片段：时间 + 地点标题，描述正文 */
@Composable
private fun SceneCard(fragment: ReportFragment.Scene) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val title = when {
                fragment.time.isNotBlank() && fragment.place.isNotBlank() ->
                    fragment.time + "：" + fragment.place
                fragment.time.isNotBlank() -> fragment.time
                fragment.place.isNotBlank() -> fragment.place
                else -> null
            }
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            if (fragment.description.isNotBlank()) {
                Text(
                    text = parseInlineMd(fragment.description),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

/**
 * 对话片段：QQ 聊天式——首报告人（head.name）靠右，其余人靠左；
 * 事件条目居中、无卡片背景（类似 QQ 系统文本）。
 */
@Composable
private fun DialogBlock(fragment: ReportFragment.Dialog, author: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        fragment.items.forEach { item ->
            when (item) {
                is DialogItem.Event -> Text(
                    text = item.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = QuoteGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                )
                is DialogItem.Say -> {
                    val isAuthor = author.isNotBlank() && item.name == author
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isAuthor) Arrangement.End else Arrangement.Start,
                    ) {
                        Column(
                            horizontalAlignment = if (isAuthor) Alignment.End else Alignment.Start,
                            modifier = Modifier.width(IntrinsicSize.Max).fillMaxWidth(0.8f),
                        ) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = QuoteGray,
                                modifier = Modifier.padding(bottom = 3.dp),
                            )
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isAuthor) BubbleAuthor else BubbleOther,
                            ) {
                                Text(
                                    text = parseInlineMd(item.content),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (isAuthor) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private val BubbleAuthor = Color(0xFF4E8DDB)
private val BubbleOther = Color(0xFFEDEDED)

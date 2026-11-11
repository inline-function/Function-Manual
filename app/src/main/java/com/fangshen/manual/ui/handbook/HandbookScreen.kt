package com.fangshen.manual.ui.handbook

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fangshen.manual.model.EffectCatalog
import com.fangshen.manual.model.EffectDef
import com.fangshen.manual.model.EffectUrgency
import com.fangshen.manual.model.EffectValence
import com.fangshen.manual.ui.theme.FangShenTheme

/**
 * 手册：效应词典。按正面/负面/中立列出全部效应定义与其通俗描述，
 * 效应包（综合征）单独成节。点击条目展开完整描述。
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HandbookScreen(
    onBack: () -> Unit,
) {
    FangShenTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text("手册 · 效应词典", fontWeight = FontWeight.SemiBold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                        }
                    },
                )
            },
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Text(
                        text = "手册记录了应用内所有效应的含义：首页的效应条、预警规则与曲线解读都以此为词表。" +
                            "效应权重是示意性推算，不是诊断结论。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
                item { SectionTitle("效应包（综合征集合）") }
                items(EffectCatalog.PACKS.entries.toList(), key = { it.key }) { (pack, members) ->
                    ExpandablePack(pack = pack, members = members)
                }
                item { SectionTitle("正面效应") }
                val positive = EffectCatalog.defMap.values.filter {
                    it.valence == EffectValence.POSITIVE && !EffectCatalog.PACKS.containsKey(it.name)
                }
                items(positive, key = { it.name }) { def -> EffectCard(def) }
                item { SectionTitle("负面效应") }
                val negative = EffectCatalog.defMap.values.filter {
                    it.valence == EffectValence.NEGATIVE && !EffectCatalog.PACKS.containsKey(it.name)
                }
                items(negative, key = { it.name }) { def -> EffectCard(def) }
                item { SectionTitle("中立效应") }
                val neutral = EffectCatalog.defMap.values.filter {
                    it.valence == EffectValence.NEUTRAL && !EffectCatalog.PACKS.containsKey(it.name)
                }
                items(neutral, key = { it.name }) { def -> EffectCard(def) }
                item { Box(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 12.dp, start = 4.dp),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandablePack(pack: String, members: List<String>) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded },
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = pack,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 6.dp),
            ) {
                members.forEach { member ->
                    TagChip(text = member, color = packColor(pack))
                }
            }
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = EffectCatalog.packDescription(pack),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EffectCard(def: EffectDef) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded },
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = def.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 6.dp),
            ) {
                TagChip(
                    text = if (def.urgency == EffectUrgency.ACUTE) "急性" else "慢性",
                    color = urgencyColor(def.urgency),
                )
                if (def.hidden) TagChip(text = "隐效应", color = Color(0xFF757575))
                if (def.pack != null) TagChip(text = "属于" + def.pack, color = Color(0xFF757575))
                def.neurotransmitter?.let { TagChip(text = "递质·" + it, color = Color(0xFF9E9E9E)) }
            }
            if (!expanded) {
                Text(
                    text = def.description.take(42) + "…",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            AnimatedVisibility(visible = expanded, enter = expandVertically(), exit = shrinkVertically()) {
                Text(
                    text = def.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun TagChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.16f))
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
    }
}

private fun packColor(pack: String): Color = when (pack) {
    "血清素综合征" -> Color(0xFFD81B60)
    "抗胆碱能综合征" -> Color(0xFF795548)
    "阿片类中毒" -> Color(0xFFB71C1C)
    else -> Color(0xFF607D8B)
}

private fun urgencyColor(urgency: EffectUrgency): Color = when (urgency) {
    EffectUrgency.ACUTE -> Color(0xFFC62828)
    EffectUrgency.CHRONIC -> Color(0xFF455A64)
}

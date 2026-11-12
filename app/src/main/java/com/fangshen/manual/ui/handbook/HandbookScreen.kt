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
import androidx.compose.foundation.layout.Row
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
import com.fangshen.manual.ui.LocalApplication
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.model.CustomEffect
import com.fangshen.manual.model.CustomPack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.remember

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
                    title = { Text("手册", fontWeight = FontWeight.SemiBold) },
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
                item { CustomContentSection() }
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
                verticalArrangement = Arrangement.spacedBy(5.dp),
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

/** 自定义效应与效应包的增删改：与内置词典合并显示 */
@Composable
private fun CustomContentSection() {
    val app = LocalApplication.current as FangShenApplication
    val jsonData by app.contentRepository.state.collectAsState()
    var editingEffect by remember { mutableStateOf<CustomEffect?>(null) }
    var editingPack by remember { mutableStateOf<CustomPack?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle("我的自定义")
        if (jsonData.customEffects.isEmpty() && jsonData.customPacks.isEmpty()) {
            Text(
                "还没有自定义内容。效应和效应包都可以自己定义，会在主页效应流与词典里与内置条目一起出现。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        jsonData.customEffects.forEach { effect ->
            Card(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .clickable { editingEffect = effect },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(effect.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        if (effect.description.isNotBlank()) {
                            Text(
                                effect.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    IconButton(onClick = { app.contentRepository.deleteCustomEffect(effect.name) }) {
                        Icon(Icons.Rounded.Close, contentDescription = "删除自定义效应")
                    }
                }
            }
        }
        jsonData.customPacks.forEach { pack ->
            Card(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .clickable { editingPack = pack },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(pack.name + "（效应包）", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            pack.members.joinToString("、"),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    IconButton(onClick = { app.contentRepository.deleteCustomPack(pack.name) }) {
                        Icon(Icons.Rounded.Close, contentDescription = "删除自定义效应包")
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { editingEffect = CustomEffect("", EffectValence.POSITIVE, false, "") }) {
                Text("+ 自定义效应")
            }
            TextButton(onClick = { editingPack = CustomPack("", emptyList(), "") }) {
                Text("+ 自定义效应包")
            }
        }
    }

    editingEffect?.let { effect ->
        CustomEffectDialog(
            initial = effect,
            onDismiss = { editingEffect = null },
            onConfirm = { saved ->
                if (saved.name.isNotBlank()) app.contentRepository.saveCustomEffect(saved)
                editingEffect = null
            },
        )
    }
    editingPack?.let { pack ->
        CustomPackDialog(
            initial = pack,
            onDismiss = { editingPack = null },
            onConfirm = { saved ->
                if (saved.name.isNotBlank() && saved.members.isNotEmpty()) app.contentRepository.saveCustomPack(saved)
                editingPack = null
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomEffectDialog(
    initial: CustomEffect,
    onDismiss: () -> Unit,
    onConfirm: (CustomEffect) -> Unit,
) {
    var name by remember { mutableStateOf(initial.name) }
    var description by remember { mutableStateOf(initial.description) }
    var chronic by remember { mutableStateOf(initial.chronic) }
    var valence by remember { mutableStateOf(initial.valence) }
    var inverseOf by remember { mutableStateOf(initial.inverseOf ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("自定义效应") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("效应名称") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("通俗描述") },
                    minLines = 2,
                )
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Switch(checked = chronic, onCheckedChange = { chronic = it })
                    Text("慢性效应（长期形成）")
                }
                Text("效价", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EffectValence.entries.forEach { v ->
                        FilterChip(
                            selected = valence == v,
                            onClick = { valence = v },
                            label = {
                                Text(when (v) {
                                    EffectValence.POSITIVE -> "正面"
                                    EffectValence.NEGATIVE -> "负面"
                                    EffectValence.NEUTRAL -> "中立"
                                })
                            },
                        )
                    }
                }
                OutlinedTextField(
                    value = inverseOf,
                    onValueChange = { inverseOf = it },
                    label = { Text("抵消（填效应名，可空）") },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(CustomEffect(name.trim(), valence, chronic, description.trim(), inverseOf.ifBlank { null }))
            }) { Text("保存") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomPackDialog(
    initial: CustomPack,
    onDismiss: () -> Unit,
    onConfirm: (CustomPack) -> Unit,
) {
    var name by remember { mutableStateOf(initial.name) }
    var members by remember { mutableStateOf(initial.members.joinToString("\n")) }
    var description by remember { mutableStateOf(initial.description) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("自定义效应包") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("效应包名称") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = members,
                    onValueChange = { members = it },
                    label = { Text("成员效应（每行一个）") },
                    minLines = 3,
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("通俗描述") },
                    minLines = 2,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(CustomPack(name.trim(), members.split("\n").map { it.trim() }.filter { it.isNotBlank() }, description.trim()))
            }) { Text("保存") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
    )
}
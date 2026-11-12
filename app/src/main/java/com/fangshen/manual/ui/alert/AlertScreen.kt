package com.fangshen.manual.ui.alert

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.model.CustomInteraction
import com.fangshen.manual.ui.LocalApplication
import com.fangshen.manual.model.AlertRule
import com.fangshen.manual.model.DrugCatalog
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.EffectCatalog
import com.fangshen.manual.ui.profile.DISORDER_ITEMS
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.material.icons.rounded.Close

/** 匹配值的展示标签：药物名优先，其余视为标签 */
fun matcherLabel(value: String, drugNames: List<String>): String =
    if (value in drugNames) "药物「$value」" else "标签「$value」"

fun inferIsTag(value: String, drugNames: List<String>): Boolean = value.isNotEmpty() && value !in drugNames

class AlertViewModel(application: Application) : AndroidViewModel(application) {

    private val app = getApplication<FangShenApplication>()

    val rules: StateFlow<List<AlertRule>> = app.alertRepository.rules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** 全部药物名 */
    val drugNames: List<String> = DrugCatalog.all.map { it.name }

    /** 全部标签：主观标签 + 机制标签 */
    val tagOptions: List<String> = buildList {
        val tags = sortedSetOf<String>()
        DrugCatalog.all.forEach { drug: DrugInfo ->
            tags += drug.subjectiveTags
            tags += drug.effectTags
        }
        addAll(tags)
    }

    /** 画像匹配候选：类别 + 具体条目 */
    val profileOptions: List<String> = buildList {
        DISORDER_ITEMS.forEach { (category, items) ->
            add(category)
            addAll(items)
        }
    }

    val effectOptions: List<String> = EffectCatalog.allEffectNames

    fun save(rule: AlertRule) {
        viewModelScope.launch {
            if (rule.id == 0L) {
                app.alertRepository.add(
                    name = rule.name, drug = rule.drug,
                    withDrug = rule.withDrug, withProfile = rule.withProfile,
                    effect = rule.effect, weight = rule.weight,
                )
            } else {
                app.alertRepository.update(rule)
            }
        }
    }

    fun toggle(rule: AlertRule) {
        viewModelScope.launch { app.alertRepository.toggle(rule) }
    }

    fun delete(rule: AlertRule) {
        viewModelScope.launch { app.alertRepository.delete(rule) }
    }

    companion object {
        fun provideFactory(app: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = AlertViewModel(app) as T
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertScreen(
    onBack: () -> Unit,
    viewModel: AlertViewModel,
) {
    val rules by viewModel.rules.collectAsStateWithLifecycle()
    var editing by remember { mutableStateOf<AlertRule?>(null) }
    var pendingDelete by remember { mutableStateOf<AlertRule?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("预警", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { editing = AlertRule(id = 0, name = "", drug = "", effect = "", weight = 1) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) { Icon(Icons.Rounded.Add, contentDescription = "添加预警规则") }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp),
        ) {
            item {
                Card(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            "对联用药物或特定画像进行预警",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            "· 联用：两味药同时生效时给效应加权，如「吗啡 与 镇静剂 → 呼吸抑制」；\n" +
                                "· 画像：某药生效且画像命中所选障碍时加权，如「选择性血清素再摄取抑制剂 与 双相 → 躁狂」。\n" +
                                "每个条件既可填具体药物，也可填标签（镇痛剂、镇静剂、选择性血清素再摄取抑制剂等）；" +
                                "画像条件可选类别或具体障碍条目。生效后权重会并入主页效应流。",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            item { CustomInteractionSection() }
            items(rules, key = { it.id }) { rule ->
                AlertRuleCard(
                    rule = rule,
                    drugNames = viewModel.drugNames,
                    onToggle = { viewModel.toggle(rule) },
                    onEdit = { editing = rule },
                    onDelete = { pendingDelete = rule },
                )
            }
            if (rules.isEmpty()) {
                item {
                    Text(
                        "暂无预警规则。点击右下角 + 添加一条。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 24.dp),
                    )
                }
            }
        }
    }

    editing?.let { rule ->
        AlertRuleDialog(
            rule = rule,
            drugNames = viewModel.drugNames,
            tagOptions = viewModel.tagOptions,
            profileOptions = viewModel.profileOptions,
            effectOptions = viewModel.effectOptions,
            onDismiss = { editing = null },
            onConfirm = { saved -> viewModel.save(saved); editing = null },
        )
    }
    pendingDelete?.let { rule ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("删除规则") },
            text = { Text("确认删除「${rule.name}」吗？") },
            confirmButton = {
                TextButton(onClick = { viewModel.delete(rule); pendingDelete = null }) { Text("删除") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) { Text("取消") }
            },
        )
    }
}

@Composable
private fun AlertRuleCard(
    rule: AlertRule,
    drugNames: List<String>,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = rule.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                Switch(checked = rule.enabled, onCheckedChange = { onToggle() })
                IconButton(onClick = onDelete) {
                    Icon(Icons.Rounded.Delete, contentDescription = "删除")
                }
            }
            Text(
                text = ruleConditionText(rule, drugNames),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "效应 ${rule.effect} ${if (rule.weight >= 0) "+" else ""}${rule.weight}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun ruleConditionText(rule: AlertRule, drugNames: List<String>): String {
    val a = matcherLabel(rule.drug, drugNames)
    return when {
        rule.isProfileRule -> "$a 与 画像「${rule.withProfile}」生效时"
        else -> "$a 与 ${matcherLabel(rule.withDrug.orEmpty(), drugNames)} 联用时"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlertRuleDialog(
    rule: AlertRule,
    drugNames: List<String>,
    tagOptions: List<String>,
    profileOptions: List<String>,
    effectOptions: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (AlertRule) -> Unit,
) {
    var name by remember { mutableStateOf(rule.name) }
    var isProfile by remember { mutableStateOf(rule.isProfileRule) }
    var aIsTag by remember { mutableStateOf(inferIsTag(rule.drug, drugNames)) }
    var drugA by remember { mutableStateOf(rule.drug) }
    var bIsTag by remember { mutableStateOf(inferIsTag(rule.withDrug.orEmpty(), drugNames)) }
    var drugB by remember { mutableStateOf(rule.withDrug.orEmpty()) }
    var profile by remember { mutableStateOf(rule.withProfile ?: "情感障碍") }
    var effect by remember { mutableStateOf(rule.effect) }
    var weight by remember { mutableIntStateOf(rule.weight) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (rule.id == 0L) "添加预警规则" else "编辑预警规则") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("规则名称") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                KindSelector(
                    isProfile = isProfile,
                    onProfileChange = { isProfile = it },
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("条件一", style = MaterialTheme.typography.labelLarge)
                    SideKindToggle(isTag = aIsTag, onToggle = { aIsTag = it })
                }
                if (aIsTag) {
                    DropdownField(
                        label = "标签（如：镇痛剂 / 选择性血清素再摄取抑制剂）",
                        options = tagOptions,
                        value = drugA,
                        onValueChange = { drugA = it },
                    )
                } else {
                    DropdownField(
                        label = "药物",
                        options = drugNames,
                        value = drugA,
                        onValueChange = { drugA = it },
                    )
                }
                if (isProfile) {
                    DropdownField(
                        label = "画像条件（类别或障碍条目）",
                        options = profileOptions,
                        value = profile,
                        onValueChange = { profile = it },
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("条件二", style = MaterialTheme.typography.labelLarge)
                        SideKindToggle(isTag = bIsTag, onToggle = { bIsTag = it })
                    }
                    if (bIsTag) {
                        DropdownField(
                            label = "标签",
                            options = tagOptions,
                            value = drugB,
                            onValueChange = { drugB = it },
                        )
                    } else {
                        DropdownField(
                            label = "联用药物",
                            options = drugNames,
                            value = drugB,
                            onValueChange = { drugB = it },
                        )
                    }
                }
                EffectField(
                    value = effect,
                    options = effectOptions,
                    onValueChange = { effect = it },
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("权重", style = MaterialTheme.typography.bodyMedium)
                    IconButton(onClick = { weight = (weight - 1).coerceIn(-9, 9) }) {
                        Icon(Icons.Rounded.Remove, contentDescription = "减少")
                    }
                    Text(
                        text = if (weight >= 0) "+$weight" else weight.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                    IconButton(onClick = { weight = (weight + 1).coerceIn(-9, 9) }) {
                        Icon(Icons.Rounded.Add, contentDescription = "增加")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isBlank() || drugA.isBlank() || effect.isBlank()) return@TextButton
                    onConfirm(
                        rule.copy(
                            name = name.trim(),
                            enabled = rule.enabled,
                            drug = drugA,
                            withDrug = if (isProfile) null else drugB.ifBlank { null },
                            withProfile = if (isProfile) profile else null,
                            effect = effect.trim(),
                            weight = weight,
                        ),
                    )
                },
                enabled = name.isNotBlank() && drugA.isNotBlank() && effect.isNotBlank() &&
                    (isProfile || drugB.isNotBlank()),
            ) { Text("保存") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}

/** 规则类型：联用 / 画像 */
@Composable
private fun KindSelector(isProfile: Boolean, onProfileChange: (Boolean) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        androidx.compose.material3.FilterChip(
            selected = !isProfile,
            onClick = { onProfileChange(false) },
            label = { Text("联用") },
        )
        androidx.compose.material3.FilterChip(
            selected = isProfile,
            onClick = { onProfileChange(true) },
            label = { Text("画像") },
        )
    }
}

/** 单个条件侧的类型：药物 / 标签 */
@Composable
private fun SideKindToggle(isTag: Boolean, onToggle: (Boolean) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        androidx.compose.material3.FilterChip(
            selected = !isTag,
            onClick = { onToggle(false) },
            label = { Text("药物") },
        )
        androidx.compose.material3.FilterChip(
            selected = isTag,
            onClick = { onToggle(true) },
            label = { Text("标签") },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    options: List<String>,
    value: String,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onValueChange(option); expanded = false },
                )
            }
        }
    }
}

/****** 效应名：可输入或从已知效应中选择 ******/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EffectField(
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it); expanded = true },
            label = { Text("效应（如：躁狂/呼吸抑制）") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onValueChange(option); expanded = false },
                )
            }
        }
    }
}

/** 自定义联用配对：药物A × 药物B → 标题+提示，可在预警页增删改 */
@Composable
private fun CustomInteractionSection() {
    val app = LocalApplication.current as FangShenApplication
    val jsonData by app.contentRepository.state.collectAsState()
    val pairs = jsonData.customInteractions
    var editing by remember { mutableStateOf<Pair<String, CustomInteraction>?>(null) }
    

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "自定义联用配对",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            "两条药物（或标签）同时生效时给出预警。条件可填药名或标签名。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        pairs.forEach { pair ->
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            pair.title.ifBlank { pair.matcherA + " × " + pair.matcherB },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            pair.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = pair.enabled,
                        onCheckedChange = { v ->
                            val current = app.contentRepository.state.value.customInteractions
                            app.contentRepository.saveCustomInteraction(pair.copy(enabled = v))
                        },
                    )
                    IconButton(onClick = { app.contentRepository.deleteCustomInteraction(pair) }) {
                        Icon(Icons.Rounded.Close, contentDescription = "删除配对")
                    }
                }
            }
        }
        TextButton(onClick = { editing = "new" to CustomInteraction("", "", "", "", true) }) {
            Text("+ 添加联用配对")
        }
    }

    editing?.let { (_, pair) ->
        CustomInteractionDialog(
            initial = pair,
            onDismiss = { editing = null },
            onConfirm = { saved ->
                app.contentRepository.saveCustomInteraction(saved)
                editing = null
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomInteractionDialog(
    initial: CustomInteraction,
    onDismiss: () -> Unit,
    onConfirm: (CustomInteraction) -> Unit,
) {
    var a by remember { mutableStateOf(initial.matcherA) }
    var b by remember { mutableStateOf(initial.matcherB) }
    var title by remember { mutableStateOf(initial.title) }
    var message by remember { mutableStateOf(initial.message) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("联用配对") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = a,
                    onValueChange = { a = it },
                    label = { Text("药物/标签 A") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = b,
                    onValueChange = { b = it },
                    label = { Text("药物/标签 B") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("标题（如：呼吸抑制）") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("提示文字") },
                    minLines = 2,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (a.isNotBlank() && b.isNotBlank()) {
                    onConfirm(CustomInteraction(a.trim(), b.trim(), title.trim(), message.trim(), true))
                }
            }) { Text("保存") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
    )
}
package com.fangshen.manual.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.model.AppSettings
import com.fangshen.manual.ui.LocalApplication
import com.fangshen.manual.ui.navigation.Routes

/**
 * 设置：显示开关（游标线/精麻标识/效应分类）、数据管理入口。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
) {
    val app = LocalApplication.current as FangShenApplication
    val jsonData by app.contentRepository.state.collectAsState()
    val settings = jsonData.settings

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置", fontWeight = FontWeight.SemiBold) },
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item { SectionTitle("显示") }
            item {
                SwitchRow(
                    title = "成瘾游标线常显",
                    subtitle = "关闭后，仅在高亮某条药物曲线时显示其游标线",
                    checked = settings.showAddictionCursor,
                ) { app.contentRepository.updateSettings(settings.copy(showAddictionCursor = it)) }
            }
            item {
                SwitchRow(
                    title = "显示精麻标识",
                    subtitle = "服药记录卡片中，精神药品/麻醉药品药名后标注图标",
                    checked = settings.showPsychotropicBadge,
                ) { app.contentRepository.updateSettings(settings.copy(showPsychotropicBadge = it)) }
            }

            item { SectionTitle("效应显示") }
            item {
                EffectToggle("效应包（综合征）", settings.effectDisplay.showPacks) { v ->
                    app.contentRepository.updateSettings(settings.copy(effectDisplay = settings.effectDisplay.copy(showPacks = v)))
                }
            }
            item {
                EffectToggle("急性 · 正面", settings.effectDisplay.acutePositive) { v ->
                    app.contentRepository.updateSettings(settings.copy(effectDisplay = settings.effectDisplay.copy(acutePositive = v)))
                }
            }
            item {
                EffectToggle("急性 · 负面", settings.effectDisplay.acuteNegative) { v ->
                    app.contentRepository.updateSettings(settings.copy(effectDisplay = settings.effectDisplay.copy(acuteNegative = v)))
                }
            }
            item {
                EffectToggle("急性 · 中立", settings.effectDisplay.acuteNeutral) { v ->
                    app.contentRepository.updateSettings(settings.copy(effectDisplay = settings.effectDisplay.copy(acuteNeutral = v)))
                }
            }
            item {
                EffectToggle("慢性 · 正面", settings.effectDisplay.chronicPositive) { v ->
                    app.contentRepository.updateSettings(settings.copy(effectDisplay = settings.effectDisplay.copy(chronicPositive = v)))
                }
            }
            item {
                EffectToggle("慢性 · 负面", settings.effectDisplay.chronicNegative) { v ->
                    app.contentRepository.updateSettings(settings.copy(effectDisplay = settings.effectDisplay.copy(chronicNegative = v)))
                }
            }
            item {
                EffectToggle("慢性 · 中立", settings.effectDisplay.chronicNeutral) { v ->
                    app.contentRepository.updateSettings(settings.copy(effectDisplay = settings.effectDisplay.copy(chronicNeutral = v)))
                }
            }

            item { SectionTitle("数据") }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(Routes.DATA_MANAGEMENT) },
                ) {
                    Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text("数据管理", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            "导入、导出与清除数据",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Icon(Icons.Rounded.ChevronRight, contentDescription = null)
                    }
                }
            }
            item { Spacer(20) }
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

@Composable
private fun SwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.padding(end = 12.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Switch(checked = checked, onCheckedChange = onChange)
        }
    }
}

@Composable
private fun EffectToggle(title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onChange(!checked) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Switch(checked = checked, onCheckedChange = onChange)
        }
    }
}

@Composable
private fun Spacer(height: Int) {
    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(top = height.dp)) {}
}

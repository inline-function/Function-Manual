package com.fangshen.manual.ui.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fangshen.manual.ui.navigation.Routes

@Composable
fun AppDrawer(
    onNavigate: (String) -> Unit,
    onClose: () -> Unit,
) {
    // windowInsets 清零：M3 默认会把面板缩进系统栏，导致底部漏出一条阴影缝；
    // 顶部间距自行处理（状态栏下方 10dp）
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.82f),
        windowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column {
                    Text(
                        text = "芳沈手册",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "精神活性物质用药手册",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Rounded.Close, contentDescription = "关闭")
                }
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(Modifier.height(8.dp))
        }
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            DrawerItem(
                label = "统计",
                description = "服药历史、报告与种类统计",
                icon = Icons.Rounded.MenuBook,
                route = Routes.STATS,
                onNavigate = onNavigate,
            )
            DrawerItem(
                label = "个人画像",
                description = "统计精神障碍",
                icon = Icons.Rounded.Person,
                route = Routes.PROFILE,
                onNavigate = onNavigate,
            )
            DrawerItem(
                label = "预警",
                description = "联用药物或特定画像预警",
                icon = Icons.Rounded.NotificationsActive,
                route = Routes.ALERTS,
                onNavigate = onNavigate,
            )
            DrawerItem(
                label = "药物管理",
                description = "查看并管理药物目录",
                icon = Icons.Rounded.Medication,
                route = Routes.DRUG_LIST,
                onNavigate = onNavigate,
            )
            DrawerItem(
                label = "手册",
                description = "效应词典与机制说明",
                icon = Icons.Rounded.AutoStories,
                route = Routes.HANDBOOK,
                onNavigate = onNavigate,
            )
            DrawerItem(
                label = "文档",
                description = "药理与精神病学通俗读物",
                icon = Icons.Rounded.Description,
                route = Routes.DOCUMENTS,
                onNavigate = onNavigate,
            )
            DrawerItem(
                label = "处方",
                description = "创建您的药物组合",
                icon = Icons.Rounded.Description,
                route = Routes.PRESCRIPTIONS,
                onNavigate = onNavigate,
            )
            DrawerItem(
                label = "日程",
                description = "查看服药与日程提醒",
                icon = Icons.Rounded.Schedule,
                route = Routes.SCHEDULE_LIST,
                onNavigate = onNavigate,
            )
            DrawerItem(
                label = "设置",
                description = "清除记录与自定义药物",
                icon = Icons.Rounded.CleaningServices,
                route = Routes.SETTINGS,
                onNavigate = onNavigate,
            )
            DrawerItem(
                label = "关于",
                description = "应用信息",
                icon = Icons.Rounded.Info,
                route = Routes.ABOUT,
                onNavigate = onNavigate,
            )
            DrawerItem(
                label = "免责声明",
                description = "请先阅读",
                icon = Icons.Rounded.Warning,
                route = Routes.DISCLAIMER,
                onNavigate = onNavigate,
            )
        }
    }
}

@Composable
private fun DrawerItem(
    label: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    route: String,
    onNavigate: (String) -> Unit,
) {
    NavigationDrawerItem(
        label = {
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        icon = { Icon(icon, contentDescription = null) },
        selected = false,
        onClick = { onNavigate(route) },
        colors = NavigationDrawerItemDefaults.colors(),
        modifier = Modifier.padding(vertical = 2.dp),
    )
}

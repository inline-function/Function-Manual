package com.fangshen.manual.ui.stats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.model.parseReport
import com.fangshen.manual.ui.LocalApplication
import com.fangshen.manual.ui.navigation.Routes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * 统计：服药历史、累计药物种类、按日期回看当天的服药与报告。
 * 报告只在此页出现（主页时间线只有服药与笔记等条目）。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
) {
    val app = LocalApplication.current as FangShenApplication
    val entries by androidx.compose.runtime.produceState<List<EntryEntity>>(emptyList(), app.entryRepository) {
        app.entryRepository.entries.collect { value = it }
    }
    val reportRepo = remember { com.fangshen.manual.data.ReportRepository(app) }
    var selectedDay by remember { mutableLongStateOf(floorToDay(System.currentTimeMillis())) }
    var showPicker by remember { mutableStateOf(false) }

    val dayReports = remember(selectedDay, entries.size) {
        val target = dayDigits(selectedDay)
        reportRepo.listFiles().mapNotNull { f ->
            val text = reportRepo.read(reportRepo.displayName(f)) ?: return@mapNotNull null
            val report = parseReport(text)
            if (report.head.date.isBlank()) return@mapNotNull null
            if (dayDigits(parseFlexibleDate(report.head.date)) == target) f else null
        }
    }
    val dayEntries = remember(entries, selectedDay) {
        entries.filter { it.type == "DOSE" && floorToDay(it.eventTime) == selectedDay }
    }
    val drugKinds = remember(entries) {
        entries.filter { it.type == "DOSE" }.mapNotNull { it.drugId }.distinct().size
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("统计", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigate(Routes.reportEdit("新建报告")) }) {
                Icon(Icons.Rounded.Add, contentDescription = "写报告")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    StatCard(modifier = Modifier.weight(1f), title = drugKinds.toString(), subtitle = "累计服用药物种类")
                    StatCard(modifier = Modifier.weight(1f), title = entries.count { it.type == "DOSE" }.toString(), subtitle = "服药记录总数")
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth().clickable { showPicker = true }) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(Icons.Rounded.CalendarMonth, contentDescription = null)
                        Column {
                            Text("按日期回看", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text(
                                dayLabel(selectedDay),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    "当日服药（" + dayEntries.size + " 条）",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp),
                )
            }
            if (dayEntries.isEmpty()) {
                item { Text("当日没有服药记录", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            items(dayEntries, key = { it.id }) { entry -> DayDoseRow(entry) }
            item {
                Text(
                    "当日报告（" + dayReports.size + " 篇）",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 12.dp, start = 4.dp),
                )
            }
            if (dayReports.isEmpty()) {
                item { Text("当日没有报告", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            items(dayReports, key = { it.name }) { file ->
                val name = reportRepo.displayName(file)
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onNavigate(Routes.reportReader(name)) },
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            reportRepo.read(name)?.let { parseReport(it).head.name.ifBlank { "未命名报告者" } } ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            item { Box(modifier = Modifier.padding(24.dp)) {} }
        }
    }

    if (showPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = selectedDay)
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { selectedDay = floorToDay(it) }
                    showPicker = false
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("取消") }
            },
        ) {
            DatePicker(state = state)
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier, title: String, subtitle: String) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DayDoseRow(entry: EntryEntity) {
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(entry.eventTime))
    Card {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(entry.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    (entry.doseAmount ?: "") + " " + (entry.doseUnit ?: "") + " · " + (entry.route ?: ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(time, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun floorToDay(ms: Long): Long {
    val c = Calendar.getInstance()
    c.timeInMillis = ms
    c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
    return c.timeInMillis
}

private fun dayLabel(ms: Long): String =
    SimpleDateFormat("yyyy 年 M 月 d 日 EEEE", Locale.CHINESE).format(Date(ms))

/** 日期串到毫秒：支持 yyyy-MM-dd / yyyy/M/d 等；失败返回 0 */
private fun parseFlexibleDate(s: String): Long = runCatching {
    val digits = s.replace(Regex("\\D"), "")
    if (digits.length >= 8) {
        val c = Calendar.getInstance()
        c.clear()
        c.set(digits.substring(0, 4).toInt(), digits.substring(4, 6).toInt() - 1, digits.substring(6, 8).toInt())
        c.timeInMillis
    } else 0L
}.getOrDefault(0L)

private fun dayDigits(ms: Long): String {
    if (ms <= 0L) return "none"
    val c = Calendar.getInstance()
    c.timeInMillis = ms
    return "%04d%02d%02d".format(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
}

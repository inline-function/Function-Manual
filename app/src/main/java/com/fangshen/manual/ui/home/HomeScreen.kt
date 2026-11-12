package com.fangshen.manual.ui.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import com.fangshen.manual.core.enzymeArrowsSuffix
import com.fangshen.manual.core.formatDayLabel
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.engine.EffectEngine
import com.fangshen.manual.model.EffectSeverity
import com.fangshen.manual.model.EffectUrgency
import com.fangshen.manual.model.EffectValence
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.model.effectCategory
import com.fangshen.manual.model.enabledIn
import com.fangshen.manual.ui.LocalApplication
import com.fangshen.manual.ui.components.ConfirmDialog
import com.fangshen.manual.ui.drawer.AppDrawer
import com.fangshen.manual.ui.effect.EffectDetailSheet
import com.fangshen.manual.ui.common.effectGroupColors
import com.fangshen.manual.ui.navigation.Routes
import com.fangshen.manual.ui.theme.TopBarTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    viewModel: HomeViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val app = LocalApplication.current as FangShenApplication
    val jsonData by app.contentRepository.state.collectAsState()
    val settings = jsonData.settings
    val filteredEffects = remember(state.activeEffects, settings) {
        state.activeEffects.filter { group ->
            effectCategory(group.name, group.displayChronic)?.enabledIn(settings.effectDisplay, group.weight) != false
        }
    }
    // 每分钟刷新一次"当前时刻"，驱动图像重绘（函数图像随实际时间推进）
    val nowTick by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            delay(60_000L)
            value = System.currentTimeMillis()
        }
    }
    var drawerOpen by remember { mutableStateOf(false) }
    var highlightDrugId by remember { mutableStateOf<String?>(null) }
    var showDosingSheet by remember { mutableStateOf(false) }
    var pendingDelete by remember { mutableStateOf<EntryEntity?>(null) }
    var effectSheetName by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION,
                )
            } catch (e: SecurityException) {
                // 忽略：无法持久化权限时仍尝试引用
            }
            onNavigate(Routes.entryNew("PHOTO", uri.toString()))
        }
    }
    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION,
                )
            } catch (e: SecurityException) {
                // 忽略
            }
            onNavigate(Routes.entryNew("FILE", uri.toString()))
        }
    }

    val onQuickEntry: (String) -> Unit = { type ->
        drawerOpen = false
        when (type) {
            "PHOTO" -> photoPicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
            "FILE" -> filePicker.launch(arrayOf("*/*"))
            else -> onNavigate(Routes.entryNew(type))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text("芳沈手册", fontWeight = FontWeight.SemiBold) },
                    navigationIcon = {
                        IconButton(onClick = { drawerOpen = true }) {
                            Icon(Icons.Rounded.Menu, contentDescription = "菜单")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TopBarTeal,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    ),
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDosingSheet = true },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "服药")
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding(),
                    ),
            ) {
                // 血药浓度图像：固定在顶部，不随列表滚动，高度为屏幕三分之一
                ConcentrationChart(
                    drugs = state.drugs,
                    doses = state.chartDoses,
                    unit = state.chartUnit,
                    onUnitChange = viewModel::setChartUnit,
                    highlightedDrugId = highlightDrugId,
                    onLegendClick = { id ->
                        highlightDrugId = if (highlightDrugId == id) null else id
                    },
                    showAddictionCursor = settings.showAddictionCursor,
        chartAnimation = settings.chartAnimation,
                    now = if (nowTick > state.now) nowTick else state.now,
                )

                val activeDoseDrugs = remember(state.entries, state.drugs, state.now) {
                    val ids = mutableSetOf<String>()
                    state.entries
                        .filter { it.type == "DOSE" && it.drugId != null && it.eventTime <= state.now }
                        .forEach { entry ->
                            val drug = state.drugs.firstOrNull { it.id == entry.drugId }
                            if (drug != null &&
                                state.now - entry.eventTime <= drug.halfLifeHours * 5 * 3_600_000L
                            ) {
                                ids += drug.id
                            }
                        }
                    state.drugs.filter { it.id in ids }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // 效应流式布局：紧跟在图像下方
                    item {
                        EffectPanel(
                            activeEffects = filteredEffects,
                            onEffectClick = { name -> effectSheetName = name },
                        )
                    }

                    if (state.entries.isEmpty()) {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 24.dp),
                            ) {
                                Text(
                                    text = "暂无记录。点击右下角按钮记录服药，或在左侧菜单新建笔记、照片、语音与日程。",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    } else {

                        // 日程（纯提醒条目）不在服药记录中显示
                        val grouped = state.entries
                            .filterNot { it.type == "SCHEDULE" }
                            .groupBy { formatDayLabel(it.eventTime, state.now) }
                        grouped.forEach { (dayLabel, entriesInDay) ->
                            item(key = "label_" + dayLabel + "_" + entriesInDay.first().id) {
                                Text(
                                    text = dayLabel,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 3.dp, bottom = 4.dp),
                                )
                            }
                            items(
                                items = entriesInDay,
                                key = { it.id },
                            ) { entry ->
                                val drug = state.drugs.firstOrNull { it.id == entry.drugId }
                                EntryCard(
                                    entry = entry,
                                    drug = drug,
                                    now = state.now,
                                    onClick = {
                                        highlightDrugId = if (highlightDrugId == entry.drugId) {
                                            null
                                        } else {
                                            entry.drugId
                                        }
                                    },
                                    onLongClick = { pendingDelete = entry },
                                    nameSuffix = if (drug != null) enzymeArrowsSuffix(
                                        drug,
                                        activeDoseDrugs.filter { it.id != drug.id },
                                    ) else "",
                                )
                            }
                        }
                    }
                }
            }
        }

        // 左侧抽屉（菜单按钮在顶栏标题左侧）
        LeftModalDrawer(
            visible = drawerOpen,
            onDismiss = { drawerOpen = false },
        ) {
            AppDrawer(
                onNavigate = { route ->
                    drawerOpen = false
                    onNavigate(route)
                },
                onClose = { drawerOpen = false },
            )
        }
    }

    // 始终组合抽屉，由 visible 驱动滑入/滑出动画
    DosingSheet(
        viewModel = viewModel,
        visible = showDosingSheet,
        onDismiss = { showDosingSheet = false },
    )

    pendingDelete?.let { entry ->
        ConfirmDialog(
            title = "删除记录",
            message = "确认删除「" + entry.title + "」这条记录吗？",
            confirmText = "删除",
            onConfirm = {
                pendingDelete = null
                viewModel.deleteEntry(entry)
            },
            onDismiss = { pendingDelete = null },
        )
    }

    EffectDetailSheet(
        name = effectSheetName ?: "",
        effectResult = state.effectResult,
        visible = effectSheetName != null,
        onDismiss = { effectSheetName = null },
        onShowMember = { member ->
            if (member != effectSheetName) effectSheetName = member
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EffectPanel(
    activeEffects: List<EffectEngine.EffectGroup>,
    onEffectClick: (String) -> Unit,
) {
    val groups = remember(activeEffects) {
        // 排序：效应包 > 单独效应；急性 > 慢性；重度 > 中度 > 轻度
        activeEffects
            .filter { !it.absorbedByPack }
            .sortedWith(
                compareByDescending<EffectEngine.EffectGroup> { it.isPack }
                    .thenBy { if (it.def?.urgency == EffectUrgency.ACUTE) 0 else 1 }
                    .thenByDescending { severityRank(it.severity) },
            )
    }
    if (groups.isEmpty()) {
        Text(
            text = "暂无活跃效应",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp, top = 5.dp, bottom = 5.dp),
        )
        return
    }
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 11.dp, end = 11.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        groups.forEach { group ->
            EffectChip(group = group, onClick = { onEffectClick(group.name) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EffectChip(
    group: EffectEngine.EffectGroup,
    onClick: () -> Unit,
) {
    val prefix = when {
        group.isPack -> "◆ "
        group.def?.valence == EffectValence.POSITIVE -> "↑ "
        group.def?.valence == EffectValence.NEGATIVE -> "↓ "
        else -> "+ "
    }
    // 急性效应闪烁；效应包同样需要闪烁提示（慢性展示的效应不闪）
    val isAcute = group.isPack ||
        (!group.displayChronic && group.def?.urgency == EffectUrgency.ACUTE)
    val blinkAlpha by if (isAcute) {
        rememberInfiniteTransition(label = "effect_blink").animateFloat(
            initialValue = 0.35f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 900),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "effect_blink_alpha",
        )
    } else {
        remember { mutableFloatStateOf(1f) }
    }
    val (bg, fg) = effectGroupColors(group)
    Surface(
        // 微微圆角的矩形（不是全圆角胶囊）
        shape = RoundedCornerShape(8.dp),
        color = bg,
        contentColor = fg,
        modifier = Modifier
            .alpha(blinkAlpha)
            .clickable { onClick() },
    ) {
        Text(
            text = prefix + group.name,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(7.dp),
        )
    }
}

private fun severityRank(severity: EffectSeverity): Int = when (severity) {
    EffectSeverity.SEVERE -> 3
    EffectSeverity.MODERATE -> 2
    EffectSeverity.MILD -> 1
}

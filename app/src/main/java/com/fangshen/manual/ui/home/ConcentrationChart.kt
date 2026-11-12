package com.fangshen.manual.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fangshen.manual.core.formatDoseBD
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.model.DoseUnit
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.drugId
import com.fangshen.manual.model.Route
import com.fangshen.manual.model.SubjectiveTagColors
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.continuous
import com.patrykandpatrick.vico.compose.cartesian.layer.dashed
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.layer.CartesianLayerDimensions
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Position
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import java.math.RoundingMode
import kotlin.math.roundToInt

private val HoursPerTickKey = ExtraStore.Key<Int>()

/** 纵轴刻度间距（由曲线最大值算出的“好看”步长，写入模型 extras 供刻度与格式化共用） */
private val ConcentrationStepKey = ExtraStore.Key<Double>()

/** 挑一个 1/2/2.5/5 × 10^k 的刻度间距，让曲线最大值附近约有 4~5 个刻度 */
private fun niceStep(max: Double): Double {
    if (max <= 0.0) return 1.0
    val target = max / 4
    val pow = kotlin.math.floor(kotlin.math.log10(target))
    val unit = Math.pow(10.0, pow)
    return listOf(1.0, 2.0, 2.5, 5.0, 10.0).first { it * unit >= target } * unit
}

/**
 * 血药浓度示意图（x 轴时间、y 轴浓度）：图例在图像上方，没有背景色与圆角，
 * 高度为屏幕高度的三分之一。即使没有服药记录也会显示（曲线贴 0）。
 *
 * 固定显示完整 48 小时：横轴按屏幕宽度四等分，每等分为 12 小时
 * （现在之前 1 个单位、之后 3 个单位），不能缩放也不能拖动。
 * 短按在 mg → g → µg → mg 之间循环切换剂量单位（不触发水波纹）。
 *
 * 该曲线是 Bateman 单室模型的示意推算（固定表观分布容积、忽略非线性代谢与蓄积），
 * 置信度很低，只能作为记录的参考，不能作为用药依据。
 */
@OptIn(ExperimentalLayoutApi::class)
/** 图表用的一个给药点：剂量 + 时间 + 途径 */
private data class DosePoint(
    val amount: java.math.BigDecimal,
    val time: Long,
    val route: Route,
)

@Composable
fun ConcentrationChart(
    drugs: List<DrugInfo>,
    doses: List<EntryEntity>,
    unit: DoseUnit,
    onUnitChange: (DoseUnit) -> Unit,
    highlightedDrugId: String?,
    onLegendClick: (String) -> Unit,
    /** 成瘾游标线常显（设置项；关闭时仅高亮药物曲线时显示其游标线） */
    showAddictionCursor: Boolean = false,
    modifier: Modifier = Modifier,
    now: Long,
) {
    val chartHeight = (LocalConfiguration.current.screenHeightDp / 3).dp
    val chartDrugs = remember(drugs, unit) { drugs.filter { it.doseUnit == unit } }
    val dosesByDrug = remember(doses) {
        doses.filter { it.type == "DOSE" && it.drugId != null }
            .groupBy { it.drugId!! }
            .mapValues { (_, entries) ->
                entries.map {
                    DosePoint(
                        it.doseAmount?.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO,
                        it.eventTime,
                        Route.fromName(it.route),
                    )
                }
            }
    }
    val activeDrugs = remember(chartDrugs, dosesByDrug, now) {
        chartDrugs.filter { drug ->
            dosesByDrug[drug.id]?.any { it.amount > java.math.BigDecimal.ZERO } == true
        }
    }

    Column(
        modifier = modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (activeDrugs.isNotEmpty()) {
            // 药名太多时单行横滑，绝不换行挤压到下方图表区域
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                activeDrugs.forEach { drug ->
                    LegendItem(
                        drug = drug,
                        highlighted = highlightedDrugId == null || highlightedDrugId == drug.id,
                        onClick = { onLegendClick(drug.id) },
                    )
                }
                Spacer(modifier = Modifier.size(12.dp))
            }
        }

        // 图表卡片：与上方"圆点 + 药名"图例行留出明显间隔，
        // 曲线与顶部网格线裁剪在卡片内，绝不再压到药名。
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(chartHeight)
                .clip(RoundedCornerShape(16.dp))
                .background(AlphaChartBg),
        ) {
            ChartCanvas(
                chartDrugs = chartDrugs,
                dosesByDrug = dosesByDrug,
                unit = unit,
                now = now,
                highlightedDrugId = highlightedDrugId,
                allDrugs = drugs,
                showAddictionCursor = showAddictionCursor,
                modifier = Modifier.fillMaxSize(),
            )
            // 顶层拦截点击：保证图表任何状态下短按都能循环切换剂量单位
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // key 必须是 unit：否则手势闭包只会捕获初次组合时的单位，
                    // 导致永远在 mg->g 之间来回，无法进入 µg。
                    .pointerInput(unit) {
                        detectTapGestures(
                            onTap = {
                                val next = when (unit) {
                                    DoseUnit.MG -> DoseUnit.G
                                    DoseUnit.G -> DoseUnit.MCG
                                    DoseUnit.MCG -> DoseUnit.MG
                                }
                                onUnitChange(next)
                            },
                        )
                    },
            )
        }
    }
}

@Composable
private fun LegendItem(
    drug: DrugInfo,
    highlighted: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .pointerInput(Unit) { detectTapGestures(onTap = { onClick() }) }
            .padding(vertical = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color(SubjectiveTagColors.colorOf(drug)).copy(alpha = if (highlighted) 1f else 0.3f)),
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = drug.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
                .copy(alpha = if (highlighted) 1f else 0.4f),
        )
    }
}

@Composable
private fun ChartCanvas(
    chartDrugs: List<DrugInfo>,
    dosesByDrug: Map<String, List<DosePoint>>,
    unit: DoseUnit,
    now: Long,
    highlightedDrugId: String?,
    /** 全部药物（含未入选本单位的药，供成瘾游标线计算） */
    allDrugs: List<DrugInfo> = emptyList(),
    /** 成瘾游标线常显（否则仅在高亮药物时显示该药的线） */
    showAddictionCursor: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textSize = 10.sp,
        ),
        valueFormatter = remember { NowLabelFormatter() },
        guideline = rememberLineComponent(
            fill = fill(MaterialTheme.colorScheme.primary),
            thickness = 2.dp,
        ),
    )
    // 只为真正服过药（本药库单位下有剂量记录）的药物建序列：
    // 全目录有上百种药，零浓度空线会让图表卡顿且压在坐标轴上。
    val seriesDrugs = remember(chartDrugs, dosesByDrug, now) {
        chartDrugs.filter { drug ->
            (dosesByDrug[drug.id] ?: emptyList()).any { it.amount > java.math.BigDecimal.ZERO }
        }
    }
    val xsHours = remember { (-720..2160 step 10).map { it / 60.0 } }
    val cursorLines = remember(allDrugs, dosesByDrug, now) {
        val records = dosesByDrug.entries.flatMap { (id, points) ->
            val drug = allDrugs.firstOrNull { it.id == id } ?: return@flatMap emptyList()
            val f = when (drug.doseUnit) {
                DoseUnit.MG -> 1.0
                DoseUnit.G -> 1000.0
                DoseUnit.MCG -> 0.001
            }
            points.map { p ->
                com.fangshen.manual.engine.DoseRecord(id, p.amount.toDouble() * f, p.route, p.time)
            }
        }
        com.fangshen.manual.engine.AddictionEngine.cursorLines(allDrugs, records, xsHours, now)
    }
    val visibleCursorLines = remember(cursorLines, highlightedDrugId, showAddictionCursor) {
        cursorLines.filter { line ->
            showAddictionCursor || (highlightedDrugId != null && highlightedDrugId in line.memberDrugIds)
        }
    }

    LaunchedEffect(seriesDrugs, dosesByDrug, now, visibleCursorLines, unit) {
        val (xs, seriesYs, cursorYs) = withContext(Dispatchers.Default) {
            // 固定 12 小时一个单位：现在之前 1 个单位、之后 3 个单位（-720..2160 分钟）
            val xs = xsHours
            val seriesYs: List<List<Double>> = if (seriesDrugs.isEmpty()) {
                listOf(xs.map { 0.0 })
            } else {
                seriesDrugs.map { drug ->
                    val past = (dosesByDrug[drug.id] ?: emptyList()).filter { it.amount > java.math.BigDecimal.ZERO }
                    if (com.fangshen.manual.engine.PkEngine.needsIntegration(drug)) {
                        // 非线性药代（容量限制消除/自诱导）：RK4 积分整条序列
                        val doseList = past.map { point ->
                            com.fangshen.manual.engine.DrugDose(
                                point.amount.toDouble(), (now - point.time) / 3_600_000.0,
                                point.route,
                            )
                        }
                        com.fangshen.manual.engine.PkEngine.concentrationSeries(
                            drug, doseList, xs,
                        )
                    } else {
                        xs.map { x ->
                            past.sumOf { point ->
                                val hoursAgo = (now - point.time) / 3_600_000.0
                                com.fangshen.manual.engine.PkEngine.concentrationAt(
                                    drug, point.amount.toDouble(), hoursAgo + x, point.route,
                                )
                            }
                        }
                    }
                }
            }
            // 成瘾游标线：内部 mg/L 换算到当前图表单位
            val unitFactor = when (unit) {
                DoseUnit.MG -> 1.0
                DoseUnit.G -> 1000.0
                DoseUnit.MCG -> 0.001
            }
            // 成瘾游标线画成与 x 轴平行的水平直线：取当前时刻（x=0）的游标高度
            val nowIdx = xs.indexOfFirst { it >= 0.0 }.coerceAtLeast(0)
            val cursorYs = visibleCursorLines.map { line ->
                val aNow = (line.ys.getOrElse(nowIdx) { line.ys.lastOrNull() ?: 0.0 }) / unitFactor
                xs.map { aNow }
            }
            Triple(xs, seriesYs, cursorYs)
        }
        // 纵轴刻度间距按曲线（含游标线）峰值确定，写入 extras 供坐标轴与标签格式化共用
        val yMax = (seriesYs.maxOfOrNull { it.maxOrNull() ?: 0.0 } ?: 0.0)
            .coerceAtLeast(cursorYs.maxOfOrNull { it.maxOrNull() ?: 0.0 } ?: 0.0)
            .coerceAtLeast(0.0)
        modelProducer.runTransaction {
            extras { extra ->
                extra[HoursPerTickKey] = 12
                extra[ConcentrationStepKey] = niceStep(yMax)
            }
            lineSeries {
                seriesYs.forEach { ys -> series(x = xs, y = ys) }
                cursorYs.forEach { ys -> series(x = xs, y = ys) }
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    if (seriesDrugs.isEmpty()) {
                        listOf(
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(fill(Color.Transparent)),
                            ),
                        )
                    } else {
                        val parent = seriesDrugs.firstOrNull { it.id == highlightedDrugId }
                        val metaboliteIds = parent?.activeMetabolites.orEmpty().map { it.drugId() }
                        // 代谢物曲线颜色与前药一致
                        val metaboliteColors = seriesDrugs.flatMap { d ->
                            d.activeMetabolites.map { it.drugId() to SubjectiveTagColors.colorOf(d) }
                        }.toMap()
                        val drugLines = seriesDrugs.map { drug ->
                            val focused = highlightedDrugId == null || highlightedDrugId == drug.id ||
                                drug.id in metaboliteIds
                            val baseColor = metaboliteColors[drug.id] ?: SubjectiveTagColors.colorOf(drug)
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(
                                    fill(
                                        Color(baseColor).copy(
                                            alpha = if (focused) 1f else 0.15f,
                                        ),
                                    ),
                                ),
                                stroke = LineCartesianLayer.LineStroke.continuous(
                                    thickness = if (highlightedDrugId == drug.id) 3.dp else 1.5.dp,
                                ),
                                pointProvider = null,
                            )
                        }
                        // 成瘾游标线：与 x 轴平行的水平虚线，颜色取其首个成员的标签色
                        val cursorLinesSpec = visibleCursorLines.map { line ->
                            val base = Color(line.colorArgb)
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(fill(base.copy(alpha = 0.85f))),
                                stroke = LineCartesianLayer.LineStroke.dashed(
                                    thickness = 1.5.dp,
                                    dashLength = 6.dp,
                                    gapLength = 4.dp,
                                ),
                                pointProvider = null,
                            )
                        }
                        drugLines + cursorLinesSpec
                    },
                ),
            ),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = remember(unit) { ConcentrationFormatter(unit) },
                // 固定纵轴刻度间距：默认按标签高度排出的刻度过密时，g/µg 单位下
                // 会舍入成一排相同的“0g”
                itemPlacer = remember { FixedVerticalTicksItemPlacer() },
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = remember { MinuteFormatter() },
                itemPlacer = remember { FixedTicksItemPlacer() },
                guideline = remember { LineComponent(fill = Fill(0xFFA79FB8.toInt()), thicknessDp = 1.4f) },
            ),
        ),
        modelProducer = modelProducer,
        zoomState = rememberVicoZoomState(
            zoomEnabled = false,
            initialZoom = Zoom.Content,
            minZoom = Zoom.Content,
            maxZoom = Zoom.Content,
        ),
        modifier = modifier.fillMaxSize(),
    )
}
/**
 * 固定五道竖线刻度：-720, 0, 720, 1440, 2160（当前、±12、±24、36 小时）。
 * 取消 Vico 自适应间距与可见范围裁剪，保证五条参考线与标签总是齐全。
 */
/**
 * 固定三道竖线刻度：当前（x=0）、+12 小时、+24 小时，将图像四等分。
 */
private class FixedTicksItemPlacer : HorizontalAxis.ItemPlacer {
    private fun values(): List<Double> = listOf(0.0, 12.0, 24.0)

    override fun getShiftExtremeLines(context: CartesianDrawingContext): Boolean = false

    override fun getFirstLabelValue(context: CartesianMeasuringContext, maxLabelWidth: Float): Double? = null

    override fun getLastLabelValue(context: CartesianMeasuringContext, maxLabelWidth: Float): Double? = null

    override fun getLabelValues(
        context: CartesianDrawingContext,
        visibleXRange: ClosedFloatingPointRange<Double>,
        fullXRange: ClosedFloatingPointRange<Double>,
        maxLabelWidth: Float,
    ): List<Double> = values()

    override fun getLineValues(
        context: CartesianDrawingContext,
        visibleXRange: ClosedFloatingPointRange<Double>,
        fullXRange: ClosedFloatingPointRange<Double>,
        maxLabelWidth: Float,
    ): List<Double>? = values()

    override fun getWidthMeasurementLabelValues(
        context: CartesianMeasuringContext,
        layerDimensions: CartesianLayerDimensions,
        fullXRange: ClosedFloatingPointRange<Double>,
    ): List<Double> = values()

    override fun getHeightMeasurementLabelValues(
        context: CartesianMeasuringContext,
        layerDimensions: CartesianLayerDimensions,
        fullXRange: ClosedFloatingPointRange<Double>,
        maxLabelWidth: Float,
    ): List<Double> = values()

    override fun getStartLayerMargin(
        context: CartesianMeasuringContext,
        layerDimensions: CartesianLayerDimensions,
        tickThickness: Float,
        maxLabelWidth: Float,
    ): Float = 0f

    override fun getEndLayerMargin(
        context: CartesianMeasuringContext,
        layerDimensions: CartesianLayerDimensions,
        tickThickness: Float,
        maxLabelWidth: Float,
    ): Float = 0f
}

private class MinuteFormatter : CartesianValueFormatter {
    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ): CharSequence {
        val hours = value.roundToInt()
        return when {
            hours == 0 -> "现在"
            else -> (if (hours > 0) "+" else "") + hours + "小时"
        }
    }
}

/**
 * 纵轴刻度：按 ConcentrationStepKey 的步长从 minY 排到 maxY。
 * 范围不足一个步长（例如未服药的平直 0 曲线）时只保留一个刻度，
 * 避免相邻刻度舍入成一排相同的“0g”。
 */
private class FixedVerticalTicksItemPlacer : VerticalAxis.ItemPlacer {
    private fun CartesianMeasuringContext.tickValues(
        axis: Axis.Position.Vertical,
    ): List<Double> {
        val step = model.extraStore.getOrNull(ConcentrationStepKey) ?: 1.0
        val range = ranges.getYRange(axis)
        val min = range.minY
        val max = range.maxY
        if (step <= 0.0 || max - min < step) return listOf(min)
        val values = mutableListOf<Double>()
        var v = min
        while (v <= max + step / 2) {
            values += v
            v += step
        }
        return values
    }

    override fun getShiftTopLines(context: CartesianDrawingContext): Boolean = false

    override fun getLabelValues(
        context: CartesianDrawingContext,
        labelScale: Float,
        itemScale: Float,
        axisPosition: Axis.Position.Vertical,
    ): List<Double> = context.tickValues(axisPosition)

    override fun getLineValues(
        context: CartesianDrawingContext,
        labelScale: Float,
        itemScale: Float,
        axisPosition: Axis.Position.Vertical,
    ): List<Double>? = context.tickValues(axisPosition)

    override fun getWidthMeasurementLabelValues(
        context: CartesianMeasuringContext,
        labelScale: Float,
        itemScale: Float,
        axisPosition: Axis.Position.Vertical,
    ): List<Double> = context.tickValues(axisPosition)

    override fun getHeightMeasurementLabelValues(
        context: CartesianMeasuringContext,
        axisPosition: Axis.Position.Vertical,
    ): List<Double> = context.tickValues(axisPosition)

    override fun getTopLayerMargin(
        context: CartesianMeasuringContext,
        position: Position.Vertical,
        transition: Float,
        tickThickness: Float,
    ): Float = 0f

    override fun getBottomLayerMargin(
        context: CartesianMeasuringContext,
        position: Position.Vertical,
        transition: Float,
        tickThickness: Float,
    ): Float = 0f
}

class ConcentrationFormatter(private val unit: DoseUnit) : CartesianValueFormatter {
    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ): CharSequence {
        // 按刻度间距动态取小数位：间距 0.0025g -> 4 位、5000µg -> 0 位，
        // 避免相邻刻度舍入成同一标签（例如一排"0g"）。
        val step = context.model.extraStore.getOrNull(ConcentrationStepKey) ?: 0.0
        val scale = if (step > 0.0) {
            (kotlin.math.ceil(-kotlin.math.log10(step)).toInt() + 1).coerceIn(0, 6)
        } else {
            if (unit == DoseUnit.G) 4 else 2
        }
        return formatDoseBD(
            value.toBigDecimal().setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros(),
        ) + unit.shortLabel
    }
}

private class NowLabelFormatter : com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker.ValueFormatter {
    override fun format(
        context: CartesianDrawingContext,
        targets: List<CartesianMarker.Target>,
    ): CharSequence = "现在"
}


/** 图表卡片底色：比页面背景略深一点，把曲线区域与药名图例清楚隔开。 */
private val AlphaChartBg = Color(0xFFF6F1FA)

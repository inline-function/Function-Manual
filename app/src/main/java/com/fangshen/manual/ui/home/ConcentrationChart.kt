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
import com.fangshen.manual.model.SubjectiveTagColors
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.continuous
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

private val MinutesPerTickKey = ExtraStore.Key<Int>()

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
@Composable
fun ConcentrationChart(
    drugs: List<DrugInfo>,
    doses: List<EntryEntity>,
    unit: DoseUnit,
    onUnitChange: (DoseUnit) -> Unit,
    highlightedDrugId: String?,
    onLegendClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    now: Long,
) {
    val chartHeight = (LocalConfiguration.current.screenHeightDp / 3).dp
    val chartDrugs = remember(drugs, unit) { drugs.filter { it.doseUnit == unit } }
    val dosesByDrug = remember(doses) {
        doses.filter { it.type == "DOSE" && it.drugId != null }
            .groupBy { it.drugId!! }
            .mapValues { (_, entries) ->
                entries.map { (it.doseAmount?.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO) to it.eventTime }
            }
    }
    val activeDrugs = remember(chartDrugs, dosesByDrug, now) {
        chartDrugs.filter { drug ->
            dosesByDrug[drug.id]?.any { (amount, time) ->
                amount > java.math.BigDecimal.ZERO && time <= now
            } == true
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
    dosesByDrug: Map<String, List<Pair<java.math.BigDecimal, Long>>>,
    unit: DoseUnit,
    now: Long,
    highlightedDrugId: String?,
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
            (dosesByDrug[drug.id] ?: emptyList()).any { (amount, time) ->
                amount > java.math.BigDecimal.ZERO && time <= now
            }
        }
    }
    LaunchedEffect(seriesDrugs, dosesByDrug, now) {
        val (xs, seriesYs) = withContext(Dispatchers.Default) {
            // 固定 12 小时一个单位：现在之前 1 个单位、之后 3 个单位（-720..2160 分钟）
            val xs = (-720..2160 step 10).map { it.toDouble() }
            val seriesYs: List<List<Double>> = if (seriesDrugs.isEmpty()) {
                listOf(xs.map { 0.0 })
            } else {
                seriesDrugs.map { drug ->
                    val past = (dosesByDrug[drug.id] ?: emptyList()).filter { it.second <= now }
                    if (com.fangshen.manual.engine.PkEngine.needsIntegration(drug)) {
                        // 非线性药代（容量限制消除/自诱导）：RK4 积分整条序列
                        val doseList = past.map { (amount, time) ->
                            com.fangshen.manual.engine.DrugDose(
                                amount.toDouble(), (now - time) / 3_600_000.0,
                            )
                        }
                        com.fangshen.manual.engine.PkEngine.concentrationSeries(
                            drug, doseList, xs.map { it / 60.0 },
                        )
                    } else {
                        xs.map { x ->
                            past.sumOf { (amount, time) ->
                                val hoursAgo = (now - time) / 3_600_000.0
                                com.fangshen.manual.engine.PkEngine.concentrationAt(
                                    drug, amount.toDouble(), hoursAgo + x / 60.0,
                                )
                            }
                        }
                    }
                }
            }
            xs to seriesYs
        }
        // 纵轴刻度间距按曲线峰值确定，写入 extras 供坐标轴与标签格式化共用
        val yMax = seriesYs.maxOf { series -> series.maxOrNull() ?: 0.0 }.coerceAtLeast(0.0)
        modelProducer.runTransaction {
            extras { extra ->
                extra[MinutesPerTickKey] = 72
                extra[ConcentrationStepKey] = niceStep(yMax)
            }
            lineSeries {
                seriesYs.forEach { ys -> series(x = xs, y = ys) }
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
                        seriesDrugs.map { drug ->
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(
                                    fill(
                                        Color(SubjectiveTagColors.colorOf(drug)).copy(
                                            alpha = if (highlightedDrugId == null || highlightedDrugId == drug.id) 1f else 0.15f,
                                        ),
                                    ),
                                ),
                                stroke = LineCartesianLayer.LineStroke.continuous(
                                    thickness = if (highlightedDrugId == drug.id) 3.dp else 1.5.dp,
                                ),
                                pointProvider = null,
                            )
                        }
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
                guideline = remember { LineComponent(fill = Fill(0xFFB8B3C4.toInt()), thicknessDp = 1f) },
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
 * 固定五道竖线刻度：-720, 0, 720, 1440, 2160（-12h、当前、+12h、+24h、+36h）。
 * 标签与竖线均取这五个值；几何（边界标签、边距）与 Vico aligned 放置器保持一致，
 * 避免拉伸或裁剪图表区域。
 */
private class FixedTicksItemPlacer : HorizontalAxis.ItemPlacer {
    private fun CartesianMeasuringContext.tickValues(): List<Double> =
        (0..4).map { ranges.minX + it * 720.0 }

    private fun CartesianMeasuringContext.measuredValues(): List<Double> = buildList {
        add(ranges.minX)
        if (ranges.xLength < ranges.xStep) return@buildList
        add(ranges.minX + ranges.xStep * kotlin.math.floor(ranges.xLength / ranges.xStep))
        if (ranges.xLength >= 2 * ranges.xStep) {
            val midSteps = (ranges.xLength / 2 / ranges.xStep).toInt()
            add(ranges.minX + ranges.xStep * midSteps)
        }
    }

    override fun getShiftExtremeLines(context: CartesianDrawingContext): Boolean = true

    override fun getFirstLabelValue(context: CartesianMeasuringContext, maxLabelWidth: Float): Double? =
        context.ranges.minX

    override fun getLastLabelValue(context: CartesianMeasuringContext, maxLabelWidth: Float): Double? =
        context.ranges.maxX

    override fun getLabelValues(
        context: CartesianDrawingContext,
        visibleXRange: ClosedFloatingPointRange<Double>,
        fullXRange: ClosedFloatingPointRange<Double>,
        maxLabelWidth: Float,
    ): List<Double> = context.tickValues()

    override fun getLineValues(
        context: CartesianDrawingContext,
        visibleXRange: ClosedFloatingPointRange<Double>,
        fullXRange: ClosedFloatingPointRange<Double>,
        maxLabelWidth: Float,
    ): List<Double>? = context.tickValues()

    override fun getWidthMeasurementLabelValues(
        context: CartesianMeasuringContext,
        layerDimensions: CartesianLayerDimensions,
        fullXRange: ClosedFloatingPointRange<Double>,
    ): List<Double> = context.measuredValues()

    override fun getHeightMeasurementLabelValues(
        context: CartesianMeasuringContext,
        layerDimensions: CartesianLayerDimensions,
        fullXRange: ClosedFloatingPointRange<Double>,
        maxLabelWidth: Float,
    ): List<Double> = context.measuredValues()

    override fun getStartLayerMargin(
        context: CartesianMeasuringContext,
        layerDimensions: CartesianLayerDimensions,
        tickThickness: Float,
        maxLabelWidth: Float,
    ): Float = (tickThickness - layerDimensions.unscalableStartPadding).coerceAtLeast(0f)

    override fun getEndLayerMargin(
        context: CartesianMeasuringContext,
        layerDimensions: CartesianLayerDimensions,
        tickThickness: Float,
        maxLabelWidth: Float,
    ): Float = (tickThickness - layerDimensions.unscalableEndPadding).coerceAtLeast(0f)
}

private class MinuteFormatter : CartesianValueFormatter {
    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ): CharSequence {
        val minutes = value.roundToInt()
        android.util.Log.d("FSCHART", "fmt v=$value")
        return when {
            minutes == 0 -> "现在"
            minutes % 60 == 0 -> (if (minutes > 0) "+" else "") + (minutes / 60) + "小时"
            else -> (if (minutes > 0) "+" else "") + minutes + "分"
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

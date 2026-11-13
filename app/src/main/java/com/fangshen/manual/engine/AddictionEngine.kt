package com.fangshen.manual.engine

import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.effectiveRoutes
import com.fangshen.manual.model.Route
import kotlin.math.abs

/** 一条服药记录的最小形态（供游标线计算，与持久化层解耦） */
data class DoseRecord(
    val drugId: String,
    val amountMg: Double,
    val route: Route?,
    val time: Long,
)

/**
 * 成瘾游标线引擎（示意性模型，非临床定量）。
 *
 * - 只处理声明了成瘾性的药物（[DrugInfo.addictive]）；
 * - 游标线 A 以固定速率（该药声明的小时成瘾速度）始终朝当前血药浓度 C 移动：
 *   C > A 时上升、C < A 时下降，一步不越界；
 * - 交叉耐药组（[DrugInfo.toleranceGroup]）共享一条线，C 取组内成员浓度之和，
 *   速率取组内最大值；
 * - 效应调制：换算剂量 = 原剂量 × |A−C| / A。浓度贴着游标（耐受）→ 换算≈0；
 *   C 明显低于 A（戒断）→ 换算保留并给"戒断"效应加权。
 *
 * 浓度采用线性 Bateman 闭式解近似（非线性药代药物亦同），仅供形成直觉。
 */
object AddictionEngine {

    /** 一条游标线在图表采样点上的序列 */
    data class CursorLine(
        val key: String,
        val name: String,
        val colorArgb: Long,
        val memberDrugIds: List<String>,
        val ys: List<Double>,
    )

    /** 单药当前状态：C 为自身浓度，A 为所属游标线高度（mg/L 内部单位） */
    data class DrugState(val concentration: Double, val cursor: Double, val lineKey: String)

    
    /** 耐受"启动"门槛：窗口内需有 ≥ 此天数的不同服药日，游标才开始累积。
     * 单次/偶发用药不产生耐受（群友 v3.0.0 反馈：佐匹克隆单次给药后游标几小时就追上浓度，
     * 把次日残留效应"嗜睡"抹掉了）。 */
    private const val PRIME_DAYS = 3

    /** 事件是否满足启动门槛（按服药日去重计数） */
    private fun primed(events: List<Ev>, now: Long): Boolean {
        if (events.isEmpty()) return false
        val days = HashSet<Long>()
        for (e in events) {
            val epoch = now + (e.t * 3_600_000.0).toLong()
            days.add(epoch / 86_400_000L)
        }
        return days.size >= PRIME_DAYS
    }
private const val STEP_H = 0.25
    private const val MAX_HISTORY_H = 24.0 * 60.0

    /**
     * 计算 [doses] 涉及的全部成瘾游标线，序列与 [xsHours]（相对现在，过去为负）对齐。
     * [drugs] 为全部药物（用于查表），返回值已按曲线换算回各单元首个药物的剂量单位。
     */
    fun cursorLines(
        drugs: List<DrugInfo>,
        doses: List<DoseRecord>,
        xsHours: List<Double>,
        now: Long,
    ): List<CursorLine> {
        if (xsHours.isEmpty()) return emptyList()
        val byId = drugs.associateBy { it.id }
        val addictive = doses.mapNotNull { d -> byId[d.drugId]?.takeIf { it.addictive } }
        if (addictive.isEmpty()) return emptyList()
        // 按耐药组归并
        val groups = LinkedHashMap<String, MutableList<DrugInfo>>()
        for (drug in addictive) {
            val key = if (drug.toleranceGroup != null) "group:" + drug.toleranceGroup else "self:" + drug.id
            groups.getOrPut(key) { mutableListOf() } += drug
        }
        val tMax = (xsHours.maxOrNull() ?: 0.0) + 1.0
        return groups.mapNotNull { (key, members) ->
            val unitDoses = doses.filter { it.drugId in members.map { m -> m.id } }
            val events = unitDoses.mapNotNull { d ->
                val drug = byId[d.drugId] ?: return@mapNotNull null
                if (d.amountMg <= 0.0) return@mapNotNull null
                Ev(
                    t = (d.time - now) / 3_600_000.0,
                    mg = d.amountMg,
                    route = d.route ?: drug.effectiveRoutes().first(),
                    drug = drug,
                )
            }.sortedBy { it.t }
            if (events.isEmpty()) return@mapNotNull null
            // 未达启动门槛：还没有耐受，不画游标线
            if (!primed(events, now)) return@mapNotNull null
            val rate = members.maxOf { it.addictionRatePerHour.coerceAtLeast(0.0) }
            val trail = simulate(events, rate, xsHours, tMax)
            val head = members.first()
            CursorLine(
                key = key,
                name = if (members.size > 1) "耐药组·" + (head.toleranceGroup ?: "") else head.name,
                colorArgb = com.fangshen.manual.model.SubjectiveTagColors.colorOf(head).toLong(),
                memberDrugIds = members.map { it.id },
                ys = xsHours.map { x -> interpolate(trail, x) },
            )
        }
    }

    /** 全部成瘾药在现在时刻的状态（供效应引擎调制） */
    fun statesAtNow(
        drugs: List<DrugInfo>,
        doses: List<DoseRecord>,
        now: Long,
    ): Map<String, DrugState> {
        val byId = drugs.associateBy { it.id }
        val result = mutableMapOf<String, DrugState>()
        val addicting = doses.mapNotNull { byId[it.drugId]?.takeIf { d -> d.addictive } }.distinct()
        for (drug in addicting) {
            val key = if (drug.toleranceGroup != null) "group:" + drug.toleranceGroup else "self:" + drug.id
            val members = addicting.filter {
                val k = if (it.toleranceGroup != null) "group:" + it.toleranceGroup else "self:" + it.id
                k == key
            }
            val unitDoses = doses.filter { it.drugId in members.map { m -> m.id } }
            val events = unitDoses.mapNotNull { d ->
                val dr = byId[d.drugId] ?: return@mapNotNull null
                if (d.amountMg <= 0.0) return@mapNotNull null
                Ev(
                    t = (d.time - now) / 3_600_000.0,
                    mg = d.amountMg,
                    route = d.route ?: dr.effectiveRoutes().first(),
                    drug = dr,
                )
            }.sortedBy { it.t }
            if (events.isEmpty()) continue
            // 未达启动门槛：单次/偶发用药不产生耐受，不参与调制
            if (!primed(events, now)) continue
            val rate = members.maxOf { it.addictionRatePerHour.coerceAtLeast(0.0) }
            val trail = simulate(events, rate, listOf(-1.0, 0.0), 1.0)
            val a = interpolate(trail, 0.0)
            val c = concentrationAt(events, 0.0)
            result[drug.id] = DrugState(concentration = c, cursor = a, lineKey = key)
        }
        return result
    }

    // —— 内部 ——

    private data class Ev(val t: Double, val mg: Double, val route: Route, val drug: DrugInfo)

    private fun simulate(
        events: List<Ev>,
        ratePerHour: Double,
        xsHours: List<Double>,
        tMax: Double,
    ): List<DoubleArray> {
        val tFirst = events.map { it.t }.minOrNull() ?: 0.0
        val tStart = maxOf(minOf(tFirst, xsHours.minOrNull() ?: 0.0), -MAX_HISTORY_H)
        var a = 0.0
        val trail = ArrayList<DoubleArray>(2048)
        trail.add(doubleArrayOf(tStart, 0.0))
        var t = tStart
        while (t < tMax) {
            val h = minOf(STEP_H, tMax - t)
            val c = concentrationAt(events, t)
            val target = if (c > a) minOf(a + ratePerHour * h, c) else maxOf(a - ratePerHour * h, c)
            a = target.coerceAtLeast(0.0)
            t += h
            trail.add(doubleArrayOf(t, a))
        }
        return trail
    }

    /** t 时刻该单元的浓度之和（mg/L 内部） */
    private fun concentrationAt(events: List<Ev>, t: Double): Double {
        var sum = 0.0
        for (e in events) {
            val hoursAfter = t - e.t
            if (hoursAfter < 0) continue
            sum += PkEngine.concentrationAt(e.drug, e.mg, hoursAfter, e.route)
        }
        return sum
    }

    private fun interpolate(trail: List<DoubleArray>, x: Double): Double {
        if (trail.isEmpty()) return 0.0
        var lo = 0
        var hi = trail.size - 1
        if (x <= trail[lo][0]) return trail[lo][1]
        if (x >= trail[hi][0]) return trail[hi][1]
        while (hi - lo > 1) {
            val mid = (lo + hi) / 2
            if (trail[mid][0] <= x) lo = mid else hi = mid
        }
        val t0 = trail[lo][0]; val v0 = trail[lo][1]
        val t1 = trail[hi][0]; val v1 = trail[hi][1]
        if (t1 - t0 < 1e-12) return v0
        return v0 + (v1 - v0) * (x - t0) / (t1 - t0)
    }
}

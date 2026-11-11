package com.fangshen.manual.engine

import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.DoseUnit
import com.fangshen.manual.model.Route
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.ln

/**
 * 血药浓度推算引擎（示意级）。
 *
 * 采用单室 Bateman 方程：口服等血管外给药后先吸收、后消除；静脉注射视为瞬时吸收。
 * 固定使用 V = 50 L 的表观分布容积，不考虑多个蛋白结合率、代谢产物与重复给药蓄积之间的
 * 非线性关系，因此结果仅是示意值，置信度很低，不可作为用药依据。
 */
object PkEngine {
    const val VOLUME_OF_DISTRIBUTION_L = 50.0

    private const val MIN_HALF_LIFE_H = 0.25
    private const val MAX_HALF_LIFE_H = 24.0 * 30.0
    private const val MIN_T_MAX_H = 0.1
    private const val MAX_T_MAX_H = 24.0

    /** 消除速率常数（1/h） */
    fun eliminationConstant(halfLifeHours: Double): Double =
        ln(2.0) / halfLifeHours.coerceIn(MIN_HALF_LIFE_H, MAX_HALF_LIFE_H)

    /** 吸收速率常数（1/h），由达峰时间反推 */
    fun absorptionConstant(drug: DrugInfo): Double {
        if (drug.route == Route.IV || drug.route == Route.IM) return 20.0
        val t = (drug.tmaxHours ?: defaultTmaxHours(drug.route)).coerceIn(MIN_T_MAX_H, MAX_T_MAX_H)
        // 使浓度峰值出现在 t_max 附近：ka ≈ ln(2) / t_max + ke 的近似校正
        return (ln(2.0) / t) + eliminationConstant(drug.halfLifeHours)
    }

    private fun defaultTmaxHours(route: Route): Double = when (route) {
        Route.IV -> 0.05
        Route.IM -> 0.5
        Route.INSUFFLATION -> 0.75
        Route.SUBLINGUAL -> 1.0
        Route.ORAL -> 2.0
        Route.RECTAL -> 1.5
        else -> 1.5
    }

    /**
     * 单次给药后 [hoursAfter] 小时的血药浓度（单位/L），不足零返回 0。
     */
    fun concentrationAt(drug: DrugInfo, doseAmount: Double, hoursAfter: Double): Double {
        if (hoursAfter < 0 || doseAmount <= 0.0) return 0.0
        val f = (drug.bioavailabilityPct.coerceIn(0, 100)) / 100.0
        val ke = eliminationConstant(drug.halfLifeHours)
        val ka = absorptionConstant(drug)
        val denominator = ka - ke
        if (abs(denominator) < 1e-6) return doseAmount * f / VOLUME_OF_DISTRIBUTION_L
        val c = doseAmount * f * ka / (VOLUME_OF_DISTRIBUTION_L * denominator) *
            (exp(-ke * hoursAfter) - exp(-ka * hoursAfter))
        return if (c > 0 && c.isFinite()) c else 0.0
    }

    /** 多次给药叠加后的总浓度 */
    fun sumConcentrationAt(drug: DrugInfo, doses: List<DrugDose>, hoursAfter: Double): Double =
        doses.sumOf { concentrationAt(drug, it.amount, hoursAfter + it.hoursAgoAtNow) }

    // ===== v3：非线性（容量限制消除 / 自诱导 / 吸收饱和）======

    /** 该药的表观分布容积（L）：未声明时用全局默认 50L */
    fun vd(drug: DrugInfo): Double = drug.vdLiters ?: VOLUME_OF_DISTRIBUTION_L

    /** 是否需要数值积分：只要声明非线性消除或自诱导就走积分路径 */
    fun needsIntegration(drug: DrugInfo): Boolean =
        drug.nonlinearity != null || drug.autoInduction != null

    /** 剂量单位 → mg 的换算系数 */
    private fun mgFactor(unit: DoseUnit): Double = when (unit) {
        DoseUnit.MG -> 1.0
        DoseUnit.G -> 1000.0
        DoseUnit.MCG -> 0.001
    }

    /**
     * 饱和吸收下的生物利用度分数：F(D) = fMax·d50/(d50+D)；未声明则取 [DrugInfo.bioavailabilityPct]。
     * 剂量大时吸收饱和药物（加巴喷丁）的 F 明显下降。
     */
    fun bioavailabilityFraction(drug: DrugInfo, doseMg: Double): Double {
        val sat = drug.saturableAbsorption ?: return drug.bioavailabilityPct.coerceIn(0, 100) / 100.0
        val fMax = sat.fMaxPct.coerceIn(0, 100) / 100.0
        return fMax * sat.d50Mg / (sat.d50Mg + doseMg.coerceAtLeast(0.0))
    }

    /**
     * 计算整条浓度序列（按药物自身剂量单位：单位/L）。
     *
     * 线性药逐点调用闭式解；非线性药（Michaelis–Menten 或自诱导）用 RK4 积分
     * 吸收室→中央室两条 ODE。剂量内部统一换算成 mg，输出再换算回药物单位，
     * 保证浓度刻度与线性路径一致。
     *
     * @param xsHours 相对现在的小时偏移（负数=过去，正数=未来）
     */
    fun concentrationSeries(drug: DrugInfo, doses: List<DrugDose>, xsHours: List<Double>): List<Double> {
        if (!needsIntegration(drug)) {
            return xsHours.map { x -> sumConcentrationAt(drug, doses, x) }
        }
        val sim = NonlinearSim(drug, doses, xsHours)
        return sim.run()
    }

    private data class Event(val t: Double, val mg: Double)

    /** 非线性药物的数值积分器：状态 (aGut 吸收室, aC 中央室)，RK4 步进 */
    private class NonlinearSim(
        private val drug: DrugInfo,
        doses: List<DrugDose>,
        private val xsHours: List<Double>,
    ) {
        private val mg = mgFactor(drug.doseUnit)
        private val vd = vd(drug)
        private val ke0 = eliminationConstant(drug.halfLifeHours)
        private val ka = absorptionConstant(drug)
        private val mm = drug.nonlinearity
        private val ind = drug.autoInduction
        private val injected = drug.route == Route.IV || drug.route == Route.IM

        private val firstDoseHoursAgo = doses.maxOfOrNull { it.hoursAgoAtNow } ?: 0.0
        private val tStart = minOf(xsHours.minOrNull() ?: 0.0, -firstDoseHoursAgo)
        private val tEnd = xsHours.maxOrNull() ?: 0.0
        private val pending = doses.map { Event(-it.hoursAgoAtNow, it.amount * mg) }
            .filter { it.mg > 0.0 }.sortedBy { it.t }.toMutableList()

        private var aGut = 0.0
        private var aC = 0.0
        private val samples = HashMap<Double, Double>(xsHours.size)

        fun run(): List<Double> {
            preState()
            sample()
            var t = tStart
            val h = 0.1
            while (t < tEnd - 1e-9) {
                val nextT = (pending.firstOrNull()?.t ?: Double.POSITIVE_INFINITY).coerceAtMost(t + h)
                val step = (nextT - t).coerceAtLeast(1e-6)
                rk4(step, t)
                t = nextT
                val ev = pending.firstOrNull()
                if (ev != null && abs(ev.t - t) < 1e-9) {
                    pending.removeAt(0)
                    if (injected) aC += ev.mg else aGut += ev.mg * bioavailabilityFraction(drug, ev.mg)
                }
                sample()
            }
            return xsHours.map { x -> samples[x] ?: 0.0 }
        }

        /** 早于窗口起点的剂量残留：近似按线性衰减折算初始状态 */
        private fun preState() {
            for (e in pending) {
                if (e.t >= tStart) break
                val elapsed = tStart - e.t
                if (injected) {
                    aC += e.mg * exp(-ke0 * elapsed)
                } else {
                    val total = e.mg * bioavailabilityFraction(drug, e.mg)
                    val remainGut = total * exp(-ka * elapsed)
                    aGut += remainGut
                    aC += (total - remainGut) * exp(-ke0 * elapsed)
                }
            }
        }

        private fun sample() {
            // 状态写入最近的采样点
            xsHours.forEach { x ->
                samples.putIfAbsent(x, aC / vd / mg)
            }
        }

        private fun inductionFactor(t: Double): Double {
            if (ind == null) return 1.0
            // 连续用药天数按距最早一次服药计
            val days = (firstDoseHoursAgo + t) / 24.0
            return 1.0 + (ind.maxFactor - 1.0) * (1.0 - exp(-days / ind.daysToSteady))
        }

        private fun gutDeriv(g: Double) = if (injected) 0.0 else -ka * g

        private fun centralDeriv(g: Double, c: Double, t: Double): Double {
            val input = if (injected) 0.0 else ka * g
            return if (mm != null) {
                val conc = c / vd
                input - vd * mm.vmaxMgPerLPerHour * conc / (mm.kmMgPerL + conc)
            } else {
                input - ke0 * inductionFactor(t) * c
            }
        }

        private fun rk4(step: Double, t: Double) {
            val g = aGut
            val c = aC
            val k1g = gutDeriv(g); val k1c = centralDeriv(g, c, t)
            val k2g = gutDeriv(g + step / 2 * k1g); val k2c = centralDeriv(g + step / 2 * k1g, c + step / 2 * k1c, t + step / 2)
            val k3g = gutDeriv(g + step / 2 * k2g); val k3c = centralDeriv(g + step / 2 * k2g, c + step / 2 * k2c, t + step / 2)
            val k4g = gutDeriv(g + step * k3g); val k4c = centralDeriv(g + step * k3g, c + step * k3c, t + step)
            aGut = g + step / 6.0 * (k1g + 2 * k2g + 2 * k3g + k4g)
            aC = c + step / 6.0 * (k1c + 2 * k2c + 2 * k3c + k4c)
            aGut = aGut.coerceAtLeast(0.0); aC = aC.coerceAtLeast(0.0)
        }
    }
}

/** 一次给药：[amount] 为剂量，[hoursAgoAtNow] 为该次给药距现在的小时数（过去为正） */
data class DrugDose(val amount: Double, val hoursAgoAtNow: Double)

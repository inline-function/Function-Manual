package com.fangshen.manual.engine

import com.fangshen.manual.model.DrugInfo
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
}

/** 一次给药：[amount] 为剂量，[hoursAgoAtNow] 为该次给药距现在的小时数（过去为正） */
data class DrugDose(val amount: Double, val hoursAgoAtNow: Double)

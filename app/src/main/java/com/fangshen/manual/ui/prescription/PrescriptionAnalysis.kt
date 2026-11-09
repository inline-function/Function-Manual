package com.fangshen.manual.ui.prescription

import com.fangshen.manual.core.enzymeArrowsSuffix
import com.fangshen.manual.engine.InteractionEngine
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.Prescription
import com.fangshen.manual.model.SubjectiveTagColors
import java.math.BigDecimal

/** 处方卡片/截图中一行药物的展示信息（圆角小竖条、名称、酶箭头、给药途径、剂量） */
data class RxRowResolved(
    val name: String,
    val dose: String,
    val colorArgb: Long,
    val routeLabel: String,
    val arrows: String,
)

/** 处方整页渲染所需的数据（计算效应 + 酶箭头都依赖当前药物目录） */
data class PrescriptionShotData(
    val name: String,
    val rows: List<RxRowResolved>,
    val effects: List<PrescriptionEffect>,
    val indications: List<String> = emptyList(),
    val unmatched: List<String> = emptyList(),
)

/** 把一份处方在当前目录下解析成卡片/截图所需的数据 */
fun buildShotData(
    catalog: List<DrugInfo>,
    parsed: Prescription,
): PrescriptionShotData {
    val resolved = parsed.drugs.mapNotNull { row ->
        catalog.firstOrNull { it.name == row.name.trim() }
    }
    val rows = parsed.drugs.map { row ->
        val drug = catalog.firstOrNull { it.name == row.name.trim() }
        RxRowResolved(
            name = row.name,
            dose = row.dose,
            colorArgb = if (drug != null) SubjectiveTagColors.colorOf(drug) else 0xFFB8B3C4L,
            routeLabel = drug?.route?.label ?: "",
            arrows = if (drug != null) enzymeArrowsSuffix(drug, resolved) else "",
        )
    }
    val form = PrescriptionForm(
        name = parsed.name,
        drugs = parsed.drugs.map { DrugRow(it.name, it.dose) },
        indications = parsed.indications,
    )
    val analysis = computeAnalysis(catalog, form)
    return PrescriptionShotData(
        name = parsed.name,
        rows = rows,
        effects = analysis.effectGroups(),
        indications = parsed.indications,
        unmatched = analysis.unmatched,
    )
}


/** 处方相互作用分析：列表“长截图”与编辑页共用 */

/** 卡片/截图中一个估算效应：名称 + 合计权重（权重映射成程度颜色与闪烁） */
data class PrescriptionEffect(val name: String, val weight: Int)

fun PrescriptionAnalysis.effectGroups(): List<PrescriptionEffect> =
    effectLines.map { line ->
        val w = line.substringAfterLast('+').trim().toIntOrNull() ?: 0
        PrescriptionEffect(line.substringBeforeLast('+').trim(), w)
    }

fun computeAnalysis(catalog: List<DrugInfo>, form: PrescriptionForm): PrescriptionAnalysis {
    val resolved = mutableListOf<DrugInfo>()
    val amounts = mutableListOf<BigDecimal?>()
    val unmatched = mutableListOf<String>()
    form.drugs.forEach { row ->
        val drug = catalog.firstOrNull { it.name == row.name.trim() }
        if (drug != null) {
            resolved += drug
            amounts += parseAmount(row.dose) ?: drug.tabletDose
        } else if (row.name.isNotBlank()) {
            unmatched += row.name.trim()
        }
    }
    // 1. 同时服用的效应（按各药剂量规则估算权重）
    val effects = mutableMapOf<String, Int>()
    resolved.forEachIndexed { i, drug ->
        val amount = amounts[i] ?: return@forEachIndexed
        drug.rules.forEach { rule ->
            if (amount >= rule.threshold) {
                effects[rule.effectName] = (effects[rule.effectName] ?: 0) + rule.weight
            }
        }
    }
    val effectLines = effects.entries
        .sortedByDescending { it.value }
        .map { (name, w) -> "${name}  +${w}" }
    // 2. 酶的诱导、抑制与底物
    val inhibitors = mutableMapOf<String, MutableList<String>>()
    val inducers = mutableMapOf<String, MutableList<String>>()
    val substrates = mutableMapOf<String, MutableList<String>>()
    resolved.forEach { drug ->
        drug.inhibitors.forEach { e -> inhibitors.getOrPut(e) { mutableListOf() } += drug.name }
        drug.inducers.forEach { e -> inducers.getOrPut(e) { mutableListOf() } += drug.name }
        drug.substrates.forEach { e -> substrates.getOrPut(e) { mutableListOf() } += drug.name }
    }
    val enzymeLines = buildList {
        inhibitors.forEach { (e, names) -> add("${e} 抑制（${names.joinToString("、")}）") }
        inducers.forEach { (e, names) -> add("${e} 诱导（${names.joinToString("、")}）") }
        substrates.forEach { (e, names) -> add("${e} 底物（${names.joinToString("、")}）") }
    }
    // 3. 药物相互作用
    val warnings = InteractionEngine.check(resolved.distinctBy { it.id })
        .map { it.message }
    return PrescriptionAnalysis(effectLines, enzymeLines, warnings, unmatched.distinct())
}

/** 抽取剂量文本中的数字，如 "50 mg" -> 50 */
private val DoseNumber = Regex("^\\s*(\\d+(?:\\.\\d+)?)")

private fun parseAmount(dose: String): BigDecimal? =
    DoseNumber.find(dose)?.groupValues?.get(1)?.toBigDecimalOrNull()

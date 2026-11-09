package com.fangshen.manual.model

import org.json.JSONArray
import org.json.JSONObject

/**
 * 处方：名称（如"加州火箭燃料"、"氟奥合剂"）+ 药物（名称+剂量）+ 适应症。
 * 以 JSON 存于 fangshen/prescriptions 目录（.json 后缀）：
 *
 * {
 *   "name" : "加州火箭燃料",
 *   "drugs" : [ { "name" : "氟伏沙明", "dose" : "50 mg" } ],
 *   "indications" : [ "抑郁症" ]
 * }
 */
data class PrescriptionDrug(
    val name: String,
    val dose: String,
)

data class Prescription(
    val name: String,
    val drugs: List<PrescriptionDrug>,
    val indications: List<String>,
)

fun parsePrescription(json: String): Prescription {
    val o = JSONObject(json)
    val drugs = o.optJSONArray("drugs")?.let { arr ->
        List(arr.length()) { i ->
            val d = arr.optJSONObject(i) ?: return@List PrescriptionDrug("", "")
            PrescriptionDrug(
                name = d.optString("name", "").trim(),
                dose = d.optString("dose", "").trim(),
            )
        }.filter { it.name.isNotEmpty() }
    } ?: emptyList()
    val indications = o.optJSONArray("indications")?.let { arr ->
        List(arr.length()) { i -> arr.optString(i, "").trim() }
            .filter { it.isNotEmpty() }
    } ?: emptyList()
    return Prescription(
        name = o.optString("name", "").trim(),
        drugs = drugs,
        indications = indications,
    )
}

fun prescriptionTemplate(name: String): String = JSONObject().apply {
    put("name", name)
    put("drugs", JSONArray())
    put("indications", JSONArray())
}.toString(4)

package com.fangshen.manual.ui.navigation

object Routes {
    const val HOME = "home"
    const val DRUG_LIST = "drug_list"
    const val DRUG_DETAIL = "drug_detail/{drugId}"
    const val DRUG_EDIT = "drug_edit/{drugId}"
    const val ENTRY_NEW = "entry_new/{type}?uri={uri}&drugId={drugId}"
    const val ENTRY_DETAIL = "entry_detail/{entryId}"
    const val ENTRY_EDIT = "entry_edit/{entryId}"
    const val SCHEDULE_LIST = "schedule_list"
    const val REPORTS = "reports"
    const val REPORT_READER = "report_reader/{fileName}"
    const val REPORT_EDIT = "report_edit/{fileName}"
    const val PRESCRIPTIONS = "prescriptions"
    const val PRESCRIPTION_EDIT = "prescription_edit/{displayName}"
    const val PROFILE = "profile"
    const val ALERTS = "alerts"
    const val DATA_MANAGEMENT = "data_management"
    const val ABOUT = "about"
    const val DISCLAIMER = "disclaimer"

    fun drugDetail(drugId: String) = "drug_detail/${drugId}"
    fun drugEdit(drugId: String) = "drug_edit/${drugId}"
    fun entryNew(type: String, uri: String? = null, drugId: String? = null): String {
        val params = mutableListOf<String>()
        if (!uri.isNullOrEmpty()) params += "uri=${android.net.Uri.encode(uri)}"
        if (!drugId.isNullOrEmpty()) params += "drugId=${android.net.Uri.encode(drugId)}"
        return if (params.isEmpty()) "entry_new/${type}" else "entry_new/${type}?${params.joinToString("&")}"
    }
    fun entryDetail(entryId: Long) = "entry_detail/${entryId}"
    fun entryEdit(entryId: Long) = "entry_edit/${entryId}"
    fun reportReader(fileName: String) =
        "report_reader/${android.net.Uri.encode(fileName)}"
    fun reportEdit(fileName: String) = "report_edit/${android.net.Uri.encode(fileName)}"
    fun prescriptionEdit(displayName: String) =
        "prescription_edit/${android.net.Uri.encode(displayName)}"
}

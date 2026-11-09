package com.fangshen.manual.ui.prescription

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.data.PrescriptionRepository
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.Prescription
import com.fangshen.manual.model.PrescriptionDrug
import com.fangshen.manual.model.parsePrescription
import com.fangshen.manual.model.prescriptionTemplate
import com.fangshen.manual.util.FileShare
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal

/** 列表卡片信息 */
data class PrescriptionSummary(
    val displayName: String,
    val name: String,
    val rows: List<RxRowResolved> = emptyList(),
    val effects: List<PrescriptionEffect> = emptyList(),
)

class PrescriptionListViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repo = PrescriptionRepository(application)

    private val _state = MutableStateFlow<List<PrescriptionSummary>>(emptyList())
    val items: StateFlow<List<PrescriptionSummary>> = _state.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val catalog = getApplication<FangShenApplication>().drugRepository.drugs.first()
            val list = repo.listFiles().map { file ->
                val parsed = runCatching {
                    parsePrescription(repo.read(repo.displayName(file)).orEmpty())
                }.getOrNull()
                val name = repo.displayName(file)
                val shot = parsed?.let { buildShotData(catalog, it) }
                PrescriptionSummary(
                    displayName = name,
                    name = parsed?.name?.ifEmpty { name } ?: name,
                    rows = shot?.rows ?: emptyList(),
                    effects = shot?.effects ?: emptyList(),
                )
            }
            _state.value = list
            _loading.value = false
        }
    }

    /** 新建处方：写入模板并返回可用于跳转编辑的显示名 */
    fun create(displayName: String, onCreated: (String) -> Unit) {
        val trimmed = displayName.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            var name = trimmed
            var n = 1
            while (repo.exists(name)) {
                n++
                name = "${trimmed}（${n}）"
            }
            repo.write(name, prescriptionTemplate(name))
            refresh()
            withContext(Dispatchers.Main) { onCreated(name) }
        }
    }

    fun delete(displayName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(displayName)
            refresh()
        }
    }

    /** 分享处方 .json */
    fun share(displayName: String) {
        val app = getApplication<FangShenApplication>()
        viewModelScope.launch(Dispatchers.IO) {
            val raw = repo.read(displayName) ?: return@launch
            withContext(Dispatchers.Main) {
                FileShare.shareJson(app, "${displayName}.json", raw, "分享处方")
            }
        }
    }

    /** 长截图用：读出处方并按当前目录解析成整页渲染数据（不跳转编辑页） */
    suspend fun renderForScreenshot(displayName: String): PrescriptionShotData? = withContext(Dispatchers.IO) {
        val raw = repo.read(displayName) ?: return@withContext null
        val parsed = runCatching { parsePrescription(raw) }.getOrNull() ?: return@withContext null
        val catalog = getApplication<FangShenApplication>().drugRepository.drugs.first()
        buildShotData(catalog, parsed)
    }

    companion object {
        fun provideFactory(app: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    PrescriptionListViewModel(app) as T
            }
    }
}

/** 编辑表单中的一行药物 */
data class DrugRow(
    val name: String = "",
    val dose: String = "",
)

data class PrescriptionForm(
    val name: String = "",
    val displayName: String = "",
    val drugs: List<DrugRow> = emptyList(),
    val indications: List<String> = emptyList(),
    val error: String? = null,
)

/** 处方分析：同时服用的效应 + 酶的诱导/抑制 + 相互作用警示 */
data class PrescriptionAnalysis(
    val effectLines: List<String> = emptyList(),
    val enzymeLines: List<String> = emptyList(),
    val warnings: List<String> = emptyList(),
    val unmatched: List<String> = emptyList(),
)

class PrescriptionEditViewModel(
    application: Application,
    val displayName: String,
) : AndroidViewModel(application) {

    private val repo = PrescriptionRepository(application)

    private val _form = MutableStateFlow(PrescriptionForm(displayName = displayName))
    val form: StateFlow<PrescriptionForm> = _form.asStateFlow()

    /** 估算效应基于目录中真实存在的药物 */
    val analysis: StateFlow<PrescriptionAnalysis> = combine(
        _form,
        (application as FangShenApplication).drugRepository.drugs,
    ) { f, catalog -> computeAnalysis(catalog, f) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, PrescriptionAnalysis())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val raw = repo.read(displayName) ?: return@launch
            runCatching { parsePrescription(raw) }.getOrNull()?.let { p ->
                _form.value = PrescriptionForm(
                    name = p.name,
                    displayName = displayName,
                    drugs = p.drugs.map { DrugRow(it.name, it.dose) },
                    indications = p.indications,
                )
            }
        }
    }

    fun update(transform: (PrescriptionForm) -> PrescriptionForm) {
        _form.value = transform(_form.value)
    }

    fun addDrug(name: String, dose: String) = update {
        it.copy(drugs = it.drugs + DrugRow(name, dose))
    }

    fun removeDrug(index: Int) = update { f ->
        f.copy(drugs = f.drugs.toMutableList().also { it.removeAt(index) })
    }

    fun removeIndication(text: String) = update {
        it.copy(indications = it.indications - text)
    }

    fun addIndication(text: String) = update { f ->
        if (text.isBlank() || f.indications.contains(text.trim())) f
        else f.copy(indications = f.indications + text.trim())
    }

    fun save(onDone: () -> Unit) {
        val f = _form.value
        if (f.name.isBlank()) {
            _form.value = f.copy(error = "请填写处方名称")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val json = JSONObject().apply {
                put("name", f.name.trim())
                put("drugs", JSONArray().apply {
                    f.drugs.filter { it.name.isNotBlank() }.forEach { row ->
                        put(JSONObject().apply {
                            put("name", row.name.trim())
                            put("dose", row.dose.trim())
                        })
                    }
                })
                put("indications", JSONArray().apply {
                    f.indications.forEach { put(it) }
                })
            }.toString(4)
            repo.write(displayName, json)
            withContext(Dispatchers.Main) { onDone() }
        }
    }

    companion object {
        fun provideFactory(app: Application, displayName: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    PrescriptionEditViewModel(app, displayName) as T
            }
    }
}

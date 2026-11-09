package com.fangshen.manual.ui.report

import android.app.Application
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.data.ReportRepository
import com.fangshen.manual.model.Report
import com.fangshen.manual.model.parseReport
import com.fangshen.manual.model.reportTemplate
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

/** 列表卡片所需的最小信息 */
data class ReportSummary(
    val fileName: String,        // 不含 .rp
    val name: String,            // head.name，缺省回退文件名
    val preface: String,
    val modifiedAt: Long,
)

/** 报告列表状态 */
data class ReportListUiState(
    val reports: List<ReportSummary> = emptyList(),
    val loading: Boolean = true,
)

class ReportListViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repo = ReportRepository(application)

    private val _uiState = MutableStateFlow(ReportListUiState())
    val uiState: StateFlow<ReportListUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val summaries = repo.listFiles().map { file ->
                val parsed = runCatching {
                    parseReport(repo.displayName(file).let { repo.read(it).orEmpty() }).head
                }.getOrNull()
                ReportSummary(
                    fileName = repo.displayName(file),
                    name = parsed?.name?.ifEmpty { repo.displayName(file) } ?: repo.displayName(file),
                    preface = parsed?.preface.orEmpty(),
                    modifiedAt = file.lastModified(),
                )
            }
            _uiState.value = ReportListUiState(reports = summaries, loading = false)
        }
    }

    /** 新建报告：以模板写入文件，返回可用于跳转编辑的文件名 */
    fun create(name: String, onCreated: (String) -> Unit) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            val displayName = ReportRepository.uniqueName(trimmed) { repo.exists(it) }
            val today = SimpleDateFormat("yyyy年M月d日", Locale.getDefault())
                .format(System.currentTimeMillis())
            repo.write(displayName, reportTemplate(trimmed, today))
            withContext(Dispatchers.Main) { onCreated(displayName) }
        }
    }

    fun delete(fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(fileName)
            refresh()
        }
    }

    /** 长截图用：解析整篇报告（不跳转阅读页） */
    suspend fun parseForScreenshot(fileName: String): Report? = withContext(Dispatchers.IO) {
        val raw = repo.read(fileName) ?: return@withContext null
        runCatching { parseReport(raw) }.getOrNull()
    }

    /** 分享单个 .rp 文件 */
    fun share(fileName: String) {
        val app = getApplication<FangShenApplication>()
        val file = repo.fileOf(fileName)
        if (!file.isFile) return
        val uri = FileProvider.getUriForFile(app, "${app.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TITLE, fileName)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        // Application 上下文启动 Activity 需要 NEW_TASK，否则会崩溃
        val chooser = Intent.createChooser(intent, "分享报告")
            .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        app.startActivity(chooser)
    }

    companion object {
        fun provideFactory(app: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ReportListViewModel(app) as T
            }
    }
}

/** 阅读页状态 */
sealed interface ReaderState {
    data object Loading : ReaderState
    data class Success(val report: Report, val raw: String) : ReaderState
    data class Error(val message: String, val raw: String?) : ReaderState
}

class ReportReaderViewModel(
    application: Application,
    private val fileName: String,
) : AndroidViewModel(application) {

    private val repo = ReportRepository(application)

    private val _state = MutableStateFlow<ReaderState>(ReaderState.Loading)
    val state: StateFlow<ReaderState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            val raw = repo.read(fileName)
            _state.value = if (raw == null) {
                ReaderState.Error("报告文件不存在", null)
            } else {
                runCatching { parseReport(raw) }
                    .map { ReaderState.Success(it, raw) }
                    .getOrElse { ReaderState.Error("报告解析失败：${it.message}", raw) }
            }
        }
    }

    companion object {
        fun provideFactory(app: Application, fileName: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ReportReaderViewModel(app, fileName) as T
            }
    }
}

/** 编辑表单中的一行角色 */
data class CharacterRow(
    val name: String = "",
    val note: String = "",
)

/** 编辑表单中的一段正文 */
data class EpisodeRow(
    val time: String = "",
    val scene: String = "",
    val text: String = "",
)

/** 编辑页表单：结构化编辑 .rp，与药物编辑页风格一致 */
data class ReportEditForm(
    val name: String = "",
    val date: String = "",
    val location: String = "",
    val characters: List<CharacterRow> = emptyList(),
    val preface: String = "",
    val conclusion: String = "",
    val episodes: List<EpisodeRow> = emptyList(),
    val error: String? = null,
)

class ReportEditViewModel(
    application: Application,
    val fileName: String,
) : AndroidViewModel(application) {

    private val repo = ReportRepository(application)

    private val _form = MutableStateFlow(ReportEditForm())
    val form: StateFlow<ReportEditForm> = _form.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val raw = repo.read(fileName) ?: return@launch
            runCatching { parseReport(raw) }.getOrNull()?.let { r ->
                _form.value = ReportEditForm(
                    name = r.head.name,
                    date = r.head.date,
                    location = r.head.location,
                    characters = r.head.characters.map { CharacterRow(it.name, it.note.orEmpty()) },
                    preface = r.head.preface,
                    conclusion = r.head.conclusion,
                    episodes = r.body.map { EpisodeRow(it.time, it.scene, it.text) },
                )
            }
        }
    }

    fun update(transform: (ReportEditForm) -> ReportEditForm) {
        _form.value = transform(_form.value)
    }

    fun addCharacter() = update { it.copy(characters = it.characters + CharacterRow()) }

    fun removeCharacter(index: Int) = update { f ->
        f.copy(characters = f.characters.toMutableList().also { list -> list.removeAt(index) })
    }

    fun addEpisode() = update { it.copy(episodes = it.episodes + EpisodeRow()) }

    fun removeEpisode(index: Int) = update { f ->
        f.copy(episodes = f.episodes.toMutableList().also { list -> list.removeAt(index) })
    }

    /** 保存：把表单序列化为 .rp JSON */
    fun save(onDone: () -> Unit) {
        val f = _form.value
        if (f.name.isBlank()) {
            _form.value = f.copy(error = "请填写报告名称")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val json = JSONObject().apply {
                put("head", JSONObject().apply {
                    put("name", f.name.trim())
                    put("date", f.date.trim())
                    put("location", f.location.trim())
                    put("character", JSONArray().apply {
                        f.characters.filter { it.name.isNotBlank() }.forEach { c ->
                            put(JSONObject().apply {
                                put("name", c.name.trim())
                                put("note", c.note.trim())
                            })
                        }
                    })
                    put("preface", f.preface.trim())
                    put("conclusion", f.conclusion.trim())
                })
                put("body", JSONArray().apply {
                    f.episodes.forEach { e ->
                        put(JSONObject().apply {
                            put("time", e.time.trim())
                            put("scene", e.scene.trim())
                            put("text", e.text.trim())
                        })
                    }
                })
            }.toString(4)
            repo.write(fileName, json)
            withContext(Dispatchers.Main) { onDone() }
        }
    }

    companion object {
        fun provideFactory(app: Application, fileName: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ReportEditViewModel(app, fileName) as T
            }
    }
}

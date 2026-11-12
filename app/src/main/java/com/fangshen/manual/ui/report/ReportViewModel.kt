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
import com.fangshen.manual.model.flatFragments
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

/** 编辑表单中的一段正文片段 */
sealed interface FragmentRow {
    data class SceneRow(
        val time: String = "",
        val place: String = "",
        val description: String = "",
    ) : FragmentRow

    data class DialogRow(
        val items: List<DialogItemRow> = emptyList(),
    ) : FragmentRow
}

sealed interface DialogItemRow {
    data class SayRow(val name: String = "", val content: String = "") : DialogItemRow
    data class EventRow(val content: String = "") : DialogItemRow
}

/** 读取到的片段转成编辑行（旧 body 自动升级为场景片段） */
private fun List<com.fangshen.manual.model.ReportFragment>.toRows(): List<FragmentRow> = map { frag ->
    when (frag) {
        is com.fangshen.manual.model.ReportFragment.Scene -> FragmentRow.SceneRow(frag.time, frag.place, frag.description)
        is com.fangshen.manual.model.ReportFragment.Dialog -> FragmentRow.DialogRow(
            frag.items.map { item ->
                when (item) {
                    is com.fangshen.manual.model.DialogItem.Say -> DialogItemRow.SayRow(item.name, item.content)
                    is com.fangshen.manual.model.DialogItem.Event -> DialogItemRow.EventRow(item.content)
                }
            },
        )
    }
}
/** 编辑页表单：结构化编辑 .rp，与药物编辑页风格一致 */
data class ReportEditForm(
    val name: String = "",
    val date: String = "",
    val location: String = "",
    val characters: List<CharacterRow> = emptyList(),
    val preface: String = "",
    val conclusion: String = "",
    val fragments: List<FragmentRow> = emptyList(),
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
                    fragments = r.flatFragments().toRows(),
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

    fun addSceneFragment() = update { it.copy(fragments = it.fragments + FragmentRow.SceneRow()) }

    fun addDialogFragment() = update { it.copy(fragments = it.fragments + FragmentRow.DialogRow()) }

    fun removeFragment(index: Int) = update { f ->
        f.copy(fragments = f.fragments.toMutableList().also { list -> list.removeAt(index) })
    }

    fun updateFragment(index: Int, row: FragmentRow) = update { f ->
        f.copy(fragments = f.fragments.toMutableList().also { list -> list[index] = row })
    }

    fun addSay(fragmentIndex: Int, defaultName: String) = update { f ->
        val frags = f.fragments.toMutableList()
        val dialog = frags[fragmentIndex] as FragmentRow.DialogRow
        frags[fragmentIndex] = dialog.copy(items = dialog.items + DialogItemRow.SayRow(name = defaultName))
        f.copy(fragments = frags)
    }

    fun addEvent(fragmentIndex: Int) = update { f ->
        val frags = f.fragments.toMutableList()
        val dialog = frags[fragmentIndex] as FragmentRow.DialogRow
        frags[fragmentIndex] = dialog.copy(items = dialog.items + DialogItemRow.EventRow())
        f.copy(fragments = frags)
    }

    fun updateItem(fragmentIndex: Int, itemIndex: Int, item: DialogItemRow) = update { f ->
        val frags = f.fragments.toMutableList()
        val dialog = frags[fragmentIndex] as FragmentRow.DialogRow
        frags[fragmentIndex] = dialog.copy(items = dialog.items.toMutableList().also { it[itemIndex] = item })
        f.copy(fragments = frags)
    }

    fun removeItem(fragmentIndex: Int, itemIndex: Int) = update { f ->
        val frags = f.fragments.toMutableList()
        val dialog = frags[fragmentIndex] as FragmentRow.DialogRow
        frags[fragmentIndex] = dialog.copy(items = dialog.items.toMutableList().also { it.removeAt(itemIndex) })
        f.copy(fragments = frags)
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
                put("fragments", com.fangshen.manual.model.fragmentsToJson(f.fragments.map { row ->
                    when (row) {
                        is FragmentRow.SceneRow -> com.fangshen.manual.model.ReportFragment.Scene(
                            row.time.trim(), row.place.trim(), row.description.trim(),
                        )
                        is FragmentRow.DialogRow -> com.fangshen.manual.model.ReportFragment.Dialog(
                            row.items.map { item ->
                                when (item) {
                                    is DialogItemRow.SayRow -> com.fangshen.manual.model.DialogItem.Say(
                                        item.name.trim(), item.content.trim(),
                                    )
                                    is DialogItemRow.EventRow -> com.fangshen.manual.model.DialogItem.Event(item.content.trim())
                                }
                            },
                        )
                    }
                }))
                // 兼容旧阅读器：场景片段同时写旧 body，对话片段不丢失（新阅读器优先 fragments）
                put("body", JSONArray().apply {
                    f.fragments.forEach { row ->
                        if (row is FragmentRow.SceneRow) {
                            put(JSONObject().apply {
                                put("time", row.time.trim())
                                put("scene", row.place.trim())
                                put("text", row.description.trim())
                            })
                        }
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

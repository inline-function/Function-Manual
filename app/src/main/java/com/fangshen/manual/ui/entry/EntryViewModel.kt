package com.fangshen.manual.ui.entry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.alarm.AlarmScheduler
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.engine.DrugDoseInfo
import com.fangshen.manual.engine.EffectEngine
import com.fangshen.manual.engine.InteractionEngine
import com.fangshen.manual.model.DrugInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EntryDetailViewModel(
    application: Application,
    private val entryId: Long,
    entries: kotlinx.coroutines.flow.Flow<List<EntryEntity>>,
) : AndroidViewModel(application) {

    val uiState = entries
        .map { list -> list.firstOrNull { it.id == entryId } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = null,
        )

    fun delete(onDone: () -> Unit) {
        val entry = uiState.value ?: return
        viewModelScope.launch {
            val app = getApplication<FangShenApplication>()
            app.entryRepository.delete(entry)
            if (entry.type == "SCHEDULE" && entry.notify) {
                AlarmScheduler.cancel(app, entry.id)
            }
            onDone()
        }
    }

    companion object {
        fun provideFactory(app: Application, entryId: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val a = app as FangShenApplication
                    return EntryDetailViewModel(app, entryId, a.entryRepository.entries) as T
                }
                }
    }
}

data class EntryEditForm(
    val title: String = "",
    val content: String = "",
    val eventTime: Long = System.currentTimeMillis(),
    val notify: Boolean = false,
    val attachmentUri: String? = null,
    val durationMs: Long? = null,
    val error: String? = null,
)

class EntryEditViewModel(
    application: Application,
    initialType: String,
    initialUri: String?,
    private val initialEntryId: Long,
    entries: kotlinx.coroutines.flow.Flow<List<EntryEntity>>,
) : AndroidViewModel(application) {

    var type: String = initialType
        private set

    var form = MutableStateFlow(
        EntryEditForm(attachmentUri = initialUri, notify = initialType == "SCHEDULE"),
    )
        private set

    init {
        if (initialEntryId != 0L) {
            viewModelScope.launch {
                val app = getApplication<FangShenApplication>()
                app.entryRepository.getById(initialEntryId)?.let { entry ->
                    type = entry.type
                    form.value = EntryEditForm(
                        title = entry.title,
                        content = entry.content ?: "",
                        eventTime = entry.eventTime,
                        notify = entry.notify,
                        attachmentUri = entry.attachmentUri,
                        durationMs = entry.durationMs,
                    )
                }
            }
        }
    }

    fun update(transform: (EntryEditForm) -> EntryEditForm) {
        form.value = transform(form.value)
    }

    fun save(title: String, onDone: () -> Unit) {
        val f = form.value
        if (title.isBlank() && f.attachmentUri == null) {
            update { it.copy(error = "请填写标题") }
            return
        }
        viewModelScope.launch {
            val app = getApplication<FangShenApplication>()
            val now = System.currentTimeMillis()
            if (initialEntryId == 0L) {
                val entry = EntryEntity(
                    type = type,
                    title = title.ifBlank { defaultTitle() },
                    content = f.content.ifBlank { null },
                    drugId = null,
                    doseAmount = null,
                    doseUnit = null,
                    route = null,
                    attachmentUri = f.attachmentUri,
                    durationMs = f.durationMs,
                    eventTime = f.eventTime,
                    notify = f.notify,
                    createdAt = now,
                    updatedAt = now,
                )
                val id = app.entryRepository.insert(entry)
                if (type == "SCHEDULE" && f.notify) {
                    AlarmScheduler.schedule(app, entry.copy(id = id))
                }
            } else {
                val existing = app.entryRepository.getById(initialEntryId) ?: return@launch
                val updated = existing.copy(
                    title = title.ifBlank { defaultTitle() },
                    content = f.content.ifBlank { null },
                    attachmentUri = f.attachmentUri,
                    durationMs = f.durationMs,
                    eventTime = f.eventTime,
                    notify = f.notify,
                    updatedAt = now,
                )
                app.entryRepository.update(updated)
                if (type == "SCHEDULE") {
                    if (updated.notify) AlarmScheduler.schedule(app, updated)
                    else AlarmScheduler.cancel(app, updated.id)
                }
            }
            onDone()
        }
    }

    private fun defaultTitle(): String = when (type) {
        "NOTE" -> "笔记"
        "PHOTO" -> "照片"
        "VOICE" -> "语音"
        "FILE" -> "文件"
        "SCHEDULE" -> "日程"
        else -> "条目"
    }

    fun setDuration(ms: Long) {
        update { it.copy(durationMs = ms) }
    }

    companion object {
        fun provideFactory(
            app: Application,
            type: String,
            uri: String?,
            entryId: Long,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val a = app as FangShenApplication
                return EntryEditViewModel(app, type, uri, entryId, a.entryRepository.entries) as T
            }
            }
    }
}

class DoseRecordViewModel(
    application: Application,
    val initialDrugId: String?,
    drugs: kotlinx.coroutines.flow.Flow<List<DrugInfo>>,
    entries: kotlinx.coroutines.flow.Flow<List<EntryEntity>>,
) : AndroidViewModel(application) {

    data class UiState(
        val drugs: List<DrugInfo> = emptyList(),
        val selectedDrugId: String? = null,
        val amountText: String = "",
        val routeName: String = "ORAL",
        val eventTime: Long = System.currentTimeMillis(),
        val warnings: List<InteractionEngine.Warning> = emptyList(),
        val error: String? = null,
    )

    private val selectedDrugId = MutableStateFlow(initialDrugId)
    private val amountText = MutableStateFlow("")
    private val routeName = MutableStateFlow("ORAL")
    private val eventTime = MutableStateFlow(System.currentTimeMillis())
    private val error = MutableStateFlow<String?>(null)

    val uiState = combine(
        combine(drugs, entries) { drugList, entryList -> drugList to entryList },
        selectedDrugId,
        amountText,
        routeName,
        eventTime,
    ) { (drugList, entryList), drugId, amount, route, time ->
        val now = System.currentTimeMillis()
        val doses = entryList.filter {
            it.type == "DOSE" && it.doseAmount != null && it.drugId != null
        }.mapNotNull { entry ->
            val drug = drugList.firstOrNull { it.id == entry.drugId } ?: return@mapNotNull null
            DrugDoseInfo(drug, entry.doseAmount!!.toBigDecimal(), entry.eventTime)
        }
        val activeOther = EffectEngine.compute(doses, now).activeDrugs
            .filter { it.id != drugId }
        val newDrug = drugList.firstOrNull { it.id == drugId }
        val warnings = if (newDrug != null) {
            InteractionEngine.check((activeOther + newDrug).distinctBy { it.id })
        } else emptyList()
        UiState(
            drugs = drugList,
            selectedDrugId = drugId,
            amountText = amount,
            routeName = route,
            eventTime = time,
            warnings = warnings,
            error = error.value,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = UiState(),
    )

    fun selectDrug(id: String) { selectedDrugId.value = id }
    fun setAmount(text: String) { amountText.value = text }
    fun setRoute(name: String) { routeName.value = name }
    fun setTime(millis: Long) { eventTime.value = millis }

    fun save(onDone: () -> Unit) {
        val state = uiState.value
        val drug = state.drugs.firstOrNull { it.id == state.selectedDrugId }
        val amount = state.amountText.trim().toBigDecimalOrNull()
        when {
            drug == null -> { error.value = "请选择药物"; return }
            amount == null || amount <= java.math.BigDecimal.ZERO ->
                { error.value = "请输入大于 0 的剂量"; return }
            else -> {
                viewModelScope.launch {
                    val app = getApplication<FangShenApplication>()
                    val now = System.currentTimeMillis()
                    val entry = EntryEntity(
                        type = "DOSE",
                        title = drug.name,
                        content = null,
                        drugId = drug.id,
                        doseAmount = amount.stripTrailingZeros().toPlainString(),
                        doseUnit = drug.doseUnit.name,
                        route = state.routeName,
                        attachmentUri = null,
                        durationMs = null,
                        eventTime = state.eventTime,
                        notify = false,
                        createdAt = now,
                        updatedAt = now,
                    )
                    app.entryRepository.insert(entry)
                    onDone()
                }
            }
        }
    }

    companion object {
        fun provideFactory(app: Application, drugId: String?): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val a = app as FangShenApplication
                    return DoseRecordViewModel(
                        app, drugId, a.drugRepository.drugs, a.entryRepository.entries,
                    ) as T
                }
                }
    }
}

class ScheduleListViewModel(
    application: Application,
    entries: kotlinx.coroutines.flow.Flow<List<EntryEntity>>,
) : AndroidViewModel(application) {

    val uiState = entries
        .map { list -> list.filter { it.type == "SCHEDULE" }.sortedBy { it.eventTime } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList(),
        )

    fun delete(entry: EntryEntity, onDone: () -> Unit) {
        viewModelScope.launch {
            val app = getApplication<FangShenApplication>()
            app.entryRepository.delete(entry)
            if (entry.notify) AlarmScheduler.cancel(app, entry.id)
            onDone()
        }
    }

    companion object {
        fun provideFactory(app: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val a = app as FangShenApplication
                return ScheduleListViewModel(app, a.entryRepository.entries) as T
            }
            }
    }
}

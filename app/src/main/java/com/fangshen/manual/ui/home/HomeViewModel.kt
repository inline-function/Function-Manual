package com.fangshen.manual.ui.home

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.model.DoseUnit
import java.math.BigDecimal
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.engine.DrugDoseInfo
import com.fangshen.manual.engine.EffectEngine
import com.fangshen.manual.engine.InteractionEngine
import com.fangshen.manual.model.AlertRule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val entries: List<EntryEntity> = emptyList(),
    val drugs: List<DrugInfo> = emptyList(),
    val chartUnit: DoseUnit = DoseUnit.MG,
    val effectResult: EffectEngine.Result = EffectEngine.Result(emptyList(), emptyList()),
    val interactions: List<InteractionEngine.Warning> = emptyList(),
    val now: Long = System.currentTimeMillis(),
) {
    val activeEffects: List<EffectEngine.EffectGroup> get() = effectResult.groups
}

/**
 * 服药抽屉里的一条待服用项目。
 * - [tablets] > 0：按片计数（点击药物卡片服用一片）。
 * - [customAmount] != null：长按弹出的对话框里手动输入的剂量。
 */
data class QueueItem(
    val id: Long,
    val drugId: String,
    val tablets: Int = 0,
    val customAmount: BigDecimal? = null,
)

class HomeViewModel(
    application: Application,
    entries: kotlinx.coroutines.flow.Flow<List<EntryEntity>>,
    drugs: kotlinx.coroutines.flow.Flow<List<DrugInfo>>,
    alertRules: kotlinx.coroutines.flow.Flow<List<AlertRule>>,
    profileFlags: kotlinx.coroutines.flow.Flow<Set<String>>,
) : AndroidViewModel(application) {

    private val chartUnit = MutableStateFlow(DoseUnit.MG)

    /** 服药抽屉中的待服用列表（仅存在于内存，确认服用前不会写入台账） */
    val doseQueue: SnapshotStateList<QueueItem> = mutableStateListOf()

    private var queueIdSeq = 0L

    val uiState = combine(entries, drugs, chartUnit, alertRules, profileFlags) { entryList, drugList, unit, rules, flags ->
        val now = System.currentTimeMillis()
        val doses = entryList.filter {
            it.type == "DOSE" && it.doseAmount != null && it.drugId != null && it.eventTime <= now
        }.mapNotNull { entry ->
            val drug = drugList.firstOrNull { it.id == entry.drugId } ?: return@mapNotNull null
            DrugDoseInfo(drug, entry.doseAmount!!.toBigDecimal(), entry.eventTime)
        }
        val result = EffectEngine.compute(doses, now, alertRules = rules, profileFlags = flags)
        val warnings = InteractionEngine.check(result.activeDrugs)
        HomeUiState(
            entries = entryList,
            drugs = drugList,
            chartUnit = unit,
            effectResult = result,
            interactions = warnings,
            now = now,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState(),
    )

    fun setChartUnit(unit: DoseUnit) {
        chartUnit.value = unit
    }

    /** 点击药物卡片：服用一片（无单片剂量的药物请改用 [addCustomDose]） */
    fun takeTablet(drug: DrugInfo) {
        val existing = doseQueue.firstOrNull { it.drugId == drug.id && it.customAmount == null }
        if (existing != null) {
            doseQueue[doseQueue.indexOf(existing)] = existing.copy(tablets = existing.tablets + 1)
        } else {
            doseQueue.add(QueueItem(id = ++queueIdSeq, drugId = drug.id, tablets = 1))
        }
    }

    /** 长按药物卡片（或无单片剂量的药物短按）：手动输入剂量加入待服用列表 */
    fun addCustomDose(drug: DrugInfo, amount: BigDecimal) {
        if (amount <= BigDecimal.ZERO) return
        doseQueue.add(QueueItem(id = ++queueIdSeq, drugId = drug.id, customAmount = amount))
    }

    fun removeQueueItem(item: QueueItem) {
        doseQueue.remove(item)
    }

    fun clearQueue() {
        doseQueue.clear()
    }

    /** 确认服用：把待服用列表写入台账并清空 */
    fun confirmQueue(onDone: () -> Unit) {
        if (doseQueue.isEmpty()) return
        viewModelScope.launch {
            val app = getApplication<FangShenApplication>()
            val drugList = uiState.value.drugs
            val now = System.currentTimeMillis()
            doseQueue.forEach { item ->
                val drug = drugList.firstOrNull { it.id == item.drugId } ?: return@forEach
                val per = drug.tabletDose
                val amount: BigDecimal? = item.customAmount
                    ?: (if (per != null && per > BigDecimal.ZERO && item.tablets > 0)
                        per.multiply(item.tablets.toBigDecimal())
                    else null)
                if (amount == null) return@forEach
                app.entryRepository.insert(
                    EntryEntity(
                        type = "DOSE",
                        title = drug.name,
                        content = item.customAmount?.let { "手动输入剂量" },
                        drugId = drug.id,
                        doseAmount = amount.stripTrailingZeros().toPlainString(),
                        doseUnit = drug.doseUnit.name,
                        route = drug.route.name,
                        attachmentUri = null,
                        durationMs = null,
                        eventTime = now,
                        notify = false,
                        createdAt = now,
                        updatedAt = now,
                    ),
                )
            }
            doseQueue.clear()
            onDone()
        }
    }

    /** 删除一条用药记录 */
    fun deleteEntry(entry: EntryEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            val app = getApplication<FangShenApplication>()
            app.entryRepository.delete(entry)
            if (entry.type == "SCHEDULE" && entry.notify) {
                com.fangshen.manual.alarm.AlarmScheduler.cancel(app, entry.id)
            }
            onDone()
        }
    }

    companion object {
        fun provideFactory(app: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val a = app as FangShenApplication
                return HomeViewModel(
                    application = app,
                    entries = a.entryRepository.entries,
                    drugs = a.drugRepository.drugs,
                    alertRules = a.alertRepository.rules,
                    profileFlags = a.profileRepository.disorderFlags,
                ) as T
            }
        }
    }
}

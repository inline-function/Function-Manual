package com.fangshen.manual.data

import com.fangshen.manual.data.entity.CustomDrugEntity
import com.fangshen.manual.data.entity.CustomDrugNeuroEntity
import com.fangshen.manual.data.entity.CustomDrugRuleEntity
import com.fangshen.manual.data.entity.EntryEntity
import com.fangshen.manual.data.entity.ProfileEntity
import com.fangshen.manual.model.AlertRule
import com.fangshen.manual.model.DrugCatalog
import com.fangshen.manual.model.DrugInfo
import com.fangshen.manual.model.toDrugInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * 药物仓库：合并用户自定义药物（JSON 存储）与内置药物（领域知识目录）。
 */
class DrugRepository(
    private val store: JsonDataStore,
) {
    /** 全部药物：自定义药物在前，内置药物按目录顺序随后 */
    val drugs: Flow<List<DrugInfo>> = store.state.map { data ->
        val custom = data.drugs.map { dj ->
            dj.entity.toDrugInfo(
                rules = dj.rules,
                neuros = dj.neuros,
            )
        }
        custom + DrugCatalog.all
    }

    suspend fun customEntity(id: Long): CustomDrugEntity? =
        store.snapshot().drugs.firstOrNull { it.entity.id == id }?.entity

    suspend fun saveDrug(
        entity: CustomDrugEntity,
        rules: List<CustomDrugRuleEntity>,
        neuros: List<CustomDrugNeuroEntity>,
    ): Long = store.write {
        val id = if (entity.id == 0L) nextDrugId(it) else entity.id
        val others = it.drugs.filter { d -> d.entity.id != id }
        val drugJson = DrugJson(
            entity = entity.copy(id = id),
            rules = rules.map { r -> r.copy(drugId = id, id = 0L) },
            neuros = neuros.map { n -> n.copy(drugId = id, id = 0L) },
        )
        it.copy(drugs = others + drugJson) to id
    }

    suspend fun deleteDrug(entity: CustomDrugEntity) = store.write {
        it.copy(drugs = it.drugs.filter { d -> d.entity.id != entity.id }) to Unit
    }

    suspend fun loadRules(id: Long): List<CustomDrugRuleEntity> =
        store.snapshot().drugs.firstOrNull { it.entity.id == id }?.rules ?: emptyList()

    suspend fun loadNeuros(id: Long): List<CustomDrugNeuroEntity> =
        store.snapshot().drugs.firstOrNull { it.entity.id == id }?.neuros ?: emptyList()

    private fun nextDrugId(data: JsonData): Long =
        (data.drugs.maxOfOrNull { it.entity.id } ?: 0L) + 1L
}

class EntryRepository(private val store: JsonDataStore) {

    val entries: Flow<List<EntryEntity>> = store.state.map { data ->
        data.entries.sortedByDescending { e -> e.eventTime }
    }

    suspend fun getById(id: Long): EntryEntity? =
        store.snapshot().entries.firstOrNull { it.id == id }

    suspend fun insert(entry: EntryEntity): Long = store.write {
        val id = if (entry.id == 0L) (it.entries.maxOfOrNull { e -> e.id } ?: 0L) + 1L else entry.id
        it.copy(entries = it.entries + entry.copy(id = id)) to id
    }

    suspend fun update(entry: EntryEntity) = store.write {
        it.copy(entries = it.entries.map { e -> if (e.id == entry.id) entry else e }) to Unit
    }

    suspend fun delete(entry: EntryEntity) = store.write {
        it.copy(entries = it.entries.filter { e -> e.id != entry.id }) to Unit
    }

    suspend fun deleteAll() = store.write {
        it.copy(entries = emptyList()) to Unit
    }

    suspend fun upcomingSchedules(from: Long): List<EntryEntity> =
        store.snapshot().entries.filter {
            it.type == "SCHEDULE" && it.notify && it.eventTime > from
        }
}

class ProfileRepository(private val store: JsonDataStore) {

    val values: Flow<Map<String, String>> = store.state.map { data ->
        data.profile.associate { e -> e.key to e.valueText }
    }

    /** 当前画像命中的类别与具体条目名集合（供预警规则匹配） */
    val disorderFlags: Flow<Set<String>> = values.map { map ->
        com.fangshen.manual.ui.profile.disorderFlags(map["disorders"])
    }

    suspend fun put(key: String, value: String) = store.write {
        val others = it.profile.filter { e -> e.key != key }
        it.copy(profile = others + ProfileEntity(key, value, System.currentTimeMillis())) to Unit
    }

    suspend fun clear() = store.write {
        it.copy(profile = emptyList()) to Unit
    }
}

/** 预警规则仓库：联用/画像规则的增删改查 */
class AlertRepository(private val store: JsonDataStore) {

    val rules: Flow<List<AlertRule>> = store.state.map { data ->
        data.alerts.filter { it.name.isNotEmpty() }.sortedByDescending { it.id }
    }

    suspend fun add(name: String, drug: String, withDrug: String?, withProfile: String?, effect: String, weight: Int) = store.write {
        val id = (it.alerts.maxOfOrNull { r -> r.id } ?: 0L) + 1L
        val rule = AlertRule(
            id = id, name = name, enabled = true,
            drug = drug, withDrug = withDrug, withProfile = withProfile,
            effect = effect, weight = weight,
        )
        it.copy(alerts = it.alerts + rule) to id
    }

    suspend fun update(rule: AlertRule) = store.write {
        val others = it.alerts.filter { r -> r.id != rule.id }
        it.copy(alerts = others + rule) to Unit
    }

    suspend fun toggle(rule: AlertRule) = store.write {
        val updated = rule.copy(enabled = !rule.enabled)
        it.copy(alerts = it.alerts.map { r -> if (r.id == rule.id) updated else r }) to Unit
    }

    suspend fun delete(rule: AlertRule) = store.write {
        it.copy(alerts = it.alerts.filter { r -> r.id != rule.id }) to Unit
    }
}

/**
 * 串行写：同一时刻只有一个写事务落盘，[block] 返回 (新数据, 结果)。
 */
private suspend fun <T> JsonDataStore.write(
    block: (JsonData) -> Pair<JsonData, T>,
): T = STORE_LOCK.withLock {
    val (data, result) = block(snapshot())
    save(data)
    result
}

private val STORE_LOCK = ReentrantLock()

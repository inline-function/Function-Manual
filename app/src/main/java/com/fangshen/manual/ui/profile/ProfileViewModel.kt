package com.fangshen.manual.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fangshen.manual.FangShenApplication
import com.fangshen.manual.data.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * 个人画像「精神障碍统计」的 11 个类别（DSM-5 分组顺序），
 * 每个类别细分为该组下的具体障碍条目。
 */
val DISORDER_ITEMS: List<Pair<String, List<String>>> = listOf(
    "神经发育障碍" to listOf(
        "智力障碍",
        "交流障碍",
        "孤独症谱系障碍",
        "注意缺陷/多动障碍",
        "特定学习障碍",
        "发育性协调障碍",
        "抽动障碍",
        "抽动秽语综合征",
    ),
    "精神分裂障碍" to listOf(
        "精神分裂症",
        "分裂样障碍",
        "分裂情感性障碍",
        "妄想性障碍",
        "短暂精神病性障碍",
        "精神病性障碍（未特定）",
    ),
    "情感障碍" to listOf(
        "双相I型障碍",
        "双相II型障碍",
        "环性心境障碍",
        "破坏性心境失调障碍",
        "重度抑郁障碍",
        "持续性抑郁障碍（心境恶劣）",
        "经前期烦躁障碍",
    ),
    "焦虑障碍" to listOf(
        "分离焦虑障碍",
        "选择性缄默症",
        "特定恐惧症",
        "社交焦虑障碍",
        "惊恐障碍",
        "广场恐惧症",
        "广泛性焦虑障碍",
    ),
    "强迫障碍" to listOf(
        "强迫障碍",
        "躯体变形障碍",
        "囤积障碍",
        "拔毛癖",
        "抓痕（皮肤搔抓）障碍",
    ),
    "创伤与应激障碍" to listOf(
        "反应性依恋障碍",
        "去抑制性社会参与障碍",
        "创伤后应激障碍",
        "急性应激障碍",
        "适应障碍",
    ),
    "解离障碍" to listOf(
        "分离性身份障碍",
        "分离性遗忘症",
        "人格解体/现实解体障碍",
    ),
    "躯体障碍" to listOf(
        "躯体症状障碍",
        "疾病焦虑障碍",
        "转换障碍（功能性神经症状障碍）",
        "做作性障碍",
        "心理因素影响其他医学状况",
    ),
    "进食障碍" to listOf(
        "神经性厌食",
        "神经性贪食",
        "暴食障碍",
        "回避/限制性摄食障碍",
        "异食癖",
        "反刍障碍",
    ),
    "睡眠障碍" to listOf(
        "失眠障碍",
        "嗜睡障碍",
        "发作性睡病",
        "阻塞性睡眠呼吸暂停",
        "昼夜节律睡眠-觉醒障碍",
        "睡行症",
        "夜惊障碍",
        "梦魇障碍",
        "快速眼动睡眠行为障碍",
        "不安腿综合征",
    ),
    "人格障碍" to listOf(
        "偏执型人格障碍",
        "分裂样人格障碍",
        "分裂型人格障碍",
        "反社会型人格障碍",
        "边缘型人格障碍",
        "表演型人格障碍",
        "自恋型人格障碍",
        "回避型人格障碍",
        "依赖型人格障碍",
        "强迫型人格障碍",
        "其他特定/未特定人格障碍",
    ),
)

/** 11 个类别名（顺序与 [DISORDER_ITEMS] 一致） */
val DISORDER_CATEGORIES: List<String> = DISORDER_ITEMS.map { it.first }

/** 预定义条目集合：类别 → 该类别下的具体障碍名 */
private val PREDEFINED: Map<String, Set<String>> =
    DISORDER_ITEMS.associate { (category, items) -> category to items.toSet() }

/** 一条障碍记录：名称 + 备注 + 记录时间 */
data class DisorderRecord(val name: String, val note: String, val time: Long)

/**
 * 某个类别的统计状态：
 * [items] 为细分的具体障碍条目，[selected] 为其中已点选的，[custom] 为自行添加的记录。
 */
data class DisorderCategory(
    val name: String,
    val items: List<String>,
    val selected: Set<String>,
    val custom: List<DisorderRecord>,
) {
    /** 该类别下的记录总数（已选细分条目 + 自定义记录） */
    val count: Int get() = selected.size + custom.size
}

/** 个人画像界面的状态：11 个类别各自的细分统计 */
data class ProfileUiState(
    val groups: List<DisorderCategory> = DISORDER_ITEMS.map { (category, items) ->
        DisorderCategory(category, items, emptySet(), emptyList())
    },
) {
    /** 已统计的障碍总数 */
    val total: Int get() = groups.sumOf { it.count }

    /** 有记录的类别数 */
    val usedCategories: Int get() = groups.count { it.count > 0 }
}

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: ProfileRepository = getApplication<FangShenApplication>().profileRepository

    /** 障碍记录存放在 profile 表的 "disorders" 键下：{类别: [{name, note, time}]} */
    val uiState: StateFlow<ProfileUiState> = repo.values.map { map ->
        val root = runCatching { JSONObject(map[KEY_DISORDERS] ?: "{}") }.getOrDefault(JSONObject())
        ProfileUiState(
            groups = DISORDER_ITEMS.map { (category, items) ->
                val records = parseRecords(root.optJSONArray(category))
                val predefined = PREDEFINED[category].orEmpty()
                DisorderCategory(
                    name = category,
                    items = items,
                    selected = records.mapNotNull { r -> r.name.takeIf { it in predefined } }.toSet(),
                    custom = records.filter { it.name !in predefined },
                )
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState())

    /** 点选/取消某个细分条目 */
    fun toggleItem(category: String, name: String) {
        viewModelScope.launch {
            mutate(category) { records ->
                if (records.any { it.name == name }) {
                    records.filterNot { it.name == name }
                } else {
                    records + DisorderRecord(name, "", System.currentTimeMillis())
                }
            }
        }
    }

    /** 在某类别下添加一条自定义记录 */
    fun addRecord(category: String, name: String, note: String) {
        viewModelScope.launch {
            mutate(category) { records -> records + DisorderRecord(name.trim(), note.trim(), System.currentTimeMillis()) }
        }
    }

    /** 删除某类别下的一条自定义记录 */
    fun deleteRecord(category: String, record: DisorderRecord) {
        viewModelScope.launch {
            mutate(category) { records -> records.filterNot { it == record } }
        }
    }

    /** 读出当前类别记录 → 修改 → 写回（保留其它类别的数据） */
    private suspend fun mutate(
        category: String,
        block: (List<DisorderRecord>) -> List<DisorderRecord>,
    ) = withContext(Dispatchers.IO) {
        val map = repo.values.first()
        val root = runCatching { JSONObject(map[KEY_DISORDERS] ?: "{}") }.getOrDefault(JSONObject())
        val current = parseRecords(root.optJSONArray(category))
        val updated = block(current)
        if (updated.isEmpty()) {
            root.remove(category)
        } else {
            root.put(category, JSONArray(updated.map { it.toJSONObject() }))
        }
        repo.put(KEY_DISORDERS, root.toString())
    }

    companion object {
        private const val KEY_DISORDERS = "disorders"

        fun provideFactory(app: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = ProfileViewModel(app) as T
        }
    }
}

/** 从 disorders JSON 计算画像标志集合：有记录的类别名 + 已选的预定义条目名 */
fun disorderFlags(valueText: String?): Set<String> {
    if (valueText.isNullOrBlank()) return emptySet()
    val root = runCatching { JSONObject(valueText) }.getOrNull() ?: return emptySet()
    val flags = mutableSetOf<String>()
    for ((category, items) in DISORDER_ITEMS) {
        val records = parseRecords(root.optJSONArray(category))
        if (records.isEmpty()) continue
        flags += category
        val predefined = items.toSet()
        flags += records.mapNotNull { it.name }.filter { it in predefined }
    }
    return flags
}

fun parseRecords(arr: JSONArray?): List<DisorderRecord> =
    if (arr == null) emptyList()
    else List(arr.length()) { i ->
        val o = arr.optJSONObject(i)
        DisorderRecord(
            name = o?.optString("name").orEmpty(),
            note = o?.optString("note").orEmpty(),
            time = o?.optLong("time") ?: 0L,
        )
    }.filter { it.name.isNotEmpty() }

private fun DisorderRecord.toJSONObject(): JSONObject = JSONObject().apply {
    put("name", name)
    if (note.isNotEmpty()) put("note", note)
    if (time > 0L) put("time", time)
}
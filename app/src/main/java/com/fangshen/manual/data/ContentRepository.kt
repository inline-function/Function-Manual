package com.fangshen.manual.data

import com.fangshen.manual.engine.InteractionEngine
import com.fangshen.manual.model.AppSettings
import com.fangshen.manual.model.CustomEffect
import com.fangshen.manual.model.CustomInteraction
import com.fangshen.manual.model.CustomPack
import com.fangshen.manual.model.EffectCatalog
import kotlinx.coroutines.flow.StateFlow

/**
 * 用户内容仓库：自定义效应/效应包/联用配对 + 应用设置。
 * 全部持久化在 data.json，变更即写回并同步注入 EffectCatalog / InteractionEngine。
 */
class ContentRepository(private val store: JsonDataStore) {

    val state: StateFlow<JsonData> = store.state

    val settings: AppSettings get() = store.snapshot().settings

    // —— 设置 ——
    fun updateSettings(settings: AppSettings) {
        val data = store.snapshot()
        store.save(data.copy(settings = settings))
    }

    // —— 自定义效应 ——
    val customEffects: List<CustomEffect> get() = store.snapshot().customEffects

    fun saveCustomEffect(effect: CustomEffect) {
        val data = store.snapshot()
        val replaced = data.customEffects.filterNot { it.name == effect.name } + effect
        store.save(data.copy(customEffects = replaced))
        applyCustom()
    }

    fun deleteCustomEffect(name: String) {
        val data = store.snapshot()
        store.save(data.copy(customEffects = data.customEffects.filterNot { it.name == name }))
        applyCustom()
    }

    // —— 自定义效应包 ——
    val customPacks: List<CustomPack> get() = store.snapshot().customPacks

    fun saveCustomPack(pack: CustomPack) {
        val data = store.snapshot()
        val replaced = data.customPacks.filterNot { it.name == pack.name } + pack
        store.save(data.copy(customPacks = replaced))
        applyCustom()
    }

    fun deleteCustomPack(name: String) {
        val data = store.snapshot()
        store.save(data.copy(customPacks = data.customPacks.filterNot { it.name == name }))
        applyCustom()
    }

    // —— 自定义联用配对 ——
    val customInteractions: List<CustomInteraction> get() = store.snapshot().customInteractions

    fun saveCustomInteraction(pair: CustomInteraction) {
        val data = store.snapshot()
        val replaced = data.customInteractions.filterNot {
            it.matcherA == pair.matcherA && it.matcherB == pair.matcherB && it.title == pair.title
        } + pair
        store.save(data.copy(customInteractions = replaced))
        applyCustom()
    }

    fun deleteCustomInteraction(pair: CustomInteraction) {
        val data = store.snapshot()
        store.save(data.copy(customInteractions = data.customInteractions.filterNot { it == pair }))
        applyCustom()
    }

    /** 把用户内容同步进引擎（启动与每次保存后调用） */
    fun applyCustom() {
        val data = store.snapshot()
        EffectCatalog.updateCustom(
            defs = data.customEffects.map { it.toEffectDef() },
            packs = data.customPacks.associate { it.name to it.members },
        )
        EffectCatalog.updateCustomPacksDescriptions(
            data.customPacks.associate { it.name to it.description },
        )
        InteractionEngine.setCustomPairs(data.customInteractions)
    }
}

package com.fangshen.manual

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import com.fangshen.manual.data.AlertRepository
import com.fangshen.manual.data.AppDatabase
import com.fangshen.manual.data.DrugRepository
import com.fangshen.manual.data.EntryRepository
import com.fangshen.manual.data.JsonDataStore
import com.fangshen.manual.data.ProfileRepository

class FangShenApplication : Application() {

    lateinit var entryRepository: EntryRepository
        private set
    lateinit var drugRepository: DrugRepository
        private set
    lateinit var profileRepository: ProfileRepository
        private set
    lateinit var alertRepository: AlertRepository
        private set

    /** 全部数据存放在外部私有目录的 JSON 文件里（getExternalFilesDir/fangshen/data.json） */
    lateinit var jsonDataStore: JsonDataStore
        private set

    override fun onCreate() {
        super.onCreate()
        jsonDataStore = JsonDataStore(this)
        // 首次从旧 Room 数据库迁移；JSON 文件已存在则直接跳过
        runBlocking(Dispatchers.IO) {
            jsonDataStore.migrateFromRoomIfNeeded(AppDatabase.get(this@FangShenApplication))
        }
        entryRepository = EntryRepository(jsonDataStore)
        drugRepository = DrugRepository(jsonDataStore)
        profileRepository = ProfileRepository(jsonDataStore)
        alertRepository = AlertRepository(jsonDataStore)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = "日程提醒"
        val descriptionText = "芳沈手册的日程服药提醒通知"
        val channel = NotificationChannel(
            CHANNEL_SCHEDULE, name, NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_SCHEDULE = "fangshen_schedule"
    }
}

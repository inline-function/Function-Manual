package com.fangshen.manual.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.fangshen.manual.data.entity.EntryEntity

object AlarmScheduler {

    private const val EXTRA_ENTRY_ID = "entry_id"
    private const val EXTRA_TITLE = "title"

    /** 为一条日程条目安排精确提醒 */
    fun schedule(context: Context, entry: EntryEntity) {
        if (!entry.notify || entry.eventTime <= System.currentTimeMillis()) return
        val pending = pendingIntent(context, entry.id, entry.title, PendingIntentFlagUpdate)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            // 没有精确闹钟权限时回退为不精确的提醒窗口
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, entry.eventTime, pending,
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, entry.eventTime, pending,
            )
        }
    }

    /** 取消条目提醒 */
    fun cancel(context: Context, entryId: Long) {
        val pending = pendingIntent(context, entryId, "", PendingIntentFlagUpdate)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pending)
    }

    /** 开机后重新安排所有未来日程 */
    suspend fun rescheduleAll(context: Context, entries: List<EntryEntity>) {
        val now = System.currentTimeMillis()
        entries.filter { it.notify && it.eventTime > now }.forEach { schedule(context, it) }
    }

    private val PendingIntentFlagUpdate: Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        else PendingIntent.FLAG_UPDATE_CURRENT

    private fun pendingIntent(context: Context, entryId: Long, title: String, flags: Int) =
        PendingIntent.getBroadcast(
            context,
            entryId.toInt(),
            Intent(context, ReminderReceiver::class.java).apply {
                action = ReminderReceiver.ACTION_REMIND
                putExtra(EXTRA_ENTRY_ID, entryId)
                putExtra(EXTRA_TITLE, title)
            },
            flags,
        )
}

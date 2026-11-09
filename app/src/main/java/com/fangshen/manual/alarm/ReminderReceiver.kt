package com.fangshen.manual.alarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fangshen.manual.MainActivity
import com.fangshen.manual.R

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_REMIND) return
        val entryId = intent.getLongExtra("entry_id", 0L)
        val title = intent.getStringExtra("title") ?: "日程提醒"

        val contentIntent = PendingIntent.getActivity(
            context,
            entryId.toInt(),
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, "fangshen_schedule")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(entryId.toInt(), notification)
        } catch (e: SecurityException) {
            // 未授予通知权限时忽略
        }
    }

    companion object {
        const val ACTION_REMIND = "com.fangshen.manual.action.REMIND"
    }
}

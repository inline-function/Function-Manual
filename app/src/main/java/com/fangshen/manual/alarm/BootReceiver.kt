package com.fangshen.manual.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fangshen.manual.FangShenApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_LOCKED_BOOT_COMPLETED &&
            intent.action != "android.intent.action.QUICKBOOT_POWERON"
        ) return
        val pendingResult = goAsync()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        scope.launch {
            try {
                val app = context.applicationContext as FangShenApplication
                val now = System.currentTimeMillis()
                val entries = app.entryRepository.upcomingSchedules(now)
                AlarmScheduler.rescheduleAll(context, entries)
            } finally {
                pendingResult.finish()
            }
        }
    }
}

package com.studio.hello.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager

class ScheduleAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val scheduleId = intent.getLongExtra("SCHEDULE_ID", -1)
        val content = intent.getStringExtra("SCHEDULE_CONTENT") ?: ""

        if (scheduleId == -1L) return

        // 화면을 깨우기 위한 WakeLock
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "DaySchedule:AlarmWakeLock"
        )
        wakeLock.acquire(30000) // 30초 동안 유지

        // 알람 화면(Activity) 실행
        val alarmIntent = Intent().apply {
            setClassName(context.packageName, "com.studio.hello.presentation.alarm.AlarmActivity")
            putExtra("SCHEDULE_CONTENT", content)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(alarmIntent)
    }
}

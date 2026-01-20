package com.studio.hello.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.studio.hello.domain.repository.ScheduleRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ScheduleAlarmReceiver : BroadcastReceiver() {
    
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AlarmReceiverEntryPoint {
        fun scheduleRepository(): ScheduleRepository
    }

    override fun onReceive(context: Context, intent: Intent) {
        val scheduleId = intent.getLongExtra("SCHEDULE_ID", -1)
        if (scheduleId == -1L) return

        val pendingResult = goAsync()
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            AlarmReceiverEntryPoint::class.java
        )
        val repository = entryPoint.scheduleRepository()

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val schedule = repository.getScheduleById(scheduleId)
                
                // 일정이 존재하고 아직 완료되지 않은 경우에만 알람 표시
                if (schedule != null && !schedule.isCompleted) {
                    val content = schedule.content

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
            } finally {
                pendingResult.finish()
            }
        }
    }
}

package com.studio.hello.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.studio.hello.domain.alarm.AlarmScheduler
import com.studio.hello.domain.model.Schedule
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(schedule: Schedule) {
        val intent = Intent(context, ScheduleAlarmReceiver::class.java).apply {
            putExtra("SCHEDULE_ID", schedule.id)
            putExtra("SCHEDULE_CONTENT", schedule.content)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            schedule.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val time = schedule.alarmTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        // 과거 시간인 경우 예약을 건너뜀 (안전 장치)
        if (time <= System.currentTimeMillis()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
    }

    override fun cancel(schedule: Schedule) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                schedule.id.toInt(),
                Intent(context, ScheduleAlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}

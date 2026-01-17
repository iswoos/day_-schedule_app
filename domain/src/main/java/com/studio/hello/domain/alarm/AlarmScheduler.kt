package com.studio.hello.domain.alarm

import com.studio.hello.domain.model.Schedule

interface AlarmScheduler {
    fun schedule(schedule: Schedule)
    fun cancel(schedule: Schedule)
}

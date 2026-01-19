package com.studio.hello.domain.repository

import com.studio.hello.domain.model.Schedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getTodaySchedules(): Flow<List<Schedule>>
    suspend fun insertSchedule(schedule: Schedule)
    suspend fun updateSchedule(schedule: Schedule)
    suspend fun deleteSchedule(schedule: Schedule)
    suspend fun deleteAllSchedules()
    suspend fun deleteOldSchedules()
}

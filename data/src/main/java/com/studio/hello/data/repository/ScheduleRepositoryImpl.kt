package com.studio.hello.data.repository

import com.studio.hello.data.local.dao.ScheduleDao
import com.studio.hello.data.local.entity.ScheduleEntity
import com.studio.hello.domain.model.Schedule
import com.studio.hello.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val dao: ScheduleDao
) : ScheduleRepository {

    override fun getTodaySchedules(): Flow<List<Schedule>> {
        val todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
        val todayStartMillis = todayStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        
        return dao.getTodaySchedules(todayStartMillis).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertSchedule(schedule: Schedule) {
        dao.insertSchedule(ScheduleEntity.fromDomain(schedule))
    }

    override suspend fun updateSchedule(schedule: Schedule) {
        dao.updateSchedule(ScheduleEntity.fromDomain(schedule))
    }

    override suspend fun deleteSchedule(schedule: Schedule) {
        dao.deleteSchedule(ScheduleEntity.fromDomain(schedule))
    }

    override suspend fun deleteAllSchedules() {
        dao.deleteAllSchedules()
    }

    override suspend fun deleteOldSchedules() {
        val todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
        val todayStartMillis = todayStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        dao.deleteOldSchedules(todayStartMillis)
    }
}

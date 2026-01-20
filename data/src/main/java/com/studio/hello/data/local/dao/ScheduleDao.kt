package com.studio.hello.data.local.dao

import androidx.room.*
import com.studio.hello.data.local.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE createdAtMillis >= :todayStartMillis ORDER BY alarmTimeMillis ASC")
    fun getTodaySchedules(todayStartMillis: Long): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE id = :id")
    suspend fun getScheduleById(id: Long): ScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(entity: ScheduleEntity)

    @Update
    suspend fun updateSchedule(entity: ScheduleEntity)

    @Delete
    suspend fun deleteSchedule(entity: ScheduleEntity)

    @Query("DELETE FROM schedules")
    suspend fun deleteAllSchedules()

    @Query("DELETE FROM schedules WHERE createdAtMillis < :todayStartMillis")
    suspend fun deleteOldSchedules(todayStartMillis: Long)
}

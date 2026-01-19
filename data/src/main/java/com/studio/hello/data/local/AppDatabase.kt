package com.studio.hello.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.studio.hello.data.local.dao.ScheduleDao
import com.studio.hello.data.local.entity.ScheduleEntity

@Database(entities = [ScheduleEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        const val DATABASE_NAME = "day_schedule_db"
    }
}

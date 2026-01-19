package com.studio.hello.data.local.entity

import androidx.room.*
import com.studio.hello.domain.model.Schedule
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val alarmTimeMillis: Long,
    val isCompleted: Boolean,
    val createdAtMillis: Long
) {
    fun toDomain(): Schedule {
        return Schedule(
            id = id,
            content = content,
            alarmTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(alarmTimeMillis), ZoneId.systemDefault()),
            isCompleted = isCompleted,
            createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAtMillis), ZoneId.systemDefault())
        )
    }

    companion object {
        fun fromDomain(domain: Schedule): ScheduleEntity {
            return ScheduleEntity(
                id = domain.id,
                content = domain.content,
                alarmTimeMillis = domain.alarmTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                isCompleted = domain.isCompleted,
                createdAtMillis = domain.createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        }
    }
}

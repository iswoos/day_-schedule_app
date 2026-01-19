package com.studio.hello.domain.model

import java.time.LocalDateTime

data class Schedule(
    val id: Long = 0,
    val content: String,
    val alarmTime: LocalDateTime,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    /**
     * 자정 이후 데이터인지 확인하는 로직
     */
    fun isCreatedToday(): Boolean {
        val todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
        return createdAt.isAfter(todayStart) || createdAt.isEqual(todayStart)
    }
}

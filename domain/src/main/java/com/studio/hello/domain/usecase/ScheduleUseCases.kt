package com.studio.hello.domain.usecase

import com.studio.hello.domain.model.Schedule
import com.studio.hello.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodaySchedulesUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    operator fun invoke(): Flow<List<Schedule>> = repository.getTodaySchedules()
}

class AddScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(schedule: Schedule) = repository.insertSchedule(schedule)
}

class UpdateScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(schedule: Schedule) = repository.updateSchedule(schedule)
}

class DeleteScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(schedule: Schedule) = repository.deleteSchedule(schedule)
}

class CleanOldSchedulesUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke() = repository.deleteOldSchedules()
}

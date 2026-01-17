package com.studio.hello.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studio.hello.domain.model.Schedule
import com.studio.hello.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getTodaySchedulesUseCase: GetTodaySchedulesUseCase,
    private val addScheduleUseCase: AddScheduleUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase,
    private val cleanOldSchedulesUseCase: CleanOldSchedulesUseCase,
    private val alarmScheduler: com.studio.hello.domain.alarm.AlarmScheduler
) : ViewModel() {

    val schedules: StateFlow<List<Schedule>> = getTodaySchedulesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            cleanOldSchedulesUseCase()
        }
    }

    fun addSchedule(schedule: Schedule) {
        viewModelScope.launch {
            addScheduleUseCase(schedule)
            alarmScheduler.schedule(schedule)
        }
    }

    fun toggleScheduleCompletion(schedule: Schedule) {
        viewModelScope.launch {
            updateScheduleUseCase(schedule.copy(isCompleted = !schedule.isCompleted))
        }
    }

    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch {
            deleteScheduleUseCase(schedule)
        }
    }
}

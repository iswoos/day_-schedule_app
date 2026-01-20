package com.studio.hello.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studio.hello.domain.model.Schedule
import com.studio.hello.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTodaySchedulesUseCase: GetTodaySchedulesUseCase,
    private val addScheduleUseCase: AddScheduleUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase,
    private val cleanOldSchedulesUseCase: CleanOldSchedulesUseCase,
    private val alarmScheduler: com.studio.hello.domain.alarm.AlarmScheduler
) : ViewModel() {

    private val _todayStartMillis = MutableStateFlow(calculateTodayStartMillis())

    @OptIn(ExperimentalCoroutinesApi::class)
    val schedules: StateFlow<List<Schedule>> = _todayStartMillis
        .flatMapLatest { millis -> getTodaySchedulesUseCase(millis) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        refresh()
    }

    fun refresh() {
        val currentMillis = calculateTodayStartMillis()
        if (_todayStartMillis.value != currentMillis || schedules.value.isEmpty()) {
            _todayStartMillis.value = currentMillis
            viewModelScope.launch {
                cleanOldSchedulesUseCase(currentMillis)
            }
        }
    }

    private fun calculateTodayStartMillis(): Long {
        return LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
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

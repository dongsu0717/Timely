package com.dongsu.timely.presentation.viewmodel.calendar.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {
    private var _scheduleList = MutableStateFlow<TimelyResult<MutableList<Schedule>>>(TimelyResult.Empty)
    val scheduleList = _scheduleList.asStateFlow()

    fun findAllSchedule(){
        viewModelScope.launch {
            _scheduleList.value = scheduleRepository.getAllSchedule()
        }
    }
}

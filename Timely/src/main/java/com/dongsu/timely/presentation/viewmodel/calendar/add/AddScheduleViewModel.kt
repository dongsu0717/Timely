package com.dongsu.timely.presentation.viewmodel.calendar.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.domain.usecase.schedule.AddScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(
    private val addScheduleUseCase: AddScheduleUseCase
) : ViewModel() {

    fun insertSchedule(newSchedule: Schedule) {
        viewModelScope.launch {
            addScheduleUseCase(newSchedule)
        }
    }
}
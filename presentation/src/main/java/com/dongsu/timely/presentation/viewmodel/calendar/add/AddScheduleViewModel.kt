package com.dongsu.timely.presentation.viewmodel.calendar.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.domain.usecase.AddScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddScheduleViewModel @Inject constructor(
    private val addScheduleUseCase: AddScheduleUseCase
) : ViewModel() {

    private val _addScheduleState = MutableStateFlow<TimelyResult<Unit>>(TimelyResult.Empty)
    val addScheduleState = _addScheduleState.asStateFlow()

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> get() = _currentDate

    private val _currentTimeFlow = MutableStateFlow<String>(formatCurrentTime())
    val currentTimeFlow = _currentTimeFlow.asStateFlow()

    init {
        updateCurrentDate()
        updateCurrentTime()
    }

    fun insertSchedule(newSchedule: Schedule) {
        viewModelScope.launch {
            _addScheduleState.value = TimelyResult.Loading
            _addScheduleState.value = addScheduleUseCase(newSchedule)
        }
    }

    private fun updateCurrentDate() {
        _currentDate.value = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Calendar.getInstance().time)
    }

    private fun updateCurrentTime() {
        _currentTimeFlow.value = formatCurrentTime()
    }

    private fun formatCurrentTime() = SimpleDateFormat("HH:mm", Locale.getDefault())
        .format(Calendar.getInstance().time)
}
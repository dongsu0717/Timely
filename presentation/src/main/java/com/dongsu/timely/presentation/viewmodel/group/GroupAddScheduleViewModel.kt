package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.usecase.GroupAddScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GroupAddScheduleViewModel @Inject constructor(
    private val groupAddScheduleUseCase: GroupAddScheduleUseCase
) : ViewModel() {

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> get() = _currentDate

    private val _currentTimeFlow = MutableStateFlow<String>(formatCurrentTime())
    val currentTimeFlow = _currentTimeFlow.asStateFlow()

    private val _addScheduleState = MutableStateFlow<TimelyResult<Unit>>(TimelyResult.Empty)
    val addScheduleState = _addScheduleState.asStateFlow()

    init {
        updateCurrentDate()
        updateAfterTwentyMinTime()
    }

    fun addSchedule(groupId: Int, groupSchedule: GroupSchedule) {
        viewModelScope.launch {
            _addScheduleState.value = TimelyResult.Loading
            _addScheduleState.value = groupAddScheduleUseCase(groupId, groupSchedule)
        }
    }


    private fun updateCurrentDate() {
        _currentDate.value = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Calendar.getInstance().time)
    }

    private fun updateAfterTwentyMinTime() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 20)
        _currentTimeFlow.value = SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(calendar.time)
    }

    private fun formatCurrentTime() = SimpleDateFormat("HH:mm", Locale.getDefault())
        .format(Calendar.getInstance().time)
}
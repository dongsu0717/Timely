package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import com.dongsu.timely.domain.usecase.ParticipationScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDateViewModel @Inject constructor(
    private val groupScheduleRepository: GroupScheduleRepository,
    private val participationScheduleUseCase: ParticipationScheduleUseCase
): ViewModel() {

    private var _groupScheduleList = MutableStateFlow<TimelyResult<List<TotalGroupScheduleInfo>>>(TimelyResult.Empty)
    val groupScheduleList = _groupScheduleList.asStateFlow()

    fun getGroupSchedule(groupId: Int) {
        viewModelScope.launch {
            _groupScheduleList.value = TimelyResult.Loading
            _groupScheduleList.value = groupScheduleRepository.fetchGroupScheduleList(groupId)
        }
    }

    fun participationSchedule(groupId: Int, scheduleId: Int) {
        viewModelScope.launch {
            participationScheduleUseCase(groupId, scheduleId)
        }
    }
    fun cancelParticipationSchedule(groupId: Int, scheduleId: Int) {
        viewModelScope.launch {
            groupScheduleRepository.cancelParticipationSchedule(groupId, scheduleId)
        }
    }

}
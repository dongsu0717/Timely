package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.GroupScheduleInfo
import com.dongsu.timely.domain.model.map.GroupMeetingInfo
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import com.dongsu.timely.domain.repository.UserRepository
import com.dongsu.timely.domain.usecase.FetchGroupScheduleToShowMapUseCase
import com.dongsu.timely.domain.usecase.FetchMyUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupLocationViewModel @Inject constructor(
    private val groupScheduleRepository: GroupScheduleRepository,
    private val fetchGroupScheduleToShowMapUseCase : FetchGroupScheduleToShowMapUseCase,
    private val fetchMyUserIdUseCase: FetchMyUserIdUseCase,
    private val userRepository: UserRepository
): ViewModel() {

    private val _groupScheduleToShowMap = MutableStateFlow<TimelyResult<GroupScheduleInfo>>(TimelyResult.Empty)
    val groupScheduleToShowMap = _groupScheduleToShowMap.asStateFlow()

    private val _myUserId = MutableStateFlow<TimelyResult<Int>>(TimelyResult.Empty)
    val myUserId = _myUserId.asStateFlow()

    private val _groupMeetingInfo = MutableStateFlow<TimelyResult<GroupMeetingInfo>>(TimelyResult.Empty)
    val groupMeetingInfo =_groupMeetingInfo.asStateFlow()

    private val _stateMessage = MutableStateFlow<TimelyResult<Unit>>(TimelyResult.Empty)
    val stateMessage = _stateMessage.asStateFlow()

    fun fetchGroupScheduleShowMap(groupId: Int) {
        viewModelScope.launch {
            _groupScheduleToShowMap.value = TimelyResult.Loading
            _groupScheduleToShowMap.value = fetchGroupScheduleToShowMapUseCase(groupId)
        }
    }

    fun fetchMyUserId() {
        viewModelScope.launch {
            _myUserId.value = TimelyResult.Loading
            _myUserId.value = fetchMyUserIdUseCase()
        }
    }

    fun fetchGroupMeetingInfo(scheduleId: Int) {
        viewModelScope.launch {
            _groupMeetingInfo.value = TimelyResult.Loading
            _groupMeetingInfo.value = groupScheduleRepository.fetchGroupMeetingInfo(scheduleId)
        }
    }

    fun updateStateMessage(scheduleId: Int, stateMessage: String) {
        viewModelScope.launch {
            _stateMessage.value = TimelyResult.Loading
            _stateMessage.value = groupScheduleRepository.updateStateMessage(scheduleId, stateMessage)
        }
    }

    suspend fun countLateness() {
        return userRepository.countLateness()
    }
}
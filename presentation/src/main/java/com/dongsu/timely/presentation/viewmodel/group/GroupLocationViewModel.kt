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

    private val _myUserId = MutableStateFlow<TimelyResult<Int>>(TimelyResult.Empty)
    val myUserId = _myUserId.asStateFlow()

    private val _groupScheduleToShowMap = MutableStateFlow<TimelyResult<GroupScheduleInfo>>(TimelyResult.Empty)
    val groupScheduleToShowMap = _groupScheduleToShowMap.asStateFlow()

    private val _groupMembersLocation = MutableStateFlow<TimelyResult<GroupMeetingInfo>>(TimelyResult.Empty)
    var groupMembersLocation =_groupMembersLocation.asStateFlow()

    fun getParticipationMemberLocation(scheduleId: Int) {
        viewModelScope.launch {
            _groupMembersLocation.value =
                groupScheduleRepository.getParticipationMemberLocation(scheduleId)
        }
    }

    fun fetchGroupScheduleShowMap(groupId: Int) {
        viewModelScope.launch {
            _groupScheduleToShowMap.value = TimelyResult.Loading
            _groupScheduleToShowMap.value = fetchGroupScheduleToShowMapUseCase(groupId)
        }
    }

    suspend fun updateStateMessage(scheduleId: Int, stateMessage: String)
    = groupScheduleRepository.updateStateMessage(scheduleId, stateMessage)

    fun fetchMyUserId() {
        viewModelScope.launch {
            _myUserId.value = TimelyResult.Loading
            _myUserId.value = fetchMyUserIdUseCase()
        }
    }

    suspend fun countLateness() {
        return userRepository.countLateness()
    }
}
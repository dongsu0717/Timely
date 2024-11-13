package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.ParticipationMember
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupLocationViewModel @Inject constructor(
    private val groupScheduleRepository: GroupScheduleRepository
): ViewModel() {

    private val _groupMembersLocation = MutableStateFlow<TimelyResult<List<ParticipationMember>>>(TimelyResult.Empty)
    var groupMembersLocation =_groupMembersLocation.asStateFlow()

    fun getParticipationMemberLocation(scheduleId: Int) {
        viewModelScope.launch {
            _groupMembersLocation.value = groupScheduleRepository.getParticipationMemberLocation(scheduleId)
        }
    }
}
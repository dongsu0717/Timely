package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.ParticipationMember
import com.dongsu.timely.domain.model.PlaceLocation
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import com.dongsu.timely.domain.usecase.GetScheduleShowMapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupLocationViewModel @Inject constructor(
    private val groupScheduleRepository: GroupScheduleRepository,
    private val getScheduleShowMapUseCase : GetScheduleShowMapUseCase
): ViewModel() {

    private val _groupMembersLocation = MutableStateFlow<TimelyResult<List<ParticipationMember>>>(TimelyResult.Empty)
    var groupMembersLocation =_groupMembersLocation.asStateFlow()

    fun getParticipationMemberLocation(scheduleId: Int) {
        viewModelScope.launch {
            _groupMembersLocation.value =
                groupScheduleRepository.getParticipationMemberLocation(scheduleId)
        }
    }
    suspend fun getScheduleIdShowMap(groupId: Int): PlaceLocation { // return값 Int는 scheduleId
        return getScheduleShowMapUseCase(groupId)
    }
}
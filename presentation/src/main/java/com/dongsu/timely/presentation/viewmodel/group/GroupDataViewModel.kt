package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.GroupScheduleInfo
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDataViewModel @Inject constructor(
    private val groupScheduleRepository: GroupScheduleRepository
): ViewModel() {

    private var _groupScheduleList = MutableStateFlow<TimelyResult<List<GroupScheduleInfo>>>(TimelyResult.Empty)
    val groupScheduleList = _groupScheduleList.asStateFlow()

    fun getGroupSchedule(groupId: Int) {
        viewModelScope.launch {
            _groupScheduleList.value = groupScheduleRepository.getAllSchedule(groupId)
        }
    }

}
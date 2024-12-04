package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    private var groupRepository: GroupRepository,
) : ViewModel() {

    private val _createGroupState = MutableStateFlow<TimelyResult<Unit>>(TimelyResult.Empty)
    val createGroupState = _createGroupState.asStateFlow()

    fun createGroup(groupName: String, groupColor: Int) {
        viewModelScope.launch {
            _createGroupState.value = TimelyResult.Loading
            _createGroupState.value = groupRepository.createGroup(groupName, groupColor)
        }
    }
}
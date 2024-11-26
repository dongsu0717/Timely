package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.InviteCode
import com.dongsu.timely.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupManagementViewModel @Inject constructor(
    private var groupRepository: GroupRepository,
) : ViewModel() {

    private val _inviteCode = MutableStateFlow<TimelyResult<InviteCode>>(TimelyResult.Empty)
    val inviteCode = _inviteCode

    fun createInviteCode(groupId: Int) {
        viewModelScope.launch {
            _inviteCode.value = TimelyResult.Loading
            _inviteCode.value = groupRepository.createInviteCode(groupId)
        }
    }

}
package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.InviteCode
import com.dongsu.timely.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupManagementViewModel @Inject constructor(
    private var groupRepository: GroupRepository
): ViewModel(){

    suspend fun createInviteCode(groupId: Int): TimelyResult<InviteCode>
    = groupRepository.createInviteCode(groupId)
}
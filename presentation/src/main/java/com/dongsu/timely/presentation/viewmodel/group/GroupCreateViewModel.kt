package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    private var groupRepository: GroupRepository
) : ViewModel() {

    suspend fun createGroup(groupName: String): TimelyResult<Unit> {
        return try {
            groupRepository.createGroup(groupName)
            TimelyResult.Success(Unit)
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }
    }
}
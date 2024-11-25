package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupCreateViewModel @Inject constructor(
    private var groupRepository: GroupRepository,
) : ViewModel() {

    //헷갈리는 부분. 리턴값이 없는 부분은 배킹필드로 해서 그 값을 가져오는식으로 해야하나?
    //아니면 밑처럼 사용해도 되는건가?
    suspend fun createGroup(groupName: String): TimelyResult<Unit> {
        return try {
            groupRepository.createGroup(groupName)
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }
    }
}
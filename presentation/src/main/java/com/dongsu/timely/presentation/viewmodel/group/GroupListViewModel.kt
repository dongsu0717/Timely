package com.dongsu.timely.presentation.viewmodel.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Group
import com.dongsu.timely.domain.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private var groupRepository: GroupRepository
) : ViewModel() {
    private var _groupList = MutableStateFlow<TimelyResult<List<Group>>>(TimelyResult.Empty)
    val groupList = _groupList.asStateFlow()

    fun getGroupList(){
        viewModelScope.launch {
            _groupList.value = TimelyResult.Success(groupRepository.getMyGroupList())
        }
    }
}
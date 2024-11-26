package com.dongsu.timely.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.repository.GroupRepository
import com.dongsu.timely.domain.repository.UserRepository
import com.dongsu.timely.domain.usecase.UserTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimelyViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val userTokenUseCase: UserTokenUseCase,
) : ViewModel() {

    private val _sendKaKaoToken = MutableStateFlow<TimelyResult<Unit>>(TimelyResult.Empty)
    val sendKaKaoToken = _sendKaKaoToken.asStateFlow()

    private var _loginStatus = MutableStateFlow<TimelyResult<Boolean>>(TimelyResult.Empty)
    val loginStatus = _loginStatus.asStateFlow()

    fun sendKaKaoToken(token: String) {
        viewModelScope.launch {
            _sendKaKaoToken.value = TimelyResult.Loading
            _sendKaKaoToken.value = userTokenUseCase(token)
        }
    }

    fun isLoggedIn() {
        viewModelScope.launch {
            _loginStatus.value = userRepository.isLoggedIn()
            Log.e("loginStatus", _loginStatus.toString())
        }
    }

    suspend fun joinGroup(inviteCode: String) = groupRepository.joinGroup(inviteCode)

}
package com.dongsu.timely.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Token
import com.dongsu.timely.domain.repository.FCMRepository
import com.dongsu.timely.domain.repository.GroupRepository
import com.dongsu.timely.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimelyViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val fcmRepository: FCMRepository,
) : ViewModel() {

    private val _fetchToken = MutableStateFlow<TimelyResult<Token>>(TimelyResult.Empty)
    val fetchToken = _fetchToken.asStateFlow()

    private val _sendFCMTokenState = MutableStateFlow<TimelyResult<Unit>>(TimelyResult.Empty)
    val sendFCMTokenState = _sendFCMTokenState.asStateFlow()

    private val _saveTokenLocalState = MutableStateFlow<TimelyResult<Unit>>(TimelyResult.Empty)
    val saveTokenLocalState = _saveTokenLocalState.asStateFlow()

    private var _loginStatus = MutableStateFlow<TimelyResult<Boolean>>(TimelyResult.Empty)
    val loginStatus = _loginStatus.asStateFlow()

    fun sendKaKaoTokenAndGetToken(token: String) {
        viewModelScope.launch {
            _fetchToken.value = TimelyResult.Loading
            _fetchToken.value = userRepository.sendKaKaoTokenAndGetToken(token)
        }
    }

    fun sendFCMToken() {
        viewModelScope.launch {
            val fcmToken = fcmRepository.getFCMToken()
            _sendFCMTokenState.value = TimelyResult.Loading
            _sendFCMTokenState.value = userRepository.sendFCMToken(fcmToken)
        }
    }

    fun saveTokenLocal(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            _saveTokenLocalState.value = TimelyResult.Loading
            _saveTokenLocalState.value = userRepository.saveTokenLocal(accessToken, refreshToken)
        }
    }

    fun isLoggedIn() {
        viewModelScope.launch {
            _loginStatus.value = TimelyResult.Loading
            _loginStatus.value = userRepository.isLoggedIn()
        }
    }

    suspend fun joinGroup(inviteCode: String) = groupRepository.joinGroup(inviteCode)
}
package com.dongsu.timely.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.User
import com.dongsu.timely.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimelyViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private var _loginStatus = MutableStateFlow<TimelyResult<Boolean>>(TimelyResult.Empty)
    val loginStatus = _loginStatus.asStateFlow()

    suspend fun sendToken(token: String) {
        userRepository.sendToken(User(token))
    }

    fun isLoggedIn() {
        viewModelScope.launch {
            _loginStatus.value = userRepository.isLoggedIn()
            Log.e("loginStatus", _loginStatus.toString())
        }
    }
}
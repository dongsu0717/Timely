package com.dongsu.timely.data.datasource.remote

import android.util.Log
import com.dongsu.timely.data.mapper.UserMapper
import com.dongsu.timely.data.remote.api.FCMService
import com.dongsu.timely.data.remote.api.LoginService
import com.dongsu.timely.data.remote.api.UserService
import com.dongsu.timely.data.remote.dto.request.LateRequest
import com.dongsu.timely.data.remote.dto.request.SendFCMTokenRequest
import com.dongsu.timely.data.remote.dto.request.SendTokenRequest
import com.dongsu.timely.domain.model.Token
import com.dongsu.timely.domain.model.map.User
import javax.inject.Inject

class UserRemoteDatasourceImpl @Inject constructor(
    private val loginService: LoginService,
    private val fcmService: FCMService,
    private val userService: UserService,
    private val userMapper: UserMapper
): UserRemoteDatasource {

    override suspend fun sendKaKaoTokenAndGetToken(token: String): Token {
        val response = loginService.sendKaKaoTokenAndGetToken(SendTokenRequest(token))
        Log.e("서버에 카카오토큰 저장 성공","${response.body()?.accessToken}")
        return Token(accessToken = response.body()?.accessToken, refreshToken = response.body()?.refreshToken)
    }

    override suspend fun sendFCMToken(token: String) {
        fcmService.sendFcmToken(SendFCMTokenRequest(token))
        Log.e("서버에 FCM토큰 저장 성공","$token")
    }

    override suspend fun fetchMyInfo(): User {
        return when (val user = userService.fetchMyInfo().body()) {
            null -> User.EMPTY
            else -> userMapper.toDomainUser(user)
        }
    }


    override suspend fun countLateness(isLate: Boolean) {
        val response = userService.countLateness(LateRequest(isLate))
        if (!response.isSuccessful) {
            throw Exception("출석체크 Error: ${response.code()}, Message: ${response.message()}")
        }
    }
}
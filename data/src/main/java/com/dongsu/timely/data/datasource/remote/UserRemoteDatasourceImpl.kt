package com.dongsu.timely.data.datasource.remote

import android.util.Log
import com.dongsu.timely.data.mapper.UserMapper
import com.dongsu.timely.data.remote.api.LoginService
import com.dongsu.timely.domain.model.Token
import javax.inject.Inject

class UserRemoteDatasourceImpl @Inject constructor(
    private val loginService: LoginService
): UserRemoteDatasource {

    override suspend fun sendToken(token: String): Token {
        val response = loginService.sendToken(sendTokenRequest = UserMapper.toDto(token))
        Log.e("서버에 카카오토큰 저장 성공","${response.body()?.accessToken}")
        return Token(accessToken = response.body()?.accessToken, refreshToken = response.body()?.refreshToken)
    }

    override suspend fun sendFCMToken(token: String) {

    }
}
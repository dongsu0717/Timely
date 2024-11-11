package com.dongsu.timely.data.datasource.datasourceImpl

import android.util.Log
import com.dongsu.timely.data.datasource.UserLocalDatasource
import com.dongsu.timely.data.datasource.UserRemoteDatasource
import com.dongsu.timely.data.mapper.UserMapper
import com.dongsu.timely.data.remote.api.LoginService
import javax.inject.Inject

class UserRemoteDatasourceImpl @Inject constructor(
    private val loginService: LoginService,
    private val userLocalDatasource: UserLocalDatasource
): UserRemoteDatasource {

    override suspend fun sendToken(token: String) {
        val response = loginService.sendToken(sendTokenRequest = UserMapper.toDto(token))
        Log.e("서버에 카카오토큰 저장 성공","${response.body()?.accessToken}")
        response.body()?.let { body ->
            Log.e("서버에서 accessToken받기", "${body.accessToken}")
            userLocalDatasource.saveLoginStatus(body.accessToken, body.refreshToken)
        }
    }
}
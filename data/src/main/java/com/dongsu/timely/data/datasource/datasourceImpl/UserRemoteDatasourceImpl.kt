package com.dongsu.timely.data.datasource.datasourceImpl

import android.util.Log
import com.dongsu.timely.data.datasource.UserLocalDatasource
import com.dongsu.timely.data.datasource.UserRemoteDatasource
import com.dongsu.timely.data.di.Timely
import com.dongsu.timely.data.mapper.UserMapper
import com.dongsu.timely.data.remote.api.LoginService
import com.dongsu.timely.domain.model.User
import javax.inject.Inject

class UserRemoteDatasourceImpl @Inject constructor(
    @Timely private val loginService: LoginService,
    private val userLocalDatasource: UserLocalDatasource
): UserRemoteDatasource {

    override suspend fun sendToken(user: User) {
        val response = loginService.sendToken(sendTokenRequest = UserMapper.toDto(user))
        Log.e("서버에 카카오토큰 저장 성공",user.accessToken)
        response.body()?.let { body ->
            userLocalDatasource.saveLoginStatus(body.accessToken, body.refreshToken)
        }
        Log.e("서버에서 accessToken받기", "${response.body()?.accessToken}")
    }
}
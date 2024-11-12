package com.dongsu.timely.data.repository

import android.util.Log
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.datasource.local.UserLocalDatasource
import com.dongsu.timely.data.datasource.remote.UserRemoteDatasource
import com.dongsu.timely.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDatasource: UserLocalDatasource,
    private val userRemoteDatasource: UserRemoteDatasource
): UserRepository {
    override suspend fun sendToken(token: String) {
        val token = userRemoteDatasource.sendToken(token)
        if(token.accessToken != null && token.refreshToken != null){
            Log.e("서버에서 accessToken받기", "${token.accessToken}")
            userLocalDatasource.saveLoginStatus(token.accessToken.toString(), token.refreshToken.toString())
        }
    }

    override suspend fun saveStatus(accessToken: String, refreshToken: String)
    = userLocalDatasource.saveLoginStatus(accessToken,refreshToken)

    override suspend fun isLoggedIn():TimelyResult<Boolean>
    = userLocalDatasource.isLoggedIn()

    override suspend fun getUserProfile() {
    }

    override suspend fun updateUserProfile() {
    }

    override suspend fun kakaoLogout() {
    }

    override suspend fun deleteAccount() {
    }
}
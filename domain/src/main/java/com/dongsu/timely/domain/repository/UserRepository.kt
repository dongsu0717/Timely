package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Token
import com.dongsu.timely.domain.model.map.User

interface UserRepository {
    suspend fun sendToken(token: String): Token
    suspend fun saveTokenLocal(accessToken: String, refreshToken: String)
    suspend fun sendFCMToken(token: String)
    suspend fun saveLoginStatus(accessToken: String, refreshToken: String)
    suspend fun isLoggedIn(): TimelyResult<Boolean>
    suspend fun getMyInfo(): TimelyResult<User>
    suspend fun countLateness()
    suspend fun getUserProfile()
    suspend fun updateUserProfile()
    suspend fun kakaoLogout()
    suspend fun deleteAccount()
}
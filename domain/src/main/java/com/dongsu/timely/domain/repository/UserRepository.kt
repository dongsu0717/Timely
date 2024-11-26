package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Token
import com.dongsu.timely.domain.model.map.User

interface UserRepository {
    suspend fun sendKaKaoTokenAndGetToken(token: String): TimelyResult<Token>
    suspend fun saveTokenLocal(accessToken: String, refreshToken: String): TimelyResult<Unit>
    suspend fun sendFCMToken(token: String): TimelyResult<Unit>
    suspend fun isLoggedIn(): TimelyResult<Boolean>
    suspend fun fetchMyInfo(): TimelyResult<User>
    suspend fun countLateness()
    suspend fun getUserProfile()
    suspend fun updateUserProfile()
    suspend fun kakaoLogout()
    suspend fun deleteAccount()
}
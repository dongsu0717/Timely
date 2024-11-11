package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult

interface UserRepository {
    suspend fun sendToken(token: String)
    suspend fun saveStatus(accessToken: String, refreshToken: String)
    suspend fun isLoggedIn(): TimelyResult<Boolean>
    suspend fun getUserProfile()
    suspend fun updateUserProfile()
    suspend fun kakaoLogout()
    suspend fun deleteAccount()
}
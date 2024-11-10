package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.User

interface UserRepository {
    suspend fun sendToken(user: User)
    suspend fun saveStatus(accessToken: String, refreshToken: String)
    suspend fun isLoggedIn(): TimelyResult<Boolean>
    suspend fun getUserProfile()
    suspend fun updateUserProfile()
    suspend fun kakaoLogout()
    suspend fun deleteAccount()
}
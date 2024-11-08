package com.dongsu.timely.domain.repository

interface UserRepository {
    suspend fun kakaoLogin()
    suspend fun isLoggedIn()
    suspend fun getUserProfile()
    suspend fun updateUserProfile()
    suspend fun kakaoLogout()
    suspend fun deleteAccount()
}
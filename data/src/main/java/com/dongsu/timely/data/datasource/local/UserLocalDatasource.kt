package com.dongsu.timely.data.datasource.local

interface UserLocalDatasource {
    fun getToken():String
    suspend fun saveTokenLocal(accessToken: String, refreshToken: String)
    suspend fun isLoggedIn(): Boolean
}
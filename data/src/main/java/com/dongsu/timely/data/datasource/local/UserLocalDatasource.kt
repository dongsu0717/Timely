package com.dongsu.timely.data.datasource.local

import com.dongsu.timely.common.TimelyResult

interface UserLocalDatasource {
    fun getToken():String
    suspend fun saveLoginStatus(accessToken: String, refreshToken: String)
    suspend fun isLoggedIn(): TimelyResult<Boolean>
}
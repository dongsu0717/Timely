package com.dongsu.timely.data.datasource

import com.dongsu.timely.common.TimelyResult

interface UserLocalDatasource {
    suspend fun getToken()
    suspend fun saveLoginStatus(accessToken: String, refreshToken: String)
    suspend fun isLoggedIn(): TimelyResult<Boolean>
}
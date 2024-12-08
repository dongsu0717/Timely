package com.dongsu.timely.data.datasource.remote
import com.dongsu.timely.domain.model.Token
import com.dongsu.timely.domain.model.map.User

interface UserRemoteDatasource {
    suspend fun sendKaKaoTokenAndGetToken(token: String): Token
    suspend fun sendFCMToken(token: String)
    suspend fun fetchMyInfo(): User
    suspend fun countLateness(isLate: Boolean)
}
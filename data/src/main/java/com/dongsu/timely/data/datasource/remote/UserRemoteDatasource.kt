package com.dongsu.timely.data.datasource.remote
import com.dongsu.timely.domain.model.Token

interface UserRemoteDatasource {
    suspend fun sendToken(token: String): Token
    suspend fun sendFCMToken(token: String)
}
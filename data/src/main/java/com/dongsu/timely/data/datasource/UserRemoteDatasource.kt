package com.dongsu.timely.data.datasource

interface UserRemoteDatasource {
    suspend fun sendToken(token: String)
}
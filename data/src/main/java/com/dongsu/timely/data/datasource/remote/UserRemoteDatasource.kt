package com.dongsu.timely.data.datasource.remote

interface UserRemoteDatasource {
    suspend fun sendToken(token: String)
}
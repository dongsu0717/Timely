package com.dongsu.timely.data.datasource

import com.dongsu.timely.domain.model.User

interface UserRemoteDatasource {
    suspend fun sendToken(user: User)
}
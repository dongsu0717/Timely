package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.request.SendTokenRequest

object UserMapper {
    fun toDto(token: String): SendTokenRequest {
        return SendTokenRequest(accessToken = token)
    }
}
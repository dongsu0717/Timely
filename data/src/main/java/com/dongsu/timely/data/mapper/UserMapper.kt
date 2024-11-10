package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.request.SendTokenRequest
import com.dongsu.timely.domain.model.User

object UserMapper {
    fun toDto(user: User): SendTokenRequest {
        return SendTokenRequest(accessToken = user.accessToken)
    }
}
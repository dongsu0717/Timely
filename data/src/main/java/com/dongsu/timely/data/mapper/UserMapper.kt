package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.request.SendTokenRequest
import com.dongsu.timely.data.remote.dto.response.UserResponse
import com.dongsu.timely.domain.model.User

object UserMapper {
    fun toDto(token: String): SendTokenRequest {
        return SendTokenRequest(accessToken = token)
    }
    fun toDomain(userResponse: UserResponse): User {
        return User(
            userId = userResponse.userId,
            email = userResponse.email,
            nickname = userResponse.nickname
        )
    }
}
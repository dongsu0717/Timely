package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.map.UserResponse
import com.dongsu.timely.domain.model.map.User

class UserMapper {
    fun toDomainUser(userResponse: UserResponse): User {
        return User(
            userId = userResponse.userId,
            email = userResponse.email,
            nickname = userResponse.nickname
        )
    }
}
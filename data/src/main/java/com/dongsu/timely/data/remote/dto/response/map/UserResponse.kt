package com.dongsu.timely.data.remote.dto.response.map

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("email")
    val email: String?,

    @SerializedName("nickname")
    val nickname: String
)

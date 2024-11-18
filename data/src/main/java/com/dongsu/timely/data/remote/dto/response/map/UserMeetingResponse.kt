package com.dongsu.timely.data.remote.dto.response.map

import com.google.gson.annotations.SerializedName

data class UserMeetingResponse(
    @SerializedName("user")
    val user: UserResponse,

    @SerializedName("location")
    val location: LocationResponse,

    @SerializedName("stateMessage")
    val stateMessage: String = "가는중"
)

package com.dongsu.timely.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class SendTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
)
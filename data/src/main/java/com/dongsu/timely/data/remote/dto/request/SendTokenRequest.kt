package com.dongsu.timely.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class SendTokenRequest(
    @SerializedName("accessToken")
    val accessToken: String
)

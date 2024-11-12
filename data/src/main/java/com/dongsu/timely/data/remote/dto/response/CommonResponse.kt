package com.dongsu.timely.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String
)

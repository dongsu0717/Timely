package com.dongsu.timely.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class StateMessageResponse(
    @SerializedName("message")
    val message: String,
)
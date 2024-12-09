package com.dongsu.timely.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class LateRequest(
    @SerializedName("isLate")
    val isLate: Boolean
)

package com.dongsu.timely.data.remote.dto.response.map

import com.google.gson.annotations.SerializedName

data class TargetLocationResponse(
    @SerializedName("location")
    val location: String,

    @SerializedName("coordinate")
    val coordinate: LocationResponse
)

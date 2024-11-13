package com.dongsu.timely.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("locationLatitude")
    val locationLatitude: Double,

    @SerializedName("locationLongitude")
    val locationLongitude: Double
)

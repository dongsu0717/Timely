package com.dongsu.timely.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class LocationRequest(
    @SerializedName("locationLatitude")
    val locationLatitude: Double,

    @SerializedName("locationLongitude")
    val locationLongitude: Double
)

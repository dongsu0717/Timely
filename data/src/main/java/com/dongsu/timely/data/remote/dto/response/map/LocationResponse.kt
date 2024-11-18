package com.dongsu.timely.data.remote.dto.response.map

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double
)

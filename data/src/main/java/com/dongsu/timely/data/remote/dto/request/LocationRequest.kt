package com.dongsu.timely.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class LocationRequest(
    @SerializedName("locationLatitude")
    val locationLatitude: String,

    @SerializedName("locationLongitude")
    val locationLongitude: String
)

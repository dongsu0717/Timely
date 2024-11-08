package com.dongsu.timely.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TMapDistanceCalculationResponseDto(
    @SerializedName("features")
    val features: List<FeatureItems>
) {
    data class FeatureItems(
        @SerializedName("properties")
        val properties: Properties,
    )

    data class Properties(
        @SerializedName("totalDistance")
        val totalDistance: Long,
        @SerializedName("totalTime")
        val totalTime: Long,
    )
}

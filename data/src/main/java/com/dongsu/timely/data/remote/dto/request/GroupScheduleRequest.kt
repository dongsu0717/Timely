package com.dongsu.timely.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class GroupScheduleRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("startTime")
    val startTime: String,

    @SerializedName("endTime")
    val endTime: String,

    @SerializedName("isAlarmEnabled")
    val isAlarmEnabled: Boolean,

    @SerializedName("alarmBeforeHours")
    val alarmBeforeHours: Int,

    @SerializedName("location")
    val location: String,

    @SerializedName("locationLatitude")
    val locationLatitude: Double,

    @SerializedName("locationLongitude")
    val locationLongitude: Double
)

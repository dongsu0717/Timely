package com.dongsu.timely.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class GroupScheduleResponse(
    @SerializedName("groupId")
    val groupId: Int,

    @SerializedName("createUserId")
    val createUserId: Int,

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

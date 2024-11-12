package com.dongsu.timely.domain.model

data class GroupSchedule(
    val title: String ,
    val startTime: String,
    val endTime: String,
    val isAlarmEnabled: Boolean,
    val alarmBeforeHours: Int,
    val location: String,
    val latitude: Double,
    val longitude: Double
)

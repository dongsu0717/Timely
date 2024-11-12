package com.dongsu.timely.domain.model

data class GroupScheduleInfo(
    val scheduleId: Int,
    val groupId: Int,
    val createUserId: Int,
    val title: String,
    val startTime: String,
    val endTime: String,
    val isAlarmEnabled: Boolean,
    val alarmBeforeHours: Int,
    val location: String,
    val locationLatitude: Double,
    val locationLongitude: Double
)
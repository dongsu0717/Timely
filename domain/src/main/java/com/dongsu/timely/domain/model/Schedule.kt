package com.dongsu.timely.domain.model

data class Schedule(
    val title: String,
    val startDate: String,
    val lastDate: String,
    val startTime: String,
    val endTime: String,
    val repeatDays: Int,
    val appointmentPlace: String,
    val latitude: Double,
    val longitude: Double,
    val appointmentAlarm: Boolean,
    val appointmentAlarmTime: Int,
    val color: Int
)

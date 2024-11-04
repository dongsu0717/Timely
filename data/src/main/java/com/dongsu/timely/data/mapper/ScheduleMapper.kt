package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.local.entity.ScheduleInfo
import com.dongsu.timely.domain.model.Schedule

object ScheduleMapper{
    fun toEntity(schedule: Schedule): ScheduleInfo {
        return ScheduleInfo(
            title = schedule.title,
            startDate = schedule.startDate,
            lastDate = schedule.lastDate,
            startTime = schedule.startTime,
            endTime = schedule.endTime,
            repeatDays = schedule.repeatDays,
            appointmentPlace = schedule.appointmentPlace,
            latitude = schedule.latitude,
            longitude = schedule.longitude,
            appointmentAlarm = schedule.appointmentAlarm,
            appointmentAlarmTime = schedule.appointmentAlarmTime,
            color = schedule.color
        )
    }
}
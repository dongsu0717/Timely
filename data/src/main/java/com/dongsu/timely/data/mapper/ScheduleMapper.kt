package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.local.room.entity.ScheduleInfo
import com.dongsu.timely.domain.model.Schedule

class ScheduleMapper{
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

    fun toDomain(scheduleInfo: ScheduleInfo): Schedule {
        return Schedule(
            title = scheduleInfo.title,
            startDate = scheduleInfo.startDate,
            lastDate = scheduleInfo.lastDate,
            startTime = scheduleInfo.startTime,
            endTime = scheduleInfo.endTime,
            repeatDays = scheduleInfo.repeatDays,
            appointmentPlace = scheduleInfo.appointmentPlace,
            latitude = scheduleInfo.latitude,
            longitude = scheduleInfo.longitude,
            appointmentAlarm = scheduleInfo.appointmentAlarm,
            appointmentAlarmTime = scheduleInfo.appointmentAlarmTime,
            color = scheduleInfo.color
        )
    }
}
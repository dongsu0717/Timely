package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.request.GroupScheduleRequest
import com.dongsu.timely.data.remote.dto.response.GroupScheduleResponse
import com.dongsu.timely.domain.model.GroupSchedule

object GroupScheduleMapper {
    fun toDto(groupSchedule: GroupSchedule): GroupScheduleRequest {
        return GroupScheduleRequest(
            title = groupSchedule.title,
            startTime = groupSchedule.startTime,
            endTime = groupSchedule.endTime,
            isAlarmEnabled = groupSchedule.isAlarmEnabled,
            alarmBeforeHours = groupSchedule.alarmBeforeHours,
            location = groupSchedule.location,
            locationLatitude = groupSchedule.latitude,
            locationLongitude = groupSchedule.longitude
        )
    }

    fun toDomain(groupScheduleResponse: GroupScheduleResponse): GroupSchedule {
        return GroupSchedule(
            title = groupScheduleResponse.title,
            startTime = groupScheduleResponse.startTime,
            endTime = groupScheduleResponse.endTime,
            isAlarmEnabled = groupScheduleResponse.isAlarmEnabled,
            alarmBeforeHours = groupScheduleResponse.alarmBeforeHours,
            location = groupScheduleResponse.location,
            latitude = groupScheduleResponse.locationLatitude,
            longitude = groupScheduleResponse.locationLongitude
        )
    }

}
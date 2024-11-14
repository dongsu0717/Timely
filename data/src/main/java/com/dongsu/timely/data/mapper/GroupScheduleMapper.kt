package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.request.GroupScheduleRequest
import com.dongsu.timely.data.remote.dto.response.GroupScheduleResponse
import com.dongsu.timely.data.remote.dto.response.TotalGroupScheduleInfoResponse
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.model.GroupScheduleInfo
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo

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
    fun toDomain(groupScheduleResponse: GroupScheduleResponse): GroupScheduleInfo {
        return GroupScheduleInfo(
            scheduleId = groupScheduleResponse.scheduleId,
            groupId = groupScheduleResponse.groupId,
            createUserId = groupScheduleResponse.createUserId,
            title = groupScheduleResponse.title,
            startTime = groupScheduleResponse.startTime,
            endTime = groupScheduleResponse.endTime,
            isAlarmEnabled = groupScheduleResponse.isAlarmEnabled,
            alarmBeforeHours = groupScheduleResponse.alarmBeforeHours,
            location = groupScheduleResponse.location,
            locationLatitude = groupScheduleResponse.locationLatitude,
            locationLongitude = groupScheduleResponse.locationLongitude
        )
    }

    fun toDomainTotal(totalGroupScheduleInfoResponse: TotalGroupScheduleInfoResponse): TotalGroupScheduleInfo {
        return TotalGroupScheduleInfo(
            groupSchedule = toDomain(totalGroupScheduleInfoResponse.groupSchedule),
            isParticipating = totalGroupScheduleInfoResponse.isParticipating
        )
    }

}
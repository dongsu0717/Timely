package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.map.GroupMeetingResponse
import com.dongsu.timely.domain.model.map.GroupMeetingInfo

object GroupMeetingInfoMapper {
    fun toDomain(groupMeetingResponse: GroupMeetingResponse): GroupMeetingInfo {
        return GroupMeetingInfo(
            targetLocation = TargetLocationMapper.toDomain(groupMeetingResponse.targetLocation),
            userMeetingData = groupMeetingResponse.data.map { UserMeetingMapper.toDomain(it) }
        )
    }
}
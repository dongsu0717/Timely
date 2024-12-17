package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.map.GroupMeetingResponse
import com.dongsu.timely.domain.model.map.GroupMeetingInfo

class GroupMeetingInfoMapper(
    private val targetLocationMapper: TargetLocationMapper,
    private val userMeetingMapper: UserMeetingMapper
) {
    fun toDomain(groupMeetingResponse: GroupMeetingResponse): GroupMeetingInfo {
        return GroupMeetingInfo(
            targetLocation = targetLocationMapper.toDomain(groupMeetingResponse.targetLocation),
            userMeetingData = groupMeetingResponse.data.map { userMeetingMapper.toDomain(it) }
        )
    }
}
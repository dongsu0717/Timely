package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.map.GroupMeetingResponse
import com.dongsu.timely.domain.model.map.GroupMeetingInfo
import retrofit2.Response

object ParticipationMemberMapper {
    fun toDomain(groupMeetingResponse: Response<GroupMeetingResponse>): GroupMeetingInfo {
        return GroupMeetingInfo(
            targetLocation = TargetLocationMapper.toDomain(groupMeetingResponse.body()!!.targetLocation),
            userMeetingData = groupMeetingResponse.body()?.data?.map { UserMeetingMapper.toDomain(it) }?: listOf()
        )
    }
}
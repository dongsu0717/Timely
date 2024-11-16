package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.map.UserMeetingResponse
import com.dongsu.timely.domain.model.map.UserMeeting

object UserMeetingMapper {
    fun toDomain(userMeetingResponse: UserMeetingResponse): UserMeeting {
        return UserMeeting(
            user = UserMapper.toDomain(userMeetingResponse.user),
            location = LocationMapper.toDomain(userMeetingResponse.location),
            stateMessage = userMeetingResponse.stateMessage
        )
    }

}

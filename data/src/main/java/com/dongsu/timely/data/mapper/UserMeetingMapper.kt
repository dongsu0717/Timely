package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.map.UserMeetingResponse
import com.dongsu.timely.domain.model.map.UserMeeting
import javax.inject.Inject

class UserMeetingMapper @Inject constructor(
    private val userMapper: UserMapper,
    private val locationMapper: LocationMapper
) {
    fun toDomain(userMeetingResponse: UserMeetingResponse): UserMeeting {
        return UserMeeting(
            user = userMapper.toDomainUser(userMeetingResponse.user),
            location = locationMapper.toDomain(userMeetingResponse.location),
            stateMessage = userMeetingResponse.stateMessage
        )
    }
}

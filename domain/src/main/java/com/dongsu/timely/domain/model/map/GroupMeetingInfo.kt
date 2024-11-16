package com.dongsu.timely.domain.model.map

data class GroupMeetingInfo(
    val targetLocation: TargetLocation,
    val userMeetingData: List<UserMeeting>,
)

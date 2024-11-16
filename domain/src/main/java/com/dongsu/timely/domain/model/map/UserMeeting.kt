package com.dongsu.timely.domain.model.map

data class UserMeeting(
    val user: User,
    val location: LocationInfo,
    val stateMessage: String?
)

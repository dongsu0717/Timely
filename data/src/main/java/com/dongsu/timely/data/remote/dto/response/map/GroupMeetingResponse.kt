package com.dongsu.timely.data.remote.dto.response.map

import com.google.gson.annotations.SerializedName

data class GroupMeetingResponse(
    @SerializedName("targetLocation")
    val targetLocation: TargetLocationResponse,

    @SerializedName("data")
    val data: List<UserMeetingResponse>
)

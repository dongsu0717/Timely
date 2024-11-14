package com.dongsu.timely.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class TotalGroupScheduleInfoResponse(
    @SerializedName("groupSchedule")
    val groupSchedule: GroupScheduleResponse,

    @SerializedName("isParticipating")
    val isParticipating: Boolean
)

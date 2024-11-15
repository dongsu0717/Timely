package com.dongsu.timely.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class InviteCodeResponse(
    @SerializedName("groupId")
    val groupId: Int,

    @SerializedName("inviteCode")
    val inviteCode: String
)

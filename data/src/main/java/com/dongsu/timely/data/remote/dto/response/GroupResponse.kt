package com.dongsu.timely.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class GroupResponse(
    @SerializedName("id")
    val groupId: Int,

    @SerializedName("groupName")
    val groupName: String,

    @SerializedName("userResponse")
    val userResponse: UserResponse,

    @SerializedName("createAt")
    val createAt: String,

    @SerializedName("memberNumber")
    val memberNumber: Int
)

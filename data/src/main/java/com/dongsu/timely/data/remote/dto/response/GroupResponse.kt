package com.dongsu.timely.data.remote.dto.response

import com.dongsu.timely.data.remote.dto.response.map.UserResponse
import com.google.gson.annotations.SerializedName

data class GroupResponse(
    @SerializedName("id")
    val groupId: Int,

    @SerializedName("groupName")
    val groupName: String,

    @SerializedName("hostUser")
    val hostUser: UserResponse,

    @SerializedName("createAt")
    val createAt: String,

    @SerializedName("memberNumber")
    val memberNumber: Int
)

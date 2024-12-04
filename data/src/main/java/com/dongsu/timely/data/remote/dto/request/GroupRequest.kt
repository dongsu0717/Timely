package com.dongsu.timely.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class GroupRequest(
    @SerializedName("groupName")
    val groupName: String,

    @SerializedName("coverColor")
    val coverColor: Int
)

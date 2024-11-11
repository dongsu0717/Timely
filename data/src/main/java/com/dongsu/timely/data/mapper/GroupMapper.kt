package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.request.GroupRequest

object GroupMapper {
    fun toDto(newGroupName: String): GroupRequest {
        return GroupRequest(groupName = newGroupName)
    }
}
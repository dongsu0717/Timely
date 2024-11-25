package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.request.GroupRequest
import com.dongsu.timely.data.remote.dto.response.GroupResponse
import com.dongsu.timely.domain.model.Group

object GroupMapper {
    fun toDto(newGroupName: String): GroupRequest {
        return GroupRequest(groupName = newGroupName)
    }
    fun toDomainGroupList(groupResponseList: List<GroupResponse>): List<Group> {
        return groupResponseList.map { groupResponse ->
            Group(
                groupId = groupResponse.groupId,
                groupName = groupResponse.groupName,
                users = UserMapper.toDomain(groupResponse.hostUser),
                createdAt = groupResponse.createAt,
                memberNumber = groupResponse.memberNumber
            )
        }
    }

}
package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.GroupResponse
import com.dongsu.timely.domain.model.Group

class GroupMapper(
    private val userMapper: UserMapper
) {
    fun toDomainGroupList(groupResponseList: List<GroupResponse>): List<Group> {
        return groupResponseList.map { groupResponse ->
            Group(
                groupId = groupResponse.groupId,
                groupName = groupResponse.groupName,
                users = userMapper.toDomainUser(groupResponse.hostUser),
                groupColor = groupResponse.coverColor,
                createdAt = groupResponse.createAt,
                memberNumber = groupResponse.memberNumber
            )
        }
    }

}
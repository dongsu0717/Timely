package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.GroupResponse
import com.dongsu.timely.domain.model.Group

object GroupMapper {
    fun toDomainGroupList(groupResponseList: List<GroupResponse>): List<Group> {
        return groupResponseList.map { groupResponse ->
            Group(
                groupId = groupResponse.groupId,
                groupName = groupResponse.groupName,
                users = UserMapper.toDomainUser(groupResponse.hostUser),
                groupColor = groupResponse.coverColor,
                createdAt = groupResponse.createAt,
                memberNumber = groupResponse.memberNumber
            )
        }
    }

}
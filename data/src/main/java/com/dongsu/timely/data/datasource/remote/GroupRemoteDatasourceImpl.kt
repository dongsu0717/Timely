package com.dongsu.timely.data.datasource.remote

import com.dongsu.timely.data.mapper.GroupMapper
import com.dongsu.timely.data.mapper.InviteCodeMapper
import com.dongsu.timely.data.remote.api.GroupService
import com.dongsu.timely.data.remote.dto.request.GroupRequest
import com.dongsu.timely.domain.model.Group
import com.dongsu.timely.domain.model.InviteCode
import javax.inject.Inject

class GroupRemoteDatasourceImpl @Inject constructor(
    private val groupService: GroupService,
) : GroupRemoteDatasource {
    override suspend fun createGroup(groupName: String, groupColor: Int) {
        val response = groupService.createGroup(GroupRequest(groupName, groupColor))
        if (!response.isSuccessful) {
            throw Exception("그룹 만들기 Error: ${response.code()}, Message: ${response.message()}")
        }
    }

    override suspend fun fetchMyGroupList(): List<Group> =
        GroupMapper.toDomainGroupList((groupService.fetchMyGroupList()).body() ?: emptyList())

    override suspend fun createInviteCode(groupId: Int): InviteCode =
        try {
            when (val response = groupService.createInviteCode(groupId).body()) {
                null -> InviteCode.EMPTY
                else -> InviteCodeMapper.toDomain(response)
            }
        } catch (e: Exception) {
            throw e
        }

    override suspend fun joinGroup(inviteCode: String) {
        groupService.joinGroup(inviteCode)
    }
}
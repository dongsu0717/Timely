package com.dongsu.timely.data.datasource.remote

import android.util.Log
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.mapper.GroupMapper
import com.dongsu.timely.data.mapper.InviteCodeMapper
import com.dongsu.timely.data.remote.api.GroupService
import com.dongsu.timely.domain.model.Group
import com.dongsu.timely.domain.model.InviteCode
import javax.inject.Inject

class GroupRemoteDatasourceImpl @Inject constructor(
    private val groupService: GroupService
): GroupRemoteDatasource {
    override suspend fun createGroup(groupName: String) {
        val response = groupService.createGroup(GroupMapper.toDto(groupName))
        Log.e("만들어진 그룹", "${response.body()}")
    }

    override suspend fun getGroup(): List<Group>
    = GroupMapper.toDomain((groupService.getMyGroupList()).body() ?: listOf())

    override suspend fun createInviteCode(groupId: Int): TimelyResult<InviteCode> {
        val response = groupService.createInviteCode(groupId)
        return when {
            response.isSuccessful -> {
                val body = response.body()
                if (body != null) {
                    TimelyResult.Success(InviteCodeMapper.toDomain(body))
                } else {
                    TimelyResult.NetworkError(Exception("Response body is null"))
                }
            }
            else -> {
                TimelyResult.NetworkError(Exception("Request failed: ${response.code()} - ${response.message()}"))
            }
        }
    }

    override suspend fun joinGroup(inviteCode: String) {
        groupService.joinGroup(inviteCode)
    }
}
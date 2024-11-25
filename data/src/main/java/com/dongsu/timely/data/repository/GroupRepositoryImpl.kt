package com.dongsu.timely.data.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.datasource.remote.GroupRemoteDatasource
import com.dongsu.timely.domain.model.Group
import com.dongsu.timely.domain.model.InviteCode
import com.dongsu.timely.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupRemoteDatasource: GroupRemoteDatasource,
) : GroupRepository {
    override suspend fun createGroup(groupName: String): TimelyResult<Unit> =
        try {
            groupRemoteDatasource.createGroup(groupName)
            TimelyResult.Success(Unit)
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

    override suspend fun getMyGroupList(): List<Group> = groupRemoteDatasource.getGroup()

    override suspend fun createInviteCode(groupId: Int): TimelyResult<InviteCode> =
        groupRemoteDatasource.createInviteCode(groupId)

    override suspend fun joinGroup(inviteCode: String) = groupRemoteDatasource.joinGroup(inviteCode)

    override suspend fun getGroupInfo() {
    }
}
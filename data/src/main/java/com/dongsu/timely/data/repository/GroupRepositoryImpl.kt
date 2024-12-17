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
    override suspend fun createGroup(groupName: String, groupColor: Int): Result<Unit> = runCatching {
        groupRemoteDatasource.createGroup(groupName, groupColor)
    }
//        try {
//            groupRemoteDatasource.createGroup(groupName, groupColor)
//            TimelyResult.Success(Unit)
//        } catch (e: Exception) {
//            TimelyResult.NetworkError(e)
//        }

    override suspend fun fetchMyGroupList(): TimelyResult<List<Group>> =
        try {
            val myGroupList = groupRemoteDatasource.fetchMyGroupList()
            when {
                myGroupList.isEmpty() -> TimelyResult.Empty
                else -> TimelyResult.Success(myGroupList)
            }
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

    override suspend fun createInviteCode(groupId: Int): TimelyResult<InviteCode> =
        try {
            val inviteCode = groupRemoteDatasource.createInviteCode(groupId)
            when {
                inviteCode == InviteCode.EMPTY -> TimelyResult.Empty
                else -> TimelyResult.Success(inviteCode)
            }
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

    override suspend fun joinGroup(inviteCode: String) = groupRemoteDatasource.joinGroup(inviteCode)

    override suspend fun getGroupInfo() {
    }
}
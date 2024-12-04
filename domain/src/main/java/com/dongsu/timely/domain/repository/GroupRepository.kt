package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Group
import com.dongsu.timely.domain.model.InviteCode

interface GroupRepository {
    suspend fun createGroup(groupName: String, groupColor: Int): TimelyResult<Unit>
    suspend fun fetchMyGroupList(): TimelyResult<List<Group>>
    suspend fun createInviteCode(groupId: Int): TimelyResult<InviteCode>
    suspend fun joinGroup(inviteCode: String)
    suspend fun getGroupInfo()
//    suspend fun deleteGroup()
//    suspend fun updateGroup()
//    suspend fun withdrawGroup()
}
package com.dongsu.timely.data.datasource.remote

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Group
import com.dongsu.timely.domain.model.InviteCode

interface GroupRemoteDatasource {
    suspend fun createGroup(groupName: String)
    suspend fun getGroup(): List<Group>
    suspend fun createInviteCode(groupId: Int): TimelyResult<InviteCode>
    suspend fun joinGroup(inviteCode: String)
}
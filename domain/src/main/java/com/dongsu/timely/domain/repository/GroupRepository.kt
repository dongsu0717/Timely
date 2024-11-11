package com.dongsu.timely.domain.repository

import com.dongsu.timely.domain.model.Group

interface GroupRepository {
    suspend fun createGroup(groupName: String)
    suspend fun getMyGroupList():List<Group>
    suspend fun inviteGroup()
    suspend fun joinGroup()
    suspend fun getGroupInfo()
//    suspend fun deleteGroup()
//    suspend fun updateGroup()
//    suspend fun withdrawGroup()
}
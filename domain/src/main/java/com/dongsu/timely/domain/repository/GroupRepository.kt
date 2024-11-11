package com.dongsu.timely.domain.repository

interface GroupRepository {
    suspend fun createGroup(groupName: String)
    suspend fun getMyGroupList()
    suspend fun inviteGroup()
    suspend fun joinGroup()
    suspend fun getGroupInfo()
//    suspend fun deleteGroup()
//    suspend fun updateGroup()
//    suspend fun withdrawGroup()
}
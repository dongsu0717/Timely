package com.dongsu.timely.data.repository

import com.dongsu.timely.data.datasource.remote.GroupRemoteDatasource
import com.dongsu.timely.domain.model.Group
import com.dongsu.timely.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupRemoteDatasource: GroupRemoteDatasource,
): GroupRepository {
    override suspend fun createGroup(groupName: String) = groupRemoteDatasource.createGroup(groupName)

    override suspend fun getMyGroupList(): List<Group> = groupRemoteDatasource.getGroup()

    override suspend fun inviteGroup() {
    }

    override suspend fun joinGroup() {
    }

    override suspend fun getGroupInfo() {
    }
}
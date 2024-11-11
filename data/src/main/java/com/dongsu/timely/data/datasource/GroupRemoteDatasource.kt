package com.dongsu.timely.data.datasource

import com.dongsu.timely.domain.model.Group

interface GroupRemoteDatasource {
    suspend fun createGroup(groupName: String)
    suspend fun getGroup(): List<Group>
}
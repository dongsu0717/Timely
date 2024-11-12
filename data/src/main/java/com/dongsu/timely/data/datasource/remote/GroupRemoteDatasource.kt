package com.dongsu.timely.data.datasource.remote

import com.dongsu.timely.domain.model.Group

interface GroupRemoteDatasource {
    suspend fun createGroup(groupName: String)
    suspend fun getGroup(): List<Group>
}
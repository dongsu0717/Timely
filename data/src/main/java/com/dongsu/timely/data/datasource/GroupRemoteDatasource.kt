package com.dongsu.timely.data.datasource

interface GroupRemoteDatasource {
    suspend fun createGroup(groupName: String)
}
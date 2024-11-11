package com.dongsu.timely.data.datasource.datasourceImpl

import android.util.Log
import com.dongsu.timely.data.datasource.GroupRemoteDatasource
import com.dongsu.timely.data.mapper.GroupMapper
import com.dongsu.timely.data.remote.api.GroupService
import javax.inject.Inject

class GroupRemoteDatasourceImpl @Inject constructor(
    private val groupService: GroupService
): GroupRemoteDatasource{
    override suspend fun createGroup(groupName: String) {
        val response = groupService.createGroup(GroupMapper.toDto(groupName))
        Log.e("만들어진 그룹", "${response.body()}")
    }
}
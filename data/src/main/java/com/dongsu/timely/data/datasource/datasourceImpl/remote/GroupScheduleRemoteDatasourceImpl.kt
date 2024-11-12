package com.dongsu.timely.data.datasource.datasourceImpl.remote

import com.dongsu.timely.data.datasource.GroupScheduleRemoteDatasource
import com.dongsu.timely.data.mapper.GroupScheduleMapper
import com.dongsu.timely.data.remote.api.GroupScheduleService
import com.dongsu.timely.domain.model.GroupSchedule
import javax.inject.Inject

class GroupScheduleRemoteDatasourceImpl @Inject constructor(
    private val groupScheduleService: GroupScheduleService
): GroupScheduleRemoteDatasource{
    override suspend fun insertSchedule(groupId: Int, groupSchedule: GroupSchedule) {
        groupScheduleService.insertSchedule(groupId,GroupScheduleMapper.toDto(groupSchedule))
    }

    override suspend fun getAllSchedule() {
    }
}






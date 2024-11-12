package com.dongsu.timely.data.datasource.remote

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.mapper.GroupScheduleMapper
import com.dongsu.timely.data.remote.api.GroupScheduleService
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.model.GroupScheduleInfo
import javax.inject.Inject

class GroupScheduleRemoteDatasourceImpl @Inject constructor(
    private val groupScheduleService: GroupScheduleService
) : GroupScheduleRemoteDatasource {

    override suspend fun insertSchedule(groupId: Int, groupSchedule: GroupSchedule) {
        groupScheduleService.insertSchedule(groupId, GroupScheduleMapper.toDto(groupSchedule))
    }

    override suspend fun getAllSchedule(groupId: Int): TimelyResult<List<GroupScheduleInfo>> {
        val response = groupScheduleService.getAllScheduleList(groupId)
        val scheduleList = response.body()?.map { GroupScheduleMapper.toDomain(it) } ?: listOf()
        return TimelyResult.Success(scheduleList)
    }
}







package com.dongsu.timely.data.datasource.remote

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.GroupSchedule

interface GroupScheduleRemoteDatasource {
    suspend fun insertSchedule(groupId: Int, groupSchedule: GroupSchedule)
    suspend fun getAllSchedule(groupId: Int): TimelyResult<List<GroupSchedule>>
}
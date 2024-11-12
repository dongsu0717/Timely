package com.dongsu.timely.data.datasource

import com.dongsu.timely.domain.model.GroupSchedule

interface GroupScheduleRemoteDatasource {
    suspend fun insertSchedule(groupId: Int, groupSchedule: GroupSchedule)
    suspend fun getAllSchedule()
}
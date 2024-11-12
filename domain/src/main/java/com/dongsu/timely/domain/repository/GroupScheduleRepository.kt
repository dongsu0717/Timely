package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.model.GroupScheduleInfo

interface GroupScheduleRepository {
    suspend fun insertSchedule(groupId: Int,groupSchedule: GroupSchedule)
    suspend fun getAllSchedule(groupId: Int): TimelyResult<List<GroupScheduleInfo>>
    suspend fun getSchedule()
    suspend fun participationSchedule()
}
package com.dongsu.timely.domain.repository

import com.dongsu.timely.domain.model.GroupSchedule

interface GroupScheduleRepository {
    suspend fun insertSchedule(groupId: Int,groupSchedule: GroupSchedule)
    suspend fun getAllSchedule()
    suspend fun getSchedule()
    suspend fun participationSchedule()
}
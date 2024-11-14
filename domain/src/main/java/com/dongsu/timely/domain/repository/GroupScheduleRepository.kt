package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.model.ParticipationMember
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo

interface GroupScheduleRepository {
    suspend fun insertSchedule(groupId: Int,groupSchedule: GroupSchedule)
    suspend fun getAllSchedule(groupId: Int): TimelyResult<List<TotalGroupScheduleInfo>>
    suspend fun getSchedule()
    suspend fun participationSchedule(groupId: Int, scheduleId: Int)
    suspend fun cancelParticipationSchedule(groupId: Int, scheduleId: Int)
    suspend fun getParticipationMemberLocation(scheduleId: Int): TimelyResult<List<ParticipationMember>>
}
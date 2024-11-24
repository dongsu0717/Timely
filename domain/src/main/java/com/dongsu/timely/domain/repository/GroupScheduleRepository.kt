package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo
import com.dongsu.timely.domain.model.map.GroupMeetingInfo

interface GroupScheduleRepository {
    suspend fun insertSchedule(groupId: Int,groupSchedule: GroupSchedule)
    suspend fun getAllGroupSchedule(groupId: Int): TimelyResult<List<TotalGroupScheduleInfo>>
    suspend fun getSchedule()
    suspend fun participationSchedule(groupId: Int, scheduleId: Int)
    suspend fun cancelParticipationSchedule(groupId: Int, scheduleId: Int)
    suspend fun getParticipationMemberLocation(scheduleId: Int): TimelyResult<GroupMeetingInfo>
    suspend fun updateStateMessage(scheduleId: Int, stateMessage: String)
}
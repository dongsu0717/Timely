package com.dongsu.timely.data.datasource.remote

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo
import com.dongsu.timely.domain.model.map.GroupMeetingInfo

interface GroupScheduleRemoteDatasource {
    suspend fun insertSchedule(groupId: Int, groupSchedule: GroupSchedule)
    suspend fun getAllSchedule(groupId: Int): TimelyResult<List<TotalGroupScheduleInfo>>
    suspend fun participationSchedule(groupId: Int, scheduleId: Int)
    suspend fun cancelParticipationSchedule(groupId: Int, scheduleId: Int)
    suspend fun getParticipationMemberLocation(scheduleId: Int): TimelyResult<GroupMeetingInfo>
    suspend fun updateStateMessage(scheduleId: Int, stateMessage: String)
}
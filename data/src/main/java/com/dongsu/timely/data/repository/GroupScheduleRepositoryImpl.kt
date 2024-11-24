package com.dongsu.timely.data.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.datasource.remote.GroupScheduleRemoteDatasource
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo
import com.dongsu.timely.domain.model.map.GroupMeetingInfo
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import javax.inject.Inject

class GroupScheduleRepositoryImpl @Inject constructor(
    private val groupScheduleRemoteDatasource: GroupScheduleRemoteDatasource,
) : GroupScheduleRepository {
    override suspend fun insertSchedule(groupId: Int, groupSchedule: GroupSchedule) =
        groupScheduleRemoteDatasource.insertSchedule(groupId, groupSchedule)


    override suspend fun getAllGroupSchedule(groupId: Int): TimelyResult<List<TotalGroupScheduleInfo>> =
        try {
            val response = groupScheduleRemoteDatasource.getAllGroupSchedule(groupId)
            if(response.isEmpty()) TimelyResult.Empty
            TimelyResult.Success(response)
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }


    override suspend fun getSchedule() {}

    override suspend fun participationSchedule(groupId: Int, scheduleId: Int) =
        groupScheduleRemoteDatasource.participationSchedule(groupId, scheduleId)

    override suspend fun cancelParticipationSchedule(groupId: Int, scheduleId: Int) =
        groupScheduleRemoteDatasource.cancelParticipationSchedule(groupId, scheduleId)

    override suspend fun getParticipationMemberLocation(scheduleId: Int): TimelyResult<GroupMeetingInfo> =
        groupScheduleRemoteDatasource.getParticipationMemberLocation(scheduleId)

    override suspend fun updateStateMessage(scheduleId: Int, stateMessage: String) =
        groupScheduleRemoteDatasource.updateStateMessage(scheduleId, stateMessage)

}
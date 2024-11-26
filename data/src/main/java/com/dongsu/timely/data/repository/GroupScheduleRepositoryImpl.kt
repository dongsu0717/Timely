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
    override suspend fun insertSchedule(
        groupId: Int,
        groupSchedule: GroupSchedule
    ): TimelyResult<Unit> =
        try {
            groupScheduleRemoteDatasource.insertSchedule(groupId, groupSchedule)
            TimelyResult.Success(Unit)
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }


    override suspend fun fetchGroupScheduleList(groupId: Int): TimelyResult<List<TotalGroupScheduleInfo>> =
        try {
            val response = groupScheduleRemoteDatasource.fetchGroupScheduleList(groupId)
            when {
                response.isEmpty() -> TimelyResult.Empty
                else -> TimelyResult.Success(response)
            }
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }


    override suspend fun getSchedule() {}

    override suspend fun participationSchedule(groupId: Int, scheduleId: Int) =
        groupScheduleRemoteDatasource.participationSchedule(groupId, scheduleId)

    override suspend fun cancelParticipationSchedule(groupId: Int, scheduleId: Int) =
        groupScheduleRemoteDatasource.cancelParticipationSchedule(groupId, scheduleId)

    override suspend fun fetchGroupMeetingInfo(scheduleId: Int): TimelyResult<GroupMeetingInfo> =
        try {
            TimelyResult.Success(groupScheduleRemoteDatasource.fetchGroupMeetingInfo(scheduleId))
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

    override suspend fun updateStateMessage(scheduleId: Int, stateMessage: String) =
        groupScheduleRemoteDatasource.updateStateMessage(scheduleId, stateMessage)

}
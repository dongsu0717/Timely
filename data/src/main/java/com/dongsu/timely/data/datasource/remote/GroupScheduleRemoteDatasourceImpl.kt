package com.dongsu.timely.data.datasource.remote

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.mapper.GroupScheduleMapper
import com.dongsu.timely.data.mapper.ParticipationMemberMapper
import com.dongsu.timely.data.remote.api.GroupScheduleService
import com.dongsu.timely.data.remote.dto.response.StateMessageResponse
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo
import com.dongsu.timely.domain.model.map.GroupMeetingInfo
import javax.inject.Inject

class GroupScheduleRemoteDatasourceImpl @Inject constructor(
    private val groupScheduleService: GroupScheduleService
) : GroupScheduleRemoteDatasource {

    override suspend fun insertSchedule(groupId: Int, groupSchedule: GroupSchedule) {
        groupScheduleService.insertSchedule(groupId, GroupScheduleMapper.toDto(groupSchedule))
    }

    override suspend fun getAllSchedule(groupId: Int): TimelyResult<List<TotalGroupScheduleInfo>> {
        val response = groupScheduleService.getAllScheduleList(groupId)
        val scheduleList = response.body()?.map { GroupScheduleMapper.toDomainTotal(it) } ?: listOf()
        return TimelyResult.Success(scheduleList)
    }

    override suspend fun participationSchedule(groupId: Int, scheduleId: Int) {
        groupScheduleService.participationSchedule(groupId, scheduleId)
    }

    override suspend fun cancelParticipationSchedule(groupId: Int, scheduleId: Int) {
        groupScheduleService.cancelParticipationSchedule(groupId, scheduleId)
    }

    override suspend fun getParticipationMemberLocation(scheduleId: Int): TimelyResult<GroupMeetingInfo> {
        val response = groupScheduleService.getGroupLocation(scheduleId)
        val groupMeetingInfo = ParticipationMemberMapper.toDomain(response)
        return TimelyResult.Success(groupMeetingInfo)
    }

    override suspend fun updateStateMessage(scheduleId: Int, stateMessage: String) {
        val stateMessageResponse = StateMessageResponse(stateMessage)
        groupScheduleService.updateStateMessage(scheduleId, stateMessageResponse)
    }
}







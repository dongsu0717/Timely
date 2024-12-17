package com.dongsu.timely.data.datasource.remote

import android.util.Log
import com.dongsu.timely.data.mapper.GroupMeetingInfoMapper
import com.dongsu.timely.data.mapper.GroupScheduleMapper
import com.dongsu.timely.data.remote.api.GroupScheduleService
import com.dongsu.timely.data.remote.dto.response.StateMessageResponse
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo
import com.dongsu.timely.domain.model.map.GroupMeetingInfo
import javax.inject.Inject

class GroupScheduleRemoteDatasourceImpl @Inject constructor(
    private val groupScheduleService: GroupScheduleService,
    private val groupScheduleMapper: GroupScheduleMapper,
    private val groupMeetingInfoMapper: GroupMeetingInfoMapper
) : GroupScheduleRemoteDatasource {

    override suspend fun insertSchedule(groupId: Int, groupSchedule: GroupSchedule) {
        val response = groupScheduleService.insertSchedule(groupId, groupScheduleMapper.toDto(groupSchedule))
        if (!response.isSuccessful) {
            throw Exception("그룹 스케쥴 만들기 Error: ${response.code()}, Message: ${response.message()}")
        }
    }

    override suspend fun fetchGroupScheduleList(groupId: Int): List<TotalGroupScheduleInfo> =
        groupScheduleService.fetchGroupScheduleList(groupId).body()
            ?.map { groupScheduleMapper.toDomainTotal(it) } ?: emptyList()

    override suspend fun participationSchedule(groupId: Int, scheduleId: Int) {
        groupScheduleService.participationSchedule(groupId, scheduleId)
    }

    override suspend fun cancelParticipationSchedule(groupId: Int, scheduleId: Int) {
        groupScheduleService.cancelParticipationSchedule(groupId, scheduleId)
    }

    override suspend fun fetchGroupMeetingInfo(scheduleId: Int): GroupMeetingInfo =
        when (val meetingInfo = groupScheduleService.fetchGroupMeetingInfo(scheduleId).body()) {
            null -> {
                Log.e("그룹 미팅 정보 가져오기 (datasource부분)", "null뜬거임 $meetingInfo")
                throw Exception()
            }

            else -> groupMeetingInfoMapper.toDomain(meetingInfo)
        }


    override suspend fun updateStateMessage(scheduleId: Int, stateMessage: String) =
        groupScheduleService.updateStateMessage(scheduleId, StateMessageResponse(stateMessage))
}







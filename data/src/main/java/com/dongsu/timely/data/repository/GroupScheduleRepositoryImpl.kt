package com.dongsu.timely.data.repository

import android.util.Log
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.datasource.remote.GroupScheduleRemoteDatasource
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo
import com.dongsu.timely.domain.model.map.GroupMeetingInfo
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import javax.inject.Inject

class GroupScheduleRepositoryImpl @Inject constructor(
    private val groupScheduleRemoteDatasource: GroupScheduleRemoteDatasource,
): GroupScheduleRepository {
    override suspend fun insertSchedule(groupId: Int,groupSchedule: GroupSchedule)
    = groupScheduleRemoteDatasource.insertSchedule(groupId, groupSchedule)


    override suspend fun getAllSchedule(groupId: Int): TimelyResult<List<TotalGroupScheduleInfo>> {
        val result = groupScheduleRemoteDatasource.getAllSchedule(groupId)
        when(result){
            is TimelyResult.Success -> {
                Log.e("그룹스케줄리스트 넘어가는 정보","${result.resultData}")
            } else -> {}
        }
        return result
    }


    override suspend fun getSchedule() {}

    override suspend fun participationSchedule(groupId: Int, scheduleId: Int)
    = groupScheduleRemoteDatasource.participationSchedule(groupId, scheduleId)

    override suspend fun cancelParticipationSchedule(groupId: Int, scheduleId: Int)
    = groupScheduleRemoteDatasource.cancelParticipationSchedule(groupId, scheduleId)

    override suspend fun getParticipationMemberLocation(scheduleId: Int): TimelyResult<GroupMeetingInfo>
    = groupScheduleRemoteDatasource.getParticipationMemberLocation(scheduleId)

    override suspend fun updateStateMessage(scheduleId: Int, stateMessage: String)
    = groupScheduleRemoteDatasource.updateStateMessage(scheduleId, stateMessage)

}
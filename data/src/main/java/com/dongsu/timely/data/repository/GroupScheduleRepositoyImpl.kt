package com.dongsu.timely.data.repository

import com.dongsu.timely.data.datasource.GroupScheduleRemoteDatasource
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import javax.inject.Inject

class GroupScheduleRepositoryImpl @Inject constructor(
    private val groupScheduleRemoteDatasource: GroupScheduleRemoteDatasource,
): GroupScheduleRepository {
    override suspend fun insertSchedule(groupId: Int,groupSchedule: GroupSchedule)
    = groupScheduleRemoteDatasource.insertSchedule(groupId, groupSchedule)


    override suspend fun getAllSchedule() {}

    override suspend fun getSchedule() {}

    override suspend fun participationSchedule() {}
}
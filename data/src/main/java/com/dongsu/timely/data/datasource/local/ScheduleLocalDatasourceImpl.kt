package com.dongsu.timely.data.datasource.local

import com.dongsu.timely.data.local.room.dao.ScheduleDAO
import com.dongsu.timely.data.mapper.ScheduleMapper
import com.dongsu.timely.domain.model.Schedule
import javax.inject.Inject

class ScheduleLocalDatasourceImpl @Inject constructor(
    private val scheduleDAO: ScheduleDAO,
) : ScheduleLocalDatasource {
    override suspend fun insertSchedule(schedule: Schedule) =
        scheduleDAO.insertSchedule(ScheduleMapper.toEntity(schedule))

    override suspend fun loadAllSchedule(): List<Schedule> =
        scheduleDAO.loadAllSchedule().map { ScheduleMapper.toDomain(it) }
}
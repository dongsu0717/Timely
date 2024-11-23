package com.dongsu.timely.data.datasource.local

import com.dongsu.timely.data.local.room.dao.ScheduleDAO
import com.dongsu.timely.data.mapper.ScheduleMapper
import com.dongsu.timely.domain.model.Schedule
import javax.inject.Inject

class ScheduleLocalDatasourceImpl @Inject constructor (
    private val scheduleDAO: ScheduleDAO
) : ScheduleLocalDatasource {
    override suspend fun insertSchedule(schedule: Schedule) {
        val scheduleEntity = ScheduleMapper.toEntity(schedule.copy(title = schedule.title.ifEmpty { "내일정" }))
        scheduleDAO.insertSchedule(scheduleEntity)
    }

    override suspend fun getAllSchedule(): List<Schedule>
    = scheduleDAO.getAllSchedule().map { ScheduleMapper.toDomain(it) }
}
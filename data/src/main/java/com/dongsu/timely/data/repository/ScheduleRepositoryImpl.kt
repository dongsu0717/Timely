package com.dongsu.timely.data.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.local.dao.ScheduleDAO
import com.dongsu.timely.data.mapper.ScheduleMapper
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.domain.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDAO: ScheduleDAO
): ScheduleRepository {

    override suspend fun insertSchedule(schedule: Schedule) {
        val scheduleEntity = ScheduleMapper.toEntity(schedule.copy(title = schedule.title.ifEmpty { "내일정" }))
        scheduleDAO.insertSchedule(scheduleEntity)
    }

    override suspend fun getAllSchedule(): TimelyResult<MutableList<Schedule>>{
        val scheduleListDomain = scheduleDAO.getAllSchedule().map { ScheduleMapper.toDomain(it) }
        return TimelyResult.Success(scheduleListDomain.toMutableList())
    }
}
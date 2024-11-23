package com.dongsu.timely.data.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.datasource.local.ScheduleLocalDatasource
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.domain.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleLocalDatasource: ScheduleLocalDatasource
): ScheduleRepository {
    override suspend fun insertSchedule(schedule: Schedule)
    = scheduleLocalDatasource.insertSchedule(schedule)

    override suspend fun getAllSchedule(): TimelyResult<List<Schedule>> {
        return try {
            TimelyResult.Success(scheduleLocalDatasource.getAllSchedule())
        } catch (e: Exception) {
            TimelyResult.LocalError(e)
        }
    }
}
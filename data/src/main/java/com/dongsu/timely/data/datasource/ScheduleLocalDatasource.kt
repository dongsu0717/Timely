package com.dongsu.timely.data.datasource

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Schedule

interface ScheduleLocalDatasource {
    suspend fun insertSchedule(schedule:Schedule)
    suspend fun getAllSchedule() : TimelyResult<MutableList<Schedule>>
}
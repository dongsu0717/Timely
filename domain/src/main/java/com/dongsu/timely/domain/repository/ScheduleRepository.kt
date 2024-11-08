package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Schedule

interface ScheduleRepository {
    suspend fun insertSchedule(schedule: Schedule)
    suspend fun getAllSchedule(): TimelyResult<MutableList<Schedule>>
//    suspend fun updateSchedule()
//    suspend fun deleteSchedule()
}

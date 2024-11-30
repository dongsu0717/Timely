package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Schedule

interface ScheduleRepository {
    suspend fun insertSchedule(schedule: Schedule): TimelyResult<Unit>
    suspend fun loadAllSchedule(): TimelyResult<List<Schedule>>
//    suspend fun updateSchedule()
//    suspend fun deleteSchedule()
}

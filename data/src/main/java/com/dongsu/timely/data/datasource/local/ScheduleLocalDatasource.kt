package com.dongsu.timely.data.datasource.local

import com.dongsu.timely.domain.model.Schedule

interface ScheduleLocalDatasource {
    suspend fun insertSchedule(schedule:Schedule)
    suspend fun getAllSchedule() : List<Schedule>
}
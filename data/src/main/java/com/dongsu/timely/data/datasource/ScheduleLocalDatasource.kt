package com.dongsu.timely.data.datasource

interface ScheduleLocalDatasource {
    suspend fun insertSchedule()
    suspend fun getAllSchedule()
}
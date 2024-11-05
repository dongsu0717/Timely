package com.dongsu.timely.data.datasource

interface ScheduleRemoteDatasource {
    suspend fun insertSchedule()
    suspend fun getAllSchedule()
}
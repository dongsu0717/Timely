package com.dongsu.timely.data.datasource.remote

interface ScheduleRemoteDatasource {
    suspend fun insertSchedule()
    suspend fun getAllSchedule()
}
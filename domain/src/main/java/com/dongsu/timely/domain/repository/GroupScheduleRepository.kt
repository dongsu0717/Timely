package com.dongsu.timely.domain.repository

interface GroupScheduleRepository {
    suspend fun insertSchedule()
    suspend fun getAllSchedule()
    suspend fun getSchedule()
    suspend fun participationSchedule()
    suspend fun getMemberLocation()
}
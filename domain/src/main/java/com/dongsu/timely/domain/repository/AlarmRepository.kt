package com.dongsu.timely.domain.repository

interface AlarmRepository {
    suspend fun settingScheduleAlarm()
    suspend fun settingGroupScheduleAlarm()
}
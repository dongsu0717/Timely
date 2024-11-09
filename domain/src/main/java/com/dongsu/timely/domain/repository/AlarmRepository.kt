package com.dongsu.timely.domain.repository

import com.dongsu.timely.domain.model.Schedule

interface AlarmRepository {
    suspend fun settingScheduleAlarm(newSchedule: Schedule)
    suspend fun settingGroupScheduleAlarm()
    suspend fun cancelScheduleAlarm()
}
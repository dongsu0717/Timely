package com.dongsu.timely.data.repository

import com.dongsu.timely.data.receiver.AlarmScheduler
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmScheduler: AlarmScheduler
): AlarmRepository{
    override suspend fun settingScheduleAlarm(newSchedule: Schedule) =
        alarmScheduler.alarmReservation(newSchedule)

    override suspend fun settingGroupScheduleAlarm() {}
}
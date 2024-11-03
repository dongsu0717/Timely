package com.dongsu.timely.domain.repository

import com.dongsu.timely.domain.model.Schedule
import java.util.concurrent.Flow
import javax.inject.Inject

interface ScheduleRepository {
    suspend fun insertSchedule(schedule: Schedule)
//    suspend fun deleteSchedule(scheduleId: String)
//    suspend fun updateSchedule(schedule: Schedule)
//    suspend fun getSchedules(date: LocalDate): List<Schedule>
//    suspend fun searchSchedules(query: String): List<Schedule>
}

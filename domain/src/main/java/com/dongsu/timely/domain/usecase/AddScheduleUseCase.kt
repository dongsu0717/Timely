package com.dongsu.timely.domain.usecase

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.common.LOCAL_DATE_TIME_FORMAT
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.domain.repository.AlarmRepository
import com.dongsu.timely.domain.repository.ScheduleRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(newSchedule: Schedule): TimelyResult<Unit> =
        try {
            scheduleRepository.insertSchedule(newSchedule.copy(title = newSchedule.title.ifEmpty { "내일정" }))
            if(newSchedule.appointmentAlarm
                && convertToLocalDateTime(newSchedule.startDate, newSchedule.startTime).isAfter(LocalDateTime.now())) {
                alarmRepository.settingScheduleAlarm(newSchedule)
            }
            TimelyResult.Success(Unit)
        } catch (e: Exception) {
            TimelyResult.LocalError(e)
        }


    private fun convertToLocalDateTime(date: String, time: String): LocalDateTime {
        val localDate = LocalDateTime.parse("$date $time", DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT))
        return localDate
    }
}
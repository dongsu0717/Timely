package com.dongsu.timely.domain.usecase

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetScheduleShowMapUseCase @Inject constructor(
    private val groupScheduleRepository: GroupScheduleRepository
){
    suspend operator fun invoke(groupId: Int): Int? {
        var scheduleId: Int? = null
            val result = groupScheduleRepository.getAllSchedule(groupId)
            when (result) {
                is TimelyResult.Success -> {
                    val now = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    scheduleId = result.resultData.firstOrNull { schedule ->
                        val startTime = LocalDateTime.parse(schedule.groupSchedule.startTime, formatter)
                        startTime.isAfter(now) && startTime.isBefore(now.plusMinutes(30)) && schedule.isParticipating
                    }?.groupSchedule?.scheduleId
                }
                else -> {}
            }
        return scheduleId
    }
}
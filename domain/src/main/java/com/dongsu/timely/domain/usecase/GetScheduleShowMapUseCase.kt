package com.dongsu.timely.domain.usecase

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PlaceLocation
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetScheduleShowMapUseCase @Inject constructor(
    private val groupScheduleRepository: GroupScheduleRepository
){
    suspend operator fun invoke(groupId: Int): PlaceLocation {
        var scheduleId: Int? = null
        var latitude: Double? = null
        var longitude: Double? = null
            val result = groupScheduleRepository.getAllSchedule(groupId)
            when (result) {
                is TimelyResult.Success -> {
                    val now = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    scheduleId = result.resultData.firstOrNull { schedule ->
                        val startTime = LocalDateTime.parse(schedule.groupSchedule.startTime, formatter)
                        latitude = schedule.groupSchedule.locationLatitude
                        longitude = schedule.groupSchedule.locationLongitude
                        startTime.isAfter(now) && startTime.isBefore(now.plusMinutes(30)) && schedule.isParticipating
                    }?.groupSchedule?.scheduleId
                }
                else -> {}
            }
        return PlaceLocation(scheduleId, latitude, longitude)
    }
}
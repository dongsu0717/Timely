package com.dongsu.timely.domain.usecase.schedule

import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.domain.repository.ScheduleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddScheduleUseCase @Inject constructor (
    private val scheduleRepository: ScheduleRepository
) {
    //operator invoke
//    suspend operator fun invoke(newSchedule: Schedule) {
//        scheduleRepository.insertSchedule(newSchedule)
//    }
    suspend operator fun invoke(newSchedule: Schedule) = scheduleRepository.insertSchedule(newSchedule)
}
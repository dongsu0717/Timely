package com.dongsu.timely.domain.usecase

import com.dongsu.timely.domain.repository.GroupScheduleRepository
import javax.inject.Inject

class ParticipationScheduleUseCase @Inject constructor(
    private val groupScheduleRepository: GroupScheduleRepository
) {
    suspend operator fun invoke(groupId: Int, scheduleId: Int) {
        //다른 그룹 스케줄과 겹치는 시간이 있으면 안됨 - 나중에 추가
        groupScheduleRepository.participationSchedule(groupId, scheduleId)
    }

}
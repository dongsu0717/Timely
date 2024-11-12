package com.dongsu.timely.domain.usecase

import com.dongsu.timely.domain.common.DEFAULT_TITLE
import com.dongsu.timely.domain.model.GroupSchedule
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import javax.inject.Inject

class GroupAddScheduleUseCase @Inject constructor(
    private val groupScheduleRepository: GroupScheduleRepository
){
    suspend operator fun invoke(groupId: Int, groupSchedule: GroupSchedule) {
        val groupScheduleToInsert = groupSchedule.copy(title = groupSchedule.title.ifEmpty { DEFAULT_TITLE })
        groupScheduleRepository.insertSchedule(groupId, groupScheduleToInsert)
    }
}
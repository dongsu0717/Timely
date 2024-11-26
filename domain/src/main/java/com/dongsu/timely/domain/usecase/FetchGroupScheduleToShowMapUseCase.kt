package com.dongsu.timely.domain.usecase

import com.dongsu.timely.common.CommonFormatFile
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.common.TIME_TO_RANGE_GET_LOCATION
import com.dongsu.timely.domain.model.GroupScheduleInfo
import com.dongsu.timely.domain.model.TotalGroupScheduleInfo
import com.dongsu.timely.domain.repository.GroupScheduleRepository
import java.time.LocalDateTime
import javax.inject.Inject

class FetchGroupScheduleToShowMapUseCase @Inject constructor(
    private val groupScheduleRepository: GroupScheduleRepository,
) {
    suspend operator fun invoke(groupId: Int): TimelyResult<GroupScheduleInfo> =
        when (val result = groupScheduleRepository.fetchGroupScheduleList(groupId)) {
            is TimelyResult.Success -> {
                val groupSchedule = checkGroupScheduleShowMap(result.resultData)
                if (groupSchedule != null) {
                    TimelyResult.Success(groupSchedule)
                } else {
                    TimelyResult.Empty
                }
            }
            is TimelyResult.Empty -> TimelyResult.Empty
            else -> TimelyResult.NetworkError(Exception())
        }

    private fun checkGroupScheduleShowMap(groupScheduleList: List<TotalGroupScheduleInfo>): GroupScheduleInfo? {
        val now = LocalDateTime.now()
        return groupScheduleList.firstOrNull { totalGroupSchedule ->
            val startTime = CommonFormatFile.parseToLocalDateTime(totalGroupSchedule.groupSchedule.startTime)
            startTime.isAfter(now) && startTime.isBefore(now.plusMinutes(TIME_TO_RANGE_GET_LOCATION)) && totalGroupSchedule.isParticipating
        }?.groupSchedule
    }
}

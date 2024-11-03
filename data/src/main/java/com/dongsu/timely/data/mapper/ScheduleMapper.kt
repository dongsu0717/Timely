package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.local.entity.ScheduleInfo
import com.dongsu.timely.domain.model.Schedule

object ScheduleMapper{
    fun toEntity(schedule: Schedule): ScheduleInfo {
        return ScheduleInfo(
            title = schedule.title
        )
    }
}
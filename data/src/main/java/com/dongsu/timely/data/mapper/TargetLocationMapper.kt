package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.map.TargetLocationResponse
import com.dongsu.timely.domain.model.map.TargetLocation

object TargetLocationMapper {
    fun toDomain(targetLocationResponse: TargetLocationResponse): TargetLocation {
        return TargetLocation(
            location = targetLocationResponse.location,
            coordinate = LocationMapper.toDomain(targetLocationResponse.coordinate)
        )
    }

}

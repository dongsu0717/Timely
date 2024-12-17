package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.map.LocationResponse
import com.dongsu.timely.domain.model.map.LocationInfo

class LocationMapper {
    fun toDomain(locationResponse: LocationResponse): LocationInfo {
        return LocationInfo(
            latitude = locationResponse.latitude,
            longitude = locationResponse.longitude
        )
    }
}
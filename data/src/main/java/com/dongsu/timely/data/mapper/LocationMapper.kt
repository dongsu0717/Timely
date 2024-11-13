package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.LocationResponse
import com.dongsu.timely.domain.model.UserLocation

object LocationMapper {
    fun toDomain(locationResponse: LocationResponse): UserLocation {
        return UserLocation(
            locationLatitude = locationResponse.locationLatitude,
            locationLongitude = locationResponse.locationLongitude
        )
    }
}
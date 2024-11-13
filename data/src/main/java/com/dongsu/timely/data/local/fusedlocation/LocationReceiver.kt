package com.dongsu.timely.data.local.fusedlocation

import android.location.Location
import com.dongsu.timely.data.remote.api.GroupScheduleService
import com.dongsu.timely.data.remote.dto.request.LocationRequest
import javax.inject.Inject

class LocationReceiver @Inject constructor(
    private val locationProvider: LocationProvider,
    private val groupScheduleService: GroupScheduleService
) {
    fun startReceivingLocation(scheduleId: Int) {
        locationProvider.startLocationUpdates { location ->
            handleLocationUpdate(scheduleId,location)
        }
    }

    fun stopReceivingLocation() {
        locationProvider.stopLocationUpdates()
    }

    private fun handleLocationUpdate(scheduleId: Int,location: Location) {
        val locationRequest = LocationRequest(location.latitude,location.longitude)
        groupScheduleService.updateMyLocation(scheduleId,locationRequest)
    }
}
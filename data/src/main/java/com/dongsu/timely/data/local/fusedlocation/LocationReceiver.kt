package com.dongsu.timely.data.local.fusedlocation

import android.location.Location
import android.util.Log
import com.dongsu.timely.data.remote.api.GroupScheduleService
import com.dongsu.timely.data.remote.dto.request.LocationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    private fun handleLocationUpdate(scheduleId: Int, location: Location) {
        Log.e("내위치 서버에 보내기 직전","${location.latitude}, ${location.longitude}")
        val locationRequest = LocationRequest(location.latitude,location.longitude)
        Log.e("내위치 서버에 보내기 직전 요청확인","$locationRequest")
        CoroutineScope(Dispatchers.IO).launch {
            val response = groupScheduleService.updateMyLocation(scheduleId, locationRequest)
            Log.e("백그라운드 위치전송 정보","${response.body()}, ${response.message()}")
        }
    }
}
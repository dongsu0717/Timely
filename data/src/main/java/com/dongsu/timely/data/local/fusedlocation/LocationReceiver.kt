package com.dongsu.timely.data.local.fusedlocation

import android.location.Location
import android.util.Log
import com.dongsu.timely.data.remote.api.GroupScheduleService
import com.dongsu.timely.data.remote.dto.request.LocationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
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
        CoroutineScope(Dispatchers.IO).launch {
            val locationRequest = LocationRequest(location.latitude, location.longitude)
            try {
                val response = groupScheduleService.updateMyLocation(scheduleId, locationRequest)
                if (response.isSuccessful) {
                    Log.e("백그라운드 위치전송 성공", "${response.body()}, ${response.message()}")
                } else {
                    Log.e("백그라운드 위치전송 실패", "${response.errorBody()}, ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("위치 전송 중 오류 발생", e.message ?: "알 수 없는 오류")
            }
        }
    }
}
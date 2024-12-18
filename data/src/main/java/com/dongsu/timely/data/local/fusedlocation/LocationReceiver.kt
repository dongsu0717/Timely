package com.dongsu.timely.data.local.fusedlocation

import android.location.Location
import android.util.Log
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.remote.api.GroupScheduleService
import com.dongsu.timely.data.remote.dto.request.LocationRequest
import com.dongsu.timely.domain.model.UserLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationReceiver @Inject constructor(
    private val locationProvider: LocationProvider,
    private val groupScheduleService: GroupScheduleService
) {
    fun startReceivingLocation(scheduleId: Int) {
        locationProvider.startLocationUpdates { location ->
            if( scheduleId == -1) {
                Log.e("LocationReceiver","알람리시버에서 내 위치 가져감")
                handleLocationUpdateToAlarmReceiver(location)
            } else {
                Log.e("서버위치전송","시작")
                handleLocationUpdateToServer(scheduleId, location)
            }
        }
    }

    fun stopReceivingLocation() {
        locationProvider.stopLocationUpdates()
    }

    private fun handleLocationUpdateToServer(scheduleId: Int, location: Location) {
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

    private var _myLocation = MutableStateFlow<TimelyResult<UserLocation>>(TimelyResult.Empty)
    val myLocation = _myLocation.asStateFlow()

    private fun handleLocationUpdateToAlarmReceiver(location: Location) {
        _myLocation.value = TimelyResult.Loading
        _myLocation.value = TimelyResult.Success(UserLocation(location.latitude, location.longitude))
    }
}
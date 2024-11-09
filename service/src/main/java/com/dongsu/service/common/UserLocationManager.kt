//package com.dongsu.timely.data.local.tmap
//
//import android.content.Context
//import android.location.Location
//import android.util.Log
//import com.dongsu.timely.common.TimelyResult
//import com.dongsu.timely.domain.model.UserLocation
//import com.skt.tmap.TMapGpsManager
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//
//class UserLocationManager @Inject constructor(
//    @ApplicationContext private val context: Context
//) : TMapGpsManager.OnLocationChangedListener {
//
//    private lateinit var gpsManager: TMapGpsManager
//    private var _currentLocation: TimelyResult<UserLocation> = TimelyResult.Empty //stateFlow로 바꿔줄 거임
//    val currentLocation = _currentLocation
//
//    //여기를 init 묶어버릴까 생각중
//    suspend fun getCurrentLocation() {
//        gpsManager = TMapGpsManager(context).apply {
//            provider = TMapGpsManager.PROVIDER_NETWORK
//            minTime = 3000
//            minDistance = 10f
//            withContext(Dispatchers.Main) {
//                openGps() //이게 Main해서 작동하나보다.. 왜지? - 나중에 확인 해보기
//                Log.e("UserLocationManager 실행", "openGPS 열림")
//            }
//        }
//    }
//
//    override fun onLocationChange(location: Location?) {
//        location?.let {
//            _currentLocation = TimelyResult.Success(UserLocation(it.latitude, it.longitude))
//        }
//        Log.e("UserLocationManager 실행", "위도: ${location?.latitude}, 경도: ${location?.longitude}")
//    }
//
//    fun stopLocationUpdates() {
//        gpsManager.closeGps()
//        Log.e("UserLocationManager 실행", "openGPS 닫힘")
//    }
//}
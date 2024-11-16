package com.dongsu.timely.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dongsu.timely.R
import com.dongsu.timely.common.NOTIFICATION_ROUTE_ID
import com.dongsu.timely.common.NOTIFICATION_SCHEDULE_ID
import com.dongsu.timely.common.NotificationUtils
import com.dongsu.timely.common.PERSONAL_CHANNEL_ID
import com.dongsu.timely.common.PERSONAL_CHANNEL_NAME
import com.dongsu.timely.common.PERSONAL_ROUTE_CHANNEL_DESCRIPTION
import com.dongsu.timely.common.PERSONAL_ROUTE_CHANNEL_ID
import com.dongsu.timely.common.PERSONAL_ROUTE_CHANNEL_NAME
import com.dongsu.timely.common.TIME_TO_ROUTE_SCHEDULE
import com.dongsu.timely.common.TIME_TO_SCHEDULE
import com.dongsu.timely.common.TMAP_MARKET_URL
import com.dongsu.timely.common.TMAP_PACKAGE_NAME
import com.dongsu.timely.common.TMAP_ROUTE_URL
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.local.fusedlocation.LocationReceiver
import com.dongsu.timely.data.remote.api.TMapService
import com.dongsu.timely.domain.model.ScheduleDistanceTime
import com.dongsu.timely.domain.model.UserLocation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var locationReceiver: LocationReceiver
    @Inject lateinit var tMapService: TMapService

    override fun onReceive(context: Context, intent: Intent) {
        val (scheduleTitle, appointmentPlace, destinationLatitude, destinationLongitude, alarmTime)
        = extractAlarmData(intent)
        Log.e("약속 장소 위치", "latitude: $destinationLatitude, longitude: $destinationLongitude")
        if (destinationLatitude != 0.0 || destinationLongitude != 0.0) { //일정 장소 있으면 거리,시간 계산
            CoroutineScope(Dispatchers.IO).launch {
                createRouteNotification(
                    context,
                    scheduleTitle,
                    appointmentPlace,
                    destinationLatitude,
                    destinationLongitude
                )
            }
        } else createNotification(context, scheduleTitle, alarmTime)
    }

    private fun createNotification(context: Context, scheduleTitle: String, alarmTime: Int) {
        Log.e("기본알람","start")
        val notificationManager = NotificationUtils.createNotificationManager(context)
        NotificationUtils.createNotificationChannel(
            notificationManager,
            PERSONAL_CHANNEL_ID,
            PERSONAL_CHANNEL_NAME
        )
        val notification = NotificationUtils.createNotification(
            context = context,
            channelId = PERSONAL_CHANNEL_ID,
            smallIcon = R.drawable.baseline_notifications_active_24,
            title = scheduleTitle,
            message = String.format(Locale.getDefault(), TIME_TO_SCHEDULE, alarmTime),
            priority = NotificationCompat.PRIORITY_HIGH
        ).build()
        notificationManager.notify(NOTIFICATION_SCHEDULE_ID, notification)
    }

    private suspend fun createRouteNotification(
        context: Context,
        scheduleTitle: String,
        appointmentPlace: String,
        destinationLatitude: Double,
        destinationLongitude: Double
    ) {
        Log.e("기본 루트 알람","start")
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            PERSONAL_ROUTE_CHANNEL_ID,
            PERSONAL_ROUTE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = PERSONAL_ROUTE_CHANNEL_DESCRIPTION
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        }
        notificationManager.createNotificationChannel(channel)
        val (startLatitude, startLongitude) = getCurrentLocation()
        val (distance, carTime, walkTime) = getDistanceAndTime(startLatitude, startLongitude, destinationLatitude, destinationLongitude)
        val pendingIntent = createTMapPendingIntent(context, appointmentPlace, destinationLatitude, destinationLongitude, startLatitude, startLongitude)
        val formattedDistance = formatDistance(distance)
        val formattedCarTime = formatTime(carTime)
        val formattedWalkTime = formatTime(walkTime)

        val notification = NotificationCompat.Builder(context, PERSONAL_ROUTE_CHANNEL_ID)
            .setContentTitle(scheduleTitle)
            .setContentText(String.format(Locale.getDefault(), TIME_TO_ROUTE_SCHEDULE,formattedDistance,formattedCarTime,formattedWalkTime))
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        notificationManager.notify(NOTIFICATION_ROUTE_ID, notification)
    }

    private suspend fun getCurrentLocation(): UserLocation {
        var userLocation = UserLocation(0.0, 0.0)
        locationReceiver.startReceivingLocation(-1)
        try {
            val result = locationReceiver.myLocation.first { it is TimelyResult.Success }
            if (result is TimelyResult.Success) {
                userLocation = UserLocation(result.resultData.locationLatitude, result.resultData.locationLongitude)
            }
        } catch (e: Exception) {
            Log.e("위치 정보", "가져오기 실패: ${e.message}")
        }
        return userLocation
    }

    private suspend fun getDistanceAndTime(
        startLatitude: Double,
        startLongitude: Double,
        destinationLatitude: Double,
        destinationLongitude: Double
    ): ScheduleDistanceTime {
        var distance: Long = 0
        var carTime: Long = 0
        var walkTime: Long = 0
        tMapService.getCarResult(startX = startLongitude, startY = startLatitude, endX = destinationLongitude, endY = destinationLatitude).let {
            try {
                if (it.isSuccessful) {
                    distance = it.body()!!.features.first().properties.totalDistance
                    carTime = it.body()!!.features.first().properties.totalTime
                } else {
                    Log.e("tamp 차정보 가져오기 실패",it.message())
                    Log.e("tamp 차정보 가져오기 실패",it.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e("tmap 차정보 가져오기 에러", e.message.toString())
            }
        }
        tMapService.getWalkResult(startX = startLongitude, startY = startLatitude, endX = destinationLongitude, endY = destinationLatitude).let {
            try {
                if (it.isSuccessful) {
                    walkTime = it.body()!!.features.first().properties.totalTime
                } else {
                    Log.e("tamp 걸어서정보 가져오기 실패",it.message())
                    Log.e("tamp 걸어서정보 가져오기 실패",it.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e("tmap 걸어서 정보 가져오기 에러", e.message.toString())
            }
        }
        return ScheduleDistanceTime(distance, carTime, walkTime)
    }

    private fun createTMapPendingIntent(
        context: Context,
        appointmentPlace: String,
        destinationLatitude: Double,
        destinationLongitude: Double,
        startLatitude: Double,
        startLongitude: Double
    ): PendingIntent {
        val isTMapInstalled = try {
            context.packageManager.getPackageInfo(TMAP_PACKAGE_NAME, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

        val tMapIntent = if (isTMapInstalled) {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    String.format(
                        TMAP_ROUTE_URL,
                        appointmentPlace,
                        destinationLongitude,
                        destinationLatitude,
                        startLongitude,
                        startLatitude
                    )
                )
                `package` = TMAP_PACKAGE_NAME
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        } else {
            Intent(Intent.ACTION_VIEW, Uri.parse(String.format(TMAP_MARKET_URL, TMAP_ROUTE_URL)))
        }
        return PendingIntent.getActivity(
            context,
            0,
            tMapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun extractAlarmData(intent: Intent): ScheduleAlarmData {
        val scheduleTitle = intent.getStringExtra("scheduleTitle") ?: ""
        val appointmentPlace = intent.getStringExtra("appointmentPlace") ?: ""
        val destinationLatitude = intent.getDoubleExtra("latitude", 0.0)
        val destinationLongitude = intent.getDoubleExtra("longitude", 0.0)
        val alarmTime = intent.getIntExtra("alarmTime", 60)
        return ScheduleAlarmData(
            scheduleTitle,
            appointmentPlace,
            destinationLatitude,
            destinationLongitude,
            alarmTime
        )
    }

    data class ScheduleAlarmData(
        val scheduleTitle: String,
        val appointmentPlace: String,
        val destinationLatitude: Double,
        val destinationLongitude: Double,
        val alarmTime: Int
    )

    private fun formatDistance(distanceInMeters: Long): String {
        val distanceInKm = distanceInMeters / 1000.0
        return String.format(Locale.getDefault(), "%.1f km", distanceInKm)
    }

    private fun formatTime(timeInSeconds: Long): String {
        val hours = timeInSeconds / 3600
        val minutes = (timeInSeconds % 3600) / 60
        return if (hours > 0) {
            String.format(Locale.getDefault(), "%02d시간 %02d분", hours, minutes)
        } else {
            String.format(Locale.getDefault(), "%02d분", minutes)
        }
    }
}
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
import com.dongsu.timely.common.TIME_TO_SCHEDULE
import com.dongsu.timely.common.TMAP_MARKET_URL
import com.dongsu.timely.common.TMAP_PACKAGE_NAME
import com.dongsu.timely.common.TMAP_ROUTE_URL
import com.dongsu.timely.domain.model.ScheduleDistanceTime
import com.dongsu.timely.domain.model.UserLocation
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val (scheduleTitle, appointmentPlace, destinationLatitude, destinationLongitude, alarmTime) = extractAlarmData(
            intent
        ) //구조 분해 선언 처음 적용
        Log.e("약속 장소 위치", "latitude: $destinationLatitude, longitude: $destinationLongitude")

        if (destinationLatitude != 0.0 || destinationLongitude != 0.0) { //일정 장소 있으면 거리,시간 계산
            createRouteNotification(
                context,
                scheduleTitle,
                appointmentPlace,
                destinationLatitude,
                destinationLongitude
            )
        } else createNotification(context, scheduleTitle, alarmTime)
    }

    private fun createNotification(context: Context, scheduleTitle: String, alarmTime: Int) {
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

    private fun createRouteNotification(
        context: Context,
        scheduleTitle: String,
        appointmentPlace: String,
        destinationLatitude: Double,
        destinationLongitude: Double
    ) {
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
//        val (startLatitude, startLongitude) = getCurrentLocation(context)
//        val (distance, carTime, walkTime) = getDistanceAndTime(startLatitude, startLongitude, destinationLatitude, destinationLongitude)

//        val pendingIntent = createTMapPendingIntent(context, appointmentPlace, destinationLatitude, destinationLongitude, startLatitude, startLongitude)
//
//        val formattedDistance = formatDistance(distance)
//        val formattedCarTime = formatTime(carTime)
//        val formattedWalkTime = formatTime(walkTime)

        val notification = NotificationCompat.Builder(context, PERSONAL_ROUTE_CHANNEL_ID)
            .setContentTitle(scheduleTitle)
//            .setContentText(String.format(Locale.getDefault(), TIME_TO_ROUTE_SCHEDULE,formattedDistance,formattedCarTime,formattedWalkTime))
//            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
            .build()
        notificationManager.notify(NOTIFICATION_ROUTE_ID, notification)
    }

    private fun getCurrentLocation(context: Context): UserLocation {
        var startLatitude = 0.0
        var startLongitude = 0.0
//        userLocationManager.getCurrentLocation() //이게 실행되는 순간 getDistanceAndTime도 실행된다..
//        userLocationManager.currentLocation.also { location ->
//            when (location) {
//                is TimelyResult.Success -> {
//                    startLatitude = location.resultData.latitude
//                    startLongitude = location.resultData.longitude
//                    Log.e("내위치 가져옴", "위도: $startLatitude, 경도: $startLongitude")
//                    userLocationManager.stopLocationUpdates()
//                }
//                is TimelyResult.Loading -> {
//                    showToast(context,LOADING_GET_LOCATION) //나중에 삭제할 애들
//                    Log.e("위치 정보", LOADING_GET_LOCATION)
//                }
//                is TimelyResult.Error -> {
//                    showToast(context,ERROR_GET_LOCATION) //나중에 삭제할 애들
//                    Log.e("위치 정보",ERROR_GET_LOCATION)
//                }
//                else ->{}
//            }
//        }
        return UserLocation(startLatitude, startLongitude)
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
//        tMapService.getCarResult(startX = startLongitude, startY = startLatitude, endX = destinationLongitude, endY = destinationLatitude).let {
//            if (it.isSuccessful) {
//                distance = it.body()!!.features.first().properties.totalDistance
//                carTime = it.body()!!.features.first().properties.totalTime
//            } else {
//                Log.e("실패",it.message())
//                Log.e("실패",it.errorBody().toString())
//            }
//        }
//        tMapService.getWalkResult(startX = startLongitude, startY = startLatitude, endX = destinationLongitude, endY = destinationLatitude).let {
//            if (it.isSuccessful) {
//                walkTime = it.body()!!.features.first().properties.totalTime
//            } else {
//                Log.e("실패",it.message())
//                Log.e("실패",it.errorBody().toString())
//            }
//        }
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
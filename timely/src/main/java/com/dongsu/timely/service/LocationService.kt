package com.dongsu.timely.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.dongsu.timely.common.CHANNEL_ID
import com.dongsu.timely.common.FOREGROUND_SERVICE_BODY
import com.dongsu.timely.common.FOREGROUND_SERVICE_CHANNEL_ID
import com.dongsu.timely.common.FOREGROUND_SERVICE_CHANNEL_NAME
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.common.GROUP_NAME
import com.dongsu.timely.common.NOTIFICATION_FOREGROUND_LOCATION_ID
import com.dongsu.timely.common.NotificationUtils
import com.dongsu.timely.common.SCHEDULE_ID
import com.dongsu.timely.common.SCHEDULE_START_TIME
import com.dongsu.timely.common.SCHEDULE_TITLE
import com.dongsu.timely.data.local.fusedlocation.LocationReceiver
import com.dongsu.timely.presentation.ui.main.TimelyActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {
    @Inject lateinit var locationReceiver: LocationReceiver

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val (groupId, groupName, scheduleId, scheduleTitle, scheduleStartTime, channelId) = extractAlarmData(intent)
        Log.e("서비스로 넘어온 데이터","groupId: $groupId, groupName: $groupName, scheduleId: $scheduleId, scheduleTitle: $scheduleTitle, scheduleStartTime: $scheduleStartTime, channelId: $channelId")

        if(groupId != -1 && groupId != null && scheduleId != -1 && scheduleId != null){
            locationReceiver.startReceivingLocation(scheduleId.toInt())
            startForegroundService(groupId,groupName,scheduleId,scheduleTitle)
        } else Log.e("서비스 실행x", "groupId: $groupId scheduleId: $scheduleId")

        //왜 밑에 작동을 안할까..?
        val currentTime = LocalDateTime.now()
        val scheduleTime = LocalDateTime.parse(scheduleStartTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        if (scheduleTime.isBefore(currentTime) || scheduleTime.isEqual(currentTime)) {
            Log.e("서비스 종료", "The scheduled time has passed. Stopping the service.")
            stopSelf()
            return START_NOT_STICKY
        }

        return START_STICKY
    }

    private fun startForegroundService(groupId:Int, groupName:String?, scheduleId:Int, scheduleTitle:String?) {
        val notificationManager = NotificationUtils.createNotificationManager(this)
        NotificationUtils.createNotificationChannel(
            notificationManager,
            FOREGROUND_SERVICE_CHANNEL_ID,
            FOREGROUND_SERVICE_CHANNEL_NAME,
        )
        val pendingIntent = NotificationUtils.createPendingIntent(
            this,
            TimelyActivity::class.java,
            groupId,
            mapOf(GROUP_ID to groupId, SCHEDULE_ID to scheduleId)
        )
        val notification = NotificationUtils.createNotification(
            context = this,
            channelId = FOREGROUND_SERVICE_CHANNEL_ID,
            smallIcon = android.R.drawable.ic_menu_mylocation,
            title = groupName?:"위치 추적",
            message = String.format(Locale.getDefault(), FOREGROUND_SERVICE_BODY, scheduleTitle),
            pendingIntent = pendingIntent,
            autoCancel = false
        ).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_FOREGROUND_LOCATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(NOTIFICATION_FOREGROUND_LOCATION_ID, notification)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        locationReceiver.stopReceivingLocation()
    }

    private fun extractAlarmData(intent: Intent?): LocationServiceData {
        val groupId = intent?.getIntExtra(GROUP_ID, -1)
        val groupName = intent?.getStringExtra(GROUP_NAME)
        val scheduleId = intent?.getIntExtra(SCHEDULE_ID, -1)
        val scheduleTitle = intent?.getStringExtra(SCHEDULE_TITLE)
        val scheduleStartTime = intent?.getStringExtra(SCHEDULE_START_TIME)
        val channelId = intent?.getStringExtra(CHANNEL_ID)
        return LocationServiceData(groupId, groupName, scheduleId, scheduleTitle, scheduleStartTime, channelId)
    }

    data class LocationServiceData(
        val groupId: Int?,
        val groupName: String?,
        val scheduleId: Int?,
        val scheduleTitle: String?,
        val scheduleStartTime: String?,
        val channelId: String?
    )
}
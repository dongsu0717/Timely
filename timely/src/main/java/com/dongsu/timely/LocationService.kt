package com.dongsu.timely

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dongsu.timely.common.CHANNEL_ID
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.common.GROUP_NAME
import com.dongsu.timely.common.SCHEDULE_ID
import com.dongsu.timely.common.SCHEDULE_START_TIME
import com.dongsu.timely.common.SCHEDULE_TITLE
import com.dongsu.timely.data.local.fusedlocation.LocationReceiver
import com.dongsu.timely.presentation.ui.main.TimelyActivity
import com.dongsu.timely.service.common.FOREGROUND_SERVICE_BODY
import com.dongsu.timely.service.common.FOREGROUND_SERVICE_CHANNEL_ID
import com.dongsu.timely.service.common.FOREGROUND_SERVICE_CHANNEL_NAME
import com.dongsu.timely.service.common.NOTIFICATION_FOREGROUND_LOCATION_ID
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {
    @Inject lateinit var locationReceiver: LocationReceiver

    override fun onCreate() {
        super.onCreate()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val groupId = intent?.getIntExtra(GROUP_ID, -1)
        val groupName = intent?.getStringExtra(GROUP_NAME)
        val scheduleId = intent?.getIntExtra(SCHEDULE_ID, -1)
        val scheduleTitle = intent?.getStringExtra(SCHEDULE_TITLE)
        val scheduleStartTime = intent?.getStringExtra(SCHEDULE_START_TIME)
        val channelId = intent?.getStringExtra(CHANNEL_ID)
        Log.e("서비스로 넘어온 데이터","groupId: $groupId, groupName: $groupName, scheduleId: $scheduleId, scheduleTitle: $scheduleTitle, scheduleStartTime: $scheduleStartTime, channelId: $channelId")
        if(groupId != -1 && scheduleId != -1){
            locationReceiver.startReceivingLocation(scheduleId!!.toInt())
            startForegroundService(groupId!!,groupName,scheduleId,scheduleTitle)
        }
        return START_STICKY
    }

    private fun startForegroundService(groupId:Int, groupName:String?, scheduleId:Int, scheduleTitle:String?) {
        val notificationManager = createNotificationManager()
        createChannel(notificationManager, FOREGROUND_SERVICE_CHANNEL_ID, FOREGROUND_SERVICE_CHANNEL_NAME)
        val pendingIntent = createPendingIntent(groupId,scheduleId)
        val notification = createNotification(pendingIntent,groupName,scheduleTitle)
        startForeground(NOTIFICATION_FOREGROUND_LOCATION_ID, notification)
    }

    private fun createNotificationManager(): NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    private fun createChannel(manager:NotificationManager, channelId: String, channelName: String) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
    }
    private fun createPendingIntent(groupId: Int, scheduleId:Int): PendingIntent {
        val target = Intent(this, TimelyActivity::class.java).apply {
            putExtra(GROUP_ID, groupId)
            putExtra(SCHEDULE_ID, scheduleId)
        }
        return PendingIntent.getActivity(
            this,
            scheduleId,
            target,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    private fun createNotification(pendingIntent: PendingIntent, groupName: String?, scheduleTitle: String?)
            = NotificationCompat.Builder(this, FOREGROUND_SERVICE_CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_menu_mylocation)
        .setContentTitle(groupName)
        .setContentText(String.format(Locale.getDefault(), FOREGROUND_SERVICE_BODY, scheduleTitle))
        .setContentIntent(pendingIntent)
        .build()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        locationReceiver.stopReceivingLocation()
    }
}
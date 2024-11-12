package com.dongsu.timely

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.common.GROUP_NAME
import com.dongsu.timely.presentation.ui.main.TimelyActivity
import com.dongsu.timely.service.common.FCM_APPOINTMENT_CHANNEL_ID
import com.dongsu.timely.service.common.FCM_APPOINTMENT_CHANNEL_NAME
import com.dongsu.timely.service.common.FCM_CREATE_SCHEDULE_BODY
import com.dongsu.timely.service.common.FCM_CREATE_SCHEDULE_CHANNEL_ID
import com.dongsu.timely.service.common.FCM_CREATE_SCHEDULE_CHANNEL_NAME
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Locale

class TimelyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("서버에서 온 fcm", message.data.toString())
        val groupId = message.data["groupId"]?.toInt()
        val groupName = message.data["groupName"].toString()
        val scheduleTitle = message.data["scheduleTitle"].toString()
        val scheduleStartTime = message.data["scheduleStartTime"].toString()
        val channelId = message.data["channelId"].toString()

        groupId?.let {
            when (channelId) {
                FCM_CREATE_SCHEDULE_CHANNEL_ID -> {
                    createSchedulePushMessage(groupId, groupName, scheduleTitle)
                }

                FCM_APPOINTMENT_CHANNEL_ID -> {
                    createAppointmentPushMessage(groupId, groupName, scheduleTitle, scheduleStartTime)
                }
            }
        }
    }

    private fun createSchedulePushMessage(groupId: Int, groupName: String, scheduleTitle: String) {
        val notificationManager = createNotificationManager()
        createChannel(notificationManager,FCM_CREATE_SCHEDULE_CHANNEL_ID, FCM_CREATE_SCHEDULE_CHANNEL_NAME)
        val pendingIntent = createPendingIntent(groupId,groupName)
        val notification = createNotification(pendingIntent,groupName,scheduleTitle)
        notificationManager.notify(groupId, notification)
    }
    private fun createAppointmentPushMessage(groupId: Int, groupName: String, scheduleTitle: String, scheduleStartTime: String) {
        val notificationManager = createNotificationManager()
        createChannel(notificationManager, FCM_APPOINTMENT_CHANNEL_ID, FCM_APPOINTMENT_CHANNEL_NAME)
    }

    private fun createNotificationManager(): NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    private fun createChannel(manager:NotificationManager, channelId: String, channelName: String){
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
    }
    private fun createPendingIntent(groupId: Int, groupName: String): PendingIntent {
        val target = Intent(this, TimelyActivity::class.java).apply {
            putExtra(GROUP_ID, groupId)
            putExtra(GROUP_NAME, groupName)
//            putExtra(GROUP_SCHEDULE, GROUP_SCHEDULE)
        }
        return PendingIntent.getActivity(
            this,
            groupId,
            target,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    private fun createNotification(pendingIntent: PendingIntent, groupName: String, scheduleTitle: String)
    = NotificationCompat.Builder(this, FCM_CREATE_SCHEDULE_CHANNEL_ID)
        .setSmallIcon(com.dongsu.service.R.drawable.baseline_notifications_active_24)
        .setContentTitle(groupName)
        .setContentText(String.format(Locale.getDefault(), FCM_CREATE_SCHEDULE_BODY, scheduleTitle))
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}
package com.dongsu.timely.fcm

import android.util.Log
import com.dongsu.timely.R
import com.dongsu.timely.common.CHANNEL_ID
import com.dongsu.timely.common.FCM_APPOINTMENT_BODY
import com.dongsu.timely.common.FCM_APPOINTMENT_CHANNEL_ID
import com.dongsu.timely.common.FCM_APPOINTMENT_CHANNEL_NAME
import com.dongsu.timely.common.FCM_APPOINTMENT_TITLE
import com.dongsu.timely.common.FCM_CREATE_SCHEDULE_BODY
import com.dongsu.timely.common.FCM_CREATE_SCHEDULE_CHANNEL_ID
import com.dongsu.timely.common.FCM_CREATE_SCHEDULE_CHANNEL_NAME
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.common.GROUP_NAME
import com.dongsu.timely.common.NOTIFICATION_FCM_APPOINTMENT_ALARM_ID
import com.dongsu.timely.common.NOTIFICATION_FCM_CREATE_SCHEDULE_ID
import com.dongsu.timely.common.NotificationUtils
import com.dongsu.timely.common.SCHEDULE_TITLE
import com.dongsu.timely.presentation.ui.main.TimelyActivity
import com.dongsu.timely.work.setWorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Locale

class TimelyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e("서버에서 온 fcm", message.data.toString())

        val groupId = message.data[GROUP_ID]?.toInt()
        val groupName = message.data[GROUP_NAME].toString()
        val scheduleTitle = message.data[SCHEDULE_TITLE].toString()
        val channelId = message.data[CHANNEL_ID].toString()
        groupId?.let {
            when (channelId) {
                FCM_CREATE_SCHEDULE_CHANNEL_ID -> {
                    Log.e("FCM일정 만들어짐","FCM일정만들어짐")
                    createSchedulePushMessage(groupId, groupName, scheduleTitle)
                }
                FCM_APPOINTMENT_CHANNEL_ID -> {
                    Log.e("FCM일정 시간 됨","FCM일정 시간 됐음")
                    setWorkManager(message)
                    AppointmentAlarmPushMessage(groupName, scheduleTitle)
                }
            }
//            setWorkManager(message) //바로 울리기 테스트용
        }
    }

    private fun createSchedulePushMessage(groupId: Int, groupName: String, scheduleTitle: String) {
        val notificationManager = NotificationUtils.createNotificationManager(this)
        NotificationUtils.createNotificationChannel(
            notificationManager,
            FCM_CREATE_SCHEDULE_CHANNEL_ID,
            FCM_CREATE_SCHEDULE_CHANNEL_NAME
        )
        val pendingIntent = NotificationUtils.createPendingIntent(
            this,
            TimelyActivity::class.java,
            groupId,
            mapOf(GROUP_ID to groupId, GROUP_NAME to groupName)
        )
        val notification = NotificationUtils.createNotification(
            context = this,
            channelId = FCM_CREATE_SCHEDULE_CHANNEL_ID,
            smallIcon = R.drawable.baseline_notifications_active_24,
            title = groupName,
            message = String.format(Locale.getDefault(), FCM_CREATE_SCHEDULE_BODY, scheduleTitle),
            pendingIntent = pendingIntent
        ).build()
        notificationManager.notify(NOTIFICATION_FCM_CREATE_SCHEDULE_ID, notification)
    }

    private fun AppointmentAlarmPushMessage(groupName: String, scheduleTitle: String) {
        val notificationManager = NotificationUtils.createNotificationManager(this)
        NotificationUtils.createNotificationChannel(
            notificationManager,
            FCM_APPOINTMENT_CHANNEL_ID,
            FCM_APPOINTMENT_CHANNEL_NAME
        )
        val pendingIntent = NotificationUtils.createPendingIntent(
            this,
            TimelyActivity::class.java,
            0,
            mapOf(GROUP_ID to 0, GROUP_NAME to groupName)
        )
        val notification = NotificationUtils.createNotification(
            context = this,
            channelId = FCM_APPOINTMENT_CHANNEL_ID,
            smallIcon = R.drawable.baseline_notifications_active_24,
            title = String.format(Locale.getDefault(), FCM_APPOINTMENT_TITLE, scheduleTitle),
            message = FCM_APPOINTMENT_BODY,
            pendingIntent = pendingIntent
        ).build()
        notificationManager.notify(NOTIFICATION_FCM_APPOINTMENT_ALARM_ID,notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

}
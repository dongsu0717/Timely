package com.dongsu.timely

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.dongsu.timely.common.CHANNEL_ID
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.common.GROUP_NAME
import com.dongsu.timely.common.SCHEDULE_ID
import com.dongsu.timely.common.SCHEDULE_START_TIME
import com.dongsu.timely.common.SCHEDULE_TITLE
import com.dongsu.timely.presentation.ui.main.TimelyActivity
import com.dongsu.timely.service.common.FCM_APPOINTMENT_BODY
import com.dongsu.timely.service.common.FCM_APPOINTMENT_CHANNEL_ID
import com.dongsu.timely.service.common.FCM_APPOINTMENT_CHANNEL_NAME
import com.dongsu.timely.service.common.FCM_CREATE_SCHEDULE_BODY
import com.dongsu.timely.service.common.FCM_CREATE_SCHEDULE_CHANNEL_ID
import com.dongsu.timely.service.common.FCM_CREATE_SCHEDULE_CHANNEL_NAME
import com.dongsu.timely.service.common.NOTIFICATION_FCM_APPOINTMENT_ALARM_ID
import com.dongsu.timely.service.common.NOTIFICATION_FCM_CREATE_SCHEDULE_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

class TimelyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e("서버에서 온 fcm", message.data.toString())

        val groupId = message.data[GROUP_ID]?.toInt()
        val groupName = message.data[GROUP_NAME].toString()
        val scheduleTitle = message.data[SCHEDULE_TITLE].toString()
        val scheduleStartTime = message.data[SCHEDULE_START_TIME].toString()
        val channelId = message.data[CHANNEL_ID].toString()
        groupId?.let {
            when (channelId) {
                FCM_CREATE_SCHEDULE_CHANNEL_ID -> {
                    Log.e("FCM일정 만들어짐","FCM일정만들어짐")
                    createSchedulePushMessage(groupId, groupName, scheduleTitle)
                }
                FCM_APPOINTMENT_CHANNEL_ID -> {
                    Log.e("FCM일정 시간 됨","FCM일정 시간 됐음")
//                    setWorkManager(message)
                    AppointmentAlarmPushMessage(groupName, scheduleTitle)
                }
            }
            setWorkManager(message)
        }
    }

    private fun createSchedulePushMessage(groupId: Int, groupName: String, scheduleTitle: String) {
        val notificationManager = createNotificationManager()
        createChannel(notificationManager,FCM_CREATE_SCHEDULE_CHANNEL_ID, FCM_CREATE_SCHEDULE_CHANNEL_NAME)
        val pendingIntent = createPendingIntent(groupId,groupName)
        val notification = createNotification(pendingIntent,groupName,scheduleTitle)
        notificationManager.notify(NOTIFICATION_FCM_CREATE_SCHEDULE_ID, notification)
    }

    private fun AppointmentAlarmPushMessage(groupName: String, scheduleTitle: String) {
        val notificationManager = createNotificationManager()
        createChannel(notificationManager, FCM_APPOINTMENT_CHANNEL_ID, FCM_APPOINTMENT_CHANNEL_NAME)
        val notification = createAppointmentNotification(groupName,scheduleTitle)
        notificationManager.notify(NOTIFICATION_FCM_APPOINTMENT_ALARM_ID, notification)
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
    private fun createAppointmentNotification(groupName: String, scheduleTitle: String)
            = NotificationCompat.Builder(this, FCM_CREATE_SCHEDULE_CHANNEL_ID)
        .setSmallIcon(com.dongsu.service.R.drawable.baseline_notifications_active_24)
        .setContentTitle(groupName)
        .setContentText(String.format(Locale.getDefault(), FCM_APPOINTMENT_BODY, scheduleTitle))
        .setAutoCancel(true)
        .build()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun setWorkManager(message: RemoteMessage){
        Log.e("워크매니저 세팅","워크매니저세팅부분호출")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val workData = workDataOf(
            GROUP_ID to message.data[GROUP_ID]?.toInt(),
            GROUP_NAME to message.data[GROUP_NAME].toString(),
            SCHEDULE_TITLE to message.data[SCHEDULE_TITLE].toString(),
            SCHEDULE_START_TIME to message.data[SCHEDULE_START_TIME].toString(),
            CHANNEL_ID to message.data[CHANNEL_ID].toString(),
            SCHEDULE_ID to message.data[SCHEDULE_ID]?.toInt()
        )
        val scheduleStartTimeStr = message.data[SCHEDULE_START_TIME].toString()
        val initialDelay = calculateInitialDelay(scheduleStartTimeStr, 30)
        val workRequest: WorkRequest = OneTimeWorkRequestBuilder<LocationWorker>()
            .setConstraints(constraints)
//            .setInitialDelay(initialDelay, TimeUnit.MINUTES) //실제 약속시간시간 30분전에 울리는거
            .setInitialDelay(15,TimeUnit.SECONDS) //실험용 15초뒤에 작동 시키기
            .setInputData(workData)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
    private fun calculateInitialDelay(startTimeStr: String, minutesBefore: Long): Long {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val scheduleStartTime = LocalDateTime.parse(startTimeStr, formatter)
        val currentTime = LocalDateTime.now()

        val delayDuration = Duration.between(currentTime, scheduleStartTime.minusMinutes(minutesBefore))
        return if (delayDuration.isNegative) 0 else delayDuration.toMinutes()
    }
}
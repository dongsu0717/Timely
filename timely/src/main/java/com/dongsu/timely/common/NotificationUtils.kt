package com.dongsu.timely.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

object NotificationUtils {
    fun createNotificationManager(context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotificationChannel(
        manager: NotificationManager,
        channelId: String,
        channelName: String,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ) {
        val channel = NotificationChannel(channelId, channelName, importance)
        manager.createNotificationChannel(channel)
    }

    fun createPendingIntent(
        context: Context,
        targetClass: Class<*>,
        requestCode: Int,
        extras: Map<String, Any>
    ): PendingIntent {
        val intent = Intent(context, targetClass).apply {
            extras.forEach { (key, value) ->
                when (value) {
                    is Int -> putExtra(key, value)
                    is String -> putExtra(key, value)
                }
            }
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun createNotification(
        context: Context,
        channelId: String,
        smallIcon: Int,
        title: String,
        message: String,
        pendingIntent: PendingIntent? = null,
        autoCancel: Boolean = true,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(autoCancel)
            .setPriority(priority)
            .apply {
                pendingIntent?.let { setContentIntent(it) }
            }
    }
}
package com.dongsu.service.common

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.dongsu.timely.domain.model.Schedule
import com.dongsu.timely.domain.repository.AlarmRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): AlarmRepository {

    override suspend fun settingScheduleAlarm(alarmSchedule: Schedule) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmTime = getAlarmTime(alarmSchedule)
        val pendingIntent = createPendingIntent(alarmSchedule)
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.time, pendingIntent)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent) //5초뒤 울리는 테스트용
    }

    private fun getAlarmTime(alarmSchedule: Schedule): Date {
        val calendar = Calendar.getInstance()
        val scheduleTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            .parse("${alarmSchedule.startDate} ${alarmSchedule.startTime}")
        calendar.time = scheduleTime
        calendar.add(Calendar.MINUTE, -alarmSchedule.appointmentAlarmTime)
        return calendar.time
    }

    private fun createPendingIntent(alarmSchedule: Schedule): PendingIntent {
        val intent = createAlarmIntent(alarmSchedule)
        return PendingIntent.getBroadcast(
            context, alarmSchedule.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createAlarmIntent(alarmSchedule: Schedule): Intent {
        return Intent(context, com.dongsu.service.common.AlarmReceiver::class.java).apply {
            putExtra("scheduleTitle", alarmSchedule.title)
            putExtra("appointmentPlace", alarmSchedule.appointmentPlace)
            putExtra("latitude", alarmSchedule.latitude)
            putExtra("longitude", alarmSchedule.longitude)
            putExtra("alarmTime", alarmSchedule.appointmentAlarmTime)
        }
    }

    override suspend fun settingGroupScheduleAlarm() {}
    override suspend fun cancelScheduleAlarm() {
        TODO("Not yet implemented")
    }
}
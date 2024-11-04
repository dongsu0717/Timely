package com.dongsu.timely.presentation.common

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
    return String.format(Locale.KOREA, "%d-%02d-%02d", year, month + 1, dayOfMonth)
}

fun formatTimeToString(hour: Int, minute: Int): String {
    return String.format(Locale.KOREA, "%02d:%02d", hour, minute)
}

fun getAlarmTime(date: String, time: String): Calendar {
    // 날짜 포맷 (yyyy-MM-dd)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val parsedDate: Date = dateFormat.parse(date)
    // 시간 포맷 (HH:mm)
    val timeFormat = SimpleDateFormat("HH:mm", Locale.KOREA)
    val parsedTime: Date = timeFormat.parse(time)

    val calendar = Calendar.getInstance()

    parsedDate.let {
        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = it
        calendar.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR))
        calendar.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH))
        calendar.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH))
    }
    parsedTime.let {
        val timeCalendar = Calendar.getInstance()
        timeCalendar.time = it
        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, 0)
    }
    // 알람 시간 50초 전으로 설정
    calendar.add(Calendar.SECOND, -50)
    return calendar
}
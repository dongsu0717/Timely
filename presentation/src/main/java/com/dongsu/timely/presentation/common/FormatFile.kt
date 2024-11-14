package com.dongsu.timely.presentation.common

import android.icu.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
    return String.format(Locale.KOREA, "%d-%02d-%02d", year, month + 1, dayOfMonth)
}

fun formatTimeToString(hour: Int, minute: Int): String {
    return String.format(Locale.KOREA, "%02d:%02d", hour, minute)
}

fun combineDateTime(startDate: String, time: String): String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    val date = LocalDate.parse(startDate, dateFormatter)
    val localTime = LocalTime.parse(time, timeFormatter)

    return date.atTime(localTime).format(dateTimeFormatter)
}
fun formatDateTime(input: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy년M월d일 H시m분", Locale.getDefault())

    val date = inputFormat.parse(input)
    return outputFormat.format(date)
}

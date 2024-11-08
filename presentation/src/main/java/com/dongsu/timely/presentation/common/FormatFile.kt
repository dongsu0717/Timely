package com.dongsu.timely.presentation.common

import java.util.Locale

fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
    return String.format(Locale.KOREA, "%d-%02d-%02d", year, month + 1, dayOfMonth)
}

fun formatTimeToString(hour: Int, minute: Int): String {
    return String.format(Locale.KOREA, "%02d:%02d", hour, minute)
}
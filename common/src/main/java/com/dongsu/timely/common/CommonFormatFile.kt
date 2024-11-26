package com.dongsu.timely.common

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CommonFormatFile {
    fun parseToLocalDateTime(dateTimeString: String): LocalDateTime =
        LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}
package com.dongsu.timely.work

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.dongsu.timely.TimelyApplication
import com.dongsu.timely.common.CHANNEL_ID
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.common.GROUP_NAME
import com.dongsu.timely.common.SCHEDULE_ID
import com.dongsu.timely.common.SCHEDULE_START_TIME
import com.dongsu.timely.common.SCHEDULE_TITLE
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

fun setWorkManager(message: RemoteMessage){
    Timber.e("워크매니저세팅부분호출")
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
        .setInitialDelay(15, TimeUnit.SECONDS) //실험용 15초뒤에 작동 시키기
        .setInputData(workData)
        .build()
    WorkManager.getInstance(TimelyApplication.getInstance()).enqueue(workRequest)
}

private fun calculateInitialDelay(startTimeStr: String, minutesBefore: Long): Long {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val scheduleStartTime = LocalDateTime.parse(startTimeStr, formatter)
    val currentTime = LocalDateTime.now()

    val delayDuration = Duration.between(currentTime, scheduleStartTime.minusMinutes(minutesBefore))
    return if (delayDuration.isNegative) 0 else delayDuration.toMinutes()
}
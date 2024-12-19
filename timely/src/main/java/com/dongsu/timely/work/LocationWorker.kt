package com.dongsu.timely.work

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dongsu.timely.common.CHANNEL_ID
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.common.GROUP_NAME
import com.dongsu.timely.common.SCHEDULE_ID
import com.dongsu.timely.common.SCHEDULE_START_TIME
import com.dongsu.timely.common.SCHEDULE_TITLE
import com.dongsu.timely.service.LocationService
import timber.log.Timber

class LocationWorker(
    context: Context,
    private val params: WorkerParameters,
) : CoroutineWorker(context,params) {

    override suspend fun doWork(): Result {
        Timber.e(params.inputData.toString())
        return if(locationService()){
            Timber.e("성공")
            Result.success()
        } else {
            Timber
                .e("실패")
            Result.failure()
        }
    }

    private fun locationService(): Boolean {
        var flag = false
        if(params.inputData.getInt(SCHEDULE_ID, -1) != -1){
            flag = true
        }
        val serviceIntent = Intent(applicationContext, LocationService::class.java).apply {
            putExtra(GROUP_ID, params.inputData.getInt(GROUP_ID, -1))
            putExtra(GROUP_NAME, params.inputData.getString(GROUP_NAME))
            putExtra(SCHEDULE_TITLE, params.inputData.getString(SCHEDULE_TITLE))
            putExtra(SCHEDULE_START_TIME, params.inputData.getString(SCHEDULE_START_TIME))
            putExtra(CHANNEL_ID, params.inputData.getString(CHANNEL_ID))
            putExtra(SCHEDULE_ID, params.inputData.getInt(SCHEDULE_ID, -1))
        }
        applicationContext.startForegroundService(serviceIntent)
        return flag
    }
}
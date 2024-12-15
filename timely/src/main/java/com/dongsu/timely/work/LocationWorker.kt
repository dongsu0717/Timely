package com.dongsu.timely.work

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dongsu.timely.common.CHANNEL_ID
import com.dongsu.timely.common.GROUP_ID
import com.dongsu.timely.common.GROUP_NAME
import com.dongsu.timely.common.SCHEDULE_ID
import com.dongsu.timely.common.SCHEDULE_START_TIME
import com.dongsu.timely.common.SCHEDULE_TITLE
import com.dongsu.timely.service.LocationService

class LocationWorker(
    context: Context,
    private val params: WorkerParameters
) : CoroutineWorker(context,params) {

    override suspend fun doWork(): Result {
        Log.e("워크매니저로 넘어온 데이터",params.inputData.toString())
        return if(locationService()){
            Log.e("워크매너저","성공")
            Result.success()
        } else {
            Log.e("워크매너저","실패")
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
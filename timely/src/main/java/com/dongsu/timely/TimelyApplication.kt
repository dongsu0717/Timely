package com.dongsu.timely

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TimelyApplication:Application() {
    companion object{
        private lateinit var timelyApplication: TimelyApplication
        fun getInstance(): TimelyApplication = timelyApplication
    }
    override fun onCreate() {
        super.onCreate()
        timelyApplication = this
//        KakaoSdk.init(this, "ee0d873f3b14b4a89b04a43d24b5b421")
    }
}
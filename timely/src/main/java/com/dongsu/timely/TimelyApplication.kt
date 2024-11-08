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
    }
}
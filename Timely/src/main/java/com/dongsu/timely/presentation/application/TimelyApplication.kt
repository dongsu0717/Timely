package com.dongsu.timely.presentation.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TimelyApplication:Application() {

    companion object{
        private lateinit var timelyApplication: TimelyApplication
    }
    override fun onCreate() {
        super.onCreate()
        timelyApplication = this
    }

}
package com.dongsu.timely.presentation.application

import android.app.Application

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
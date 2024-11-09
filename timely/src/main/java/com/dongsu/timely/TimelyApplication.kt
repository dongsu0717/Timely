package com.dongsu.timely

import android.app.Application
import android.util.Log
import com.dongsu.timely.BuildConfig.KAKAO_API_KEY
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
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
        KakaoSdk.init(this,KAKAO_API_KEY)
        val hashKey = Utility.getKeyHash(this)
        Log.d("hash",hashKey)
    }
}
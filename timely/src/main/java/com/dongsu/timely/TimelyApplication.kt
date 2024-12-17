package com.dongsu.timely

import android.app.Application
import android.util.Log
import com.dongsu.timely.BuildConfig.KAKAO_API_KEY
import com.dongsu.timely.common.TimberDebugTree
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

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
        KakaoMapSdk.init(this,KAKAO_API_KEY)
        Timber.plant(TimberDebugTree())
        val hashKey = Utility.getKeyHash(this) //삭제할 부분
        Log.d("hash",hashKey) //삭제할 부분
    }
}
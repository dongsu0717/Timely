package com.dongsu.timely.presentation.ui.main.calendar.location

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://apis.openapi.sk.com/"

    val api: TmapService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmapService::class.java)
    }
}
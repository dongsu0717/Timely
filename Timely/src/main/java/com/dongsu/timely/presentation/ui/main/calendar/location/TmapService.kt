package com.dongsu.timely.presentation.ui.main.calendar.location

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmapService {
    @GET("tmap/pois")
    fun searchPlaces(
        @Query("version") version: Int = 1,
        @Query("searchKeyword") keyword: String,
        @Query("appKey") apiKey: String
    ): Call<TmapResponse>
}
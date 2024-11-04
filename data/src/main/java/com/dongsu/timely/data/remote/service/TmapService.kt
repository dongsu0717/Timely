package com.dongsu.timely.data.remote.service

import com.dongsu.timely.data.remote.dto.TmapResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TmapService {
    @GET("tmap/pois")
    suspend fun searchPlaces(
        @Query("version") version: Int = 1,
        @Query("searchKeyword") keyword: String,
        @Query("appKey") apiKey: String
    ): Response<TmapResponseDTO>
}
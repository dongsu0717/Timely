package com.dongsu.timely.data.remote.api

import com.dongsu.data.BuildConfig.TMAP_API_KEY
import com.dongsu.timely.data.common.TMAP_CAR_URL
import com.dongsu.timely.data.common.TMAP_POI
import com.dongsu.timely.data.common.TMAP_WALK_URL
import com.dongsu.timely.data.remote.dto.TMapDistanceCalculationResponseDto
import com.dongsu.timely.data.remote.dto.TMapPoiResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TmapService {
    @GET(TMAP_POI)
    suspend fun searchPlaces(
        @Query("version") version: Int = 1,
        @Query("searchKeyword") keyword: String,
        @Query("appKey") apiKey: String
    ): Response<TMapPoiResponseDTO>

    @GET(TMAP_CAR_URL)
    suspend fun getCarResult(
        @Query("version") version: String = "1",
        @Query("appKey") appKey: String = TMAP_API_KEY,
        @Query("totalValue") totalValue: Int = 2,
        @Query("startX") startX: Double,
        @Query("startY") startY: Double,
        @Query("endX") endX: Double,
        @Query("endY") endY: Double
    ): Response<TMapDistanceCalculationResponseDto>

    @GET(TMAP_WALK_URL)
    suspend fun getWalkResult(
        @Query("version") version: String = "1",
        @Query("appKey") appKey: String = TMAP_API_KEY,
        @Query("startX") startX: Double,
        @Query("startY") startY: Double,
        @Query("endX") endX: Double,
        @Query("endY") endY: Double,
        @Query("startName") startName: String = "%EC%B6%9C%EB%B0%9C",
        @Query("endName") endName: String = "%EB%B3%B8%EC%82%AC",
    ): Response<TMapDistanceCalculationResponseDto>
}
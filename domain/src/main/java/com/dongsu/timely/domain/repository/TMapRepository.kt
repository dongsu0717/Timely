package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PoiItem

interface TMapRepository {
    suspend fun searchPlaces(keyword: String): TimelyResult<List<PoiItem>>
//    suspend fun getMyLocation() : TimelyResult<UserLocation>
//    suspend fun showTMap()
//    suspend fun saveMyLocation()
}
package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PoiItem

interface TMapRepository {
    suspend fun searchPlaces(keyword: String): TimelyResult<MutableList<PoiItem>>
//    suspend fun showTMap()
//    suspend fun saveMyLocation()
//    suspend fun getMyLocation()
}
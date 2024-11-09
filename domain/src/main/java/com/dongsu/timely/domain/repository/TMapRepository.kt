package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.domain.model.UserLocation

interface TMapRepository {
    suspend fun searchPlaces(keyword: String): TimelyResult<MutableList<PoiItem>>
//    suspend fun getMyLocation() : TimelyResult<UserLocation>
//    suspend fun showTMap()
//    suspend fun saveMyLocation()
}
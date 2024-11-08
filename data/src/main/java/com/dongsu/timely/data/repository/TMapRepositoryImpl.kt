package com.dongsu.timely.data.repository

import com.dongsu.data.BuildConfig.TMAP_API_KEY
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.local.tmap.UserLocationManager
import com.dongsu.timely.data.mapper.LocationMapper
import com.dongsu.timely.data.remote.service.TmapService
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.domain.model.UserLocation
import com.dongsu.timely.domain.repository.TMapRepository
import javax.inject.Inject

class TMapRepositoryImpl @Inject constructor(
    private val tmapService: TmapService,
    private val userLocationManager: UserLocationManager
) : TMapRepository {

    override suspend fun searchPlaces(keyword: String): TimelyResult<MutableList<PoiItem>>{
        return try {
            val response = tmapService.searchPlaces(keyword = keyword, apiKey = "l7xx7daab04e0de142cf800ce73e929f55e3")
            val poiItems = response.body()?.searchPoiInfo?.pois?.poi?.map { LocationMapper.toDomain(it) }?.toMutableList() ?: mutableListOf()
            TimelyResult.Success(poiItems)
        } catch (e: Exception) {
            TimelyResult.Error(e)
        }
    }
    override suspend fun getMyLocation() : TimelyResult<UserLocation> = userLocationManager.currentLocation
}
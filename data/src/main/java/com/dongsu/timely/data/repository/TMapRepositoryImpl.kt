package com.dongsu.timely.data.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.mapper.PoiLocationMapper
import com.dongsu.timely.data.remote.api.TMapService
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.domain.repository.TMapRepository
import javax.inject.Inject

class TMapRepositoryImpl @Inject constructor(
    private val tmapService: TMapService,
//    private val userLocationManager: UserLocationManager
) : TMapRepository {

    override suspend fun searchPlaces(keyword: String): TimelyResult<MutableList<PoiItem>>
    = try {
            val response = tmapService.searchPlaces(keyword = keyword)
            val poiItems = response.body()?.searchPoiInfo?.pois?.poi?.map { PoiLocationMapper.toDomain(it) }?.toMutableList() ?: mutableListOf()
            TimelyResult.Success(poiItems)
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

//    override suspend fun getMyLocation() : TimelyResult<UserLocation> = userLocationManager.currentLocation
}
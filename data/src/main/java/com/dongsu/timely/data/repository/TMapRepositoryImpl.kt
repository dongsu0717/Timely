package com.dongsu.timely.data.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.mapper.LocationMapper
import com.dongsu.timely.data.remote.api.TmapService
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.domain.repository.TMapRepository
import javax.inject.Inject

class TMapRepositoryImpl @Inject constructor(
    private val tmapService: TmapService,
//    private val userLocationManager: UserLocationManager
) : TMapRepository {

    override suspend fun searchPlaces(keyword: String): TimelyResult<MutableList<PoiItem>>
    = try {
            val response = tmapService.searchPlaces(keyword = keyword)
            val poiItems = response.body()?.searchPoiInfo?.pois?.poi?.map { LocationMapper.toDomain(it) }?.toMutableList() ?: mutableListOf()
            TimelyResult.Success(poiItems)
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

//    override suspend fun getMyLocation() : TimelyResult<UserLocation> = userLocationManager.currentLocation
}
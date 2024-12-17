package com.dongsu.timely.data.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.mapper.PoiLocationMapper
import com.dongsu.timely.data.remote.api.TMapService
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.domain.repository.TMapRepository
import javax.inject.Inject

class TMapRepositoryImpl @Inject constructor(
    private val tMapService: TMapService,
    private val poiLocationMapper: PoiLocationMapper

) : TMapRepository {

    override suspend fun searchPlaces(keyword: String): TimelyResult<List<PoiItem>>
        = try {
            val response = tMapService.searchPlaces(keyword = keyword)
            if (response.body() != null){
                val poiItems = response.body()!!.searchPoiInfo.pois.poi.map { poiLocationMapper.toDomain(it) }.toList()
                TimelyResult.Success(poiItems)
            } else {
                TimelyResult.Empty
            }
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }
//    override suspend fun getMyLocation() : TimelyResult<UserLocation> = userLocationManager.currentLocation
}
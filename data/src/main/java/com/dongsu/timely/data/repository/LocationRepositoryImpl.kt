package com.dongsu.timely.data.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.mapper.LocationMapper
import com.dongsu.timely.data.remote.service.TmapService
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val tmapService: TmapService
) : LocationRepository {
    private val apiKey = "l7xx7daab04e0de142cf800ce73e929f55e3"

    override suspend fun searchPlaces(keyword: String): TimelyResult<MutableList<PoiItem>>{
        return try {
            val response = tmapService.searchPlaces(keyword = keyword, apiKey = apiKey)
            val poiItems = response.body()?.searchPoiInfo?.pois?.poi?.map { LocationMapper.toDomain(it) }?.toMutableList() ?: mutableListOf()
            TimelyResult.Success(poiItems)
        } catch (e: Exception) {
            TimelyResult.Error(e)
        }
    }
}
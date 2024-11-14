package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.PoiItemResponse
import com.dongsu.timely.domain.model.PoiItem

object PoiLocationMapper {
    fun toDomain(dto: PoiItemResponse): PoiItem{
        return PoiItem(
            name = dto.name,
            noorLat = dto.noorLat,
            noorLon = dto.noorLon
        )
    }
}
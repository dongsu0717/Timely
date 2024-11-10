package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.PoiItemDTO
import com.dongsu.timely.domain.model.PoiItem

object LocationMapper {
    fun toDomain(dto: PoiItemDTO): PoiItem{
        return PoiItem(
            name = dto.name,
            noorLat = dto.noorLat,
            noorLon = dto.noorLon
        )
    }
}
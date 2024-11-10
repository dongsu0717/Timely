package com.dongsu.timely.data.remote.dto.response

data class TMapPoiResponseDTO(
    val searchPoiInfo: SearchPoiInfoDTO
)

data class SearchPoiInfoDTO(
    val pois: PoisDTO
)

data class PoisDTO(
    val poi: MutableList<PoiItemDTO>
)

data class PoiItemDTO(
    val name: String,
    val noorLat: String, // 위도
    val noorLon: String  // 경도
)

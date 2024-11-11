package com.dongsu.timely.data.remote.dto.response

data class TMapPoiResponse(
    val searchPoiInfo: SearchPoiInfo
)

data class SearchPoiInfo(
    val pois: PoisDTO
)

data class PoisDTO(
    val poi: MutableList<PoiItemResponse>
)

data class PoiItemResponse(
    val name: String,
    val noorLat: String, // 위도
    val noorLon: String  // 경도
)

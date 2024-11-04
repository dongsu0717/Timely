package com.dongsu.timely.domain.repository

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PoiItem

interface LocationRepository {
    suspend fun searchPlaces(keyword: String): TimelyResult<MutableList<PoiItem>>
}
package com.dongsu.timely.domain.usecase.schedule

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.domain.repository.LocationRepository
import javax.inject.Inject

class SearchLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(query: String): TimelyResult<MutableList<PoiItem>>
            = locationRepository.searchPlaces(query)
}
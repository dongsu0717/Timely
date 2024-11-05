package com.dongsu.timely.domain.usecase.schedule

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.PoiItem
import com.dongsu.timely.domain.repository.TMapRepository
import javax.inject.Inject

class SearchLocationUseCase @Inject constructor(
    private val tMapRepository: TMapRepository
) {
    suspend operator fun invoke(query: String): TimelyResult<MutableList<PoiItem>>
            = tMapRepository.searchPlaces(query)
}
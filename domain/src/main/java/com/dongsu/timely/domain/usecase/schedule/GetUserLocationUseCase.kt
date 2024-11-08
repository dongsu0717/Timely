package com.dongsu.timely.domain.usecase.schedule

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.UserLocation
import com.dongsu.timely.domain.repository.TMapRepository
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val tMapRepository: TMapRepository
) {
    suspend operator fun invoke(): TimelyResult<UserLocation> = tMapRepository.getMyLocation()
}
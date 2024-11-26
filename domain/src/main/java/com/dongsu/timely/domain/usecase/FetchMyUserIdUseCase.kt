package com.dongsu.timely.domain.usecase

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.repository.UserRepository
import javax.inject.Inject

//MyInfo에서 userId만 가져오는 UseCase
class FetchMyUserIdUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(): TimelyResult<Int> =
        try {
            when(val result = userRepository.fetchMyInfo()){
                is TimelyResult.Empty -> TimelyResult.Empty
                is TimelyResult.Success -> TimelyResult.Success(result.resultData.userId)
                else -> TimelyResult.NetworkError(Exception())
            }
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }
}
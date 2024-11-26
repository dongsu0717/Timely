package com.dongsu.timely.domain.usecase

import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.domain.model.Token
import com.dongsu.timely.domain.repository.FCMRepository
import com.dongsu.timely.domain.repository.UserRepository
import javax.inject.Inject

class UserTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val fcmRepository: FCMRepository
) {
    suspend operator fun invoke(token: String): TimelyResult<Unit> =
        try {
            when(val result = userRepository.sendKaKaoTokenAndGetToken(token)){
                is TimelyResult.Empty -> TimelyResult.Empty
                is TimelyResult.Success -> {
                    saveTokenLocal(result.resultData)
                    sendFCMToken()
                    TimelyResult.Success(Unit)
                }
                else -> TimelyResult.NetworkError(Exception())
            }
        } catch (e: Exception) {
            TimelyResult.NetworkError(e)
        }

    private suspend fun saveTokenLocal(token: Token) {
        if(token.accessToken != null && token.refreshToken != null) {
            userRepository.saveTokenLocal(token.accessToken, token.refreshToken)
        }
    }

    private suspend fun sendFCMToken() {
        val fcmToken = fcmRepository.getFCMToken()
        userRepository.sendFCMToken(fcmToken)
    }
}
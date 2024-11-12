package com.dongsu.timely.domain.usecase

import com.dongsu.timely.domain.repository.FCMRepository
import com.dongsu.timely.domain.repository.UserRepository
import javax.inject.Inject

class UserTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val fcmRepository: FCMRepository
) {
    suspend operator fun invoke(token: String) {
        val tokenInfo = userRepository.sendToken(token)
        if(tokenInfo.accessToken != null && tokenInfo.refreshToken != null) {
            userRepository.saveTokenLocal(tokenInfo.accessToken, tokenInfo.refreshToken)
            sendFCMToken()
        }
    }
    private suspend fun sendFCMToken(){
        val fcmToken = fcmRepository.getFCMToken()
        userRepository.sendFCMToken(fcmToken)
    }
}
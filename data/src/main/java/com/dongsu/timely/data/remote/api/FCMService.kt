package com.dongsu.timely.data.remote.api

import com.dongsu.timely.data.common.FCM
import com.dongsu.timely.data.remote.dto.request.SendFCMTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMService {

    @POST(FCM)
    suspend fun sendFcmToken(
        @Body sendTokenRequest: SendFCMTokenRequest
    ): Response<Unit>

}
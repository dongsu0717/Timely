package com.dongsu.timely.data.remote.api

import com.dongsu.timely.data.common.LOGIN_TOKEN
import com.dongsu.timely.data.remote.dto.request.SendTokenRequest
import com.dongsu.timely.data.remote.dto.response.SendTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST(LOGIN_TOKEN)
    suspend fun sendToken(
        @Body sendTokenRequest: SendTokenRequest
    ): Response<SendTokenResponse>

}
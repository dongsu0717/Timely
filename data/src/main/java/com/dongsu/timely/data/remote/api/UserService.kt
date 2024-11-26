package com.dongsu.timely.data.remote.api

import com.dongsu.timely.data.common.USER_INFO
import com.dongsu.timely.data.common.USER_LATE
import com.dongsu.timely.data.remote.dto.response.map.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @GET(USER_INFO)
    suspend fun fetchMyInfo(): Response<UserResponse>

    @POST(USER_LATE)
    suspend fun countLateness()

}
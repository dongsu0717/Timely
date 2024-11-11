package com.dongsu.timely.data.remote.api

import com.dongsu.timely.data.common.GROUP
import com.dongsu.timely.data.remote.dto.request.GroupRequest
import com.dongsu.timely.data.remote.dto.response.GroupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupService {

    @POST(GROUP)
    suspend fun createGroup(
        @Body groupRequest: GroupRequest
    ):Response<GroupResponse>

}
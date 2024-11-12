package com.dongsu.timely.data.remote.api

import com.dongsu.timely.data.common.GROUP_SCHEDULE
import com.dongsu.timely.data.remote.dto.request.GroupScheduleRequest
import com.dongsu.timely.data.remote.dto.response.CommonResponse
import com.dongsu.timely.data.remote.dto.response.GroupScheduleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupScheduleService {

    @POST(GROUP_SCHEDULE)
    suspend fun insertSchedule(
        @Path("groupId") groupId: Int,
        @Body groupScheduleRequest: GroupScheduleRequest
    ): Response<CommonResponse>

    @GET(GROUP_SCHEDULE)
    suspend fun getAllScheduleList(
        @Path("groupId") groupId: Int
    ): Response<List<GroupScheduleResponse>>

}
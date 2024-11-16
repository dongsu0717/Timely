package com.dongsu.timely.data.remote.api

import com.dongsu.timely.data.common.GROUP_SCHEDULE
import com.dongsu.timely.data.common.GROUP_SCHEDULE_PARTICIPATION
import com.dongsu.timely.data.common.LOCATION
import com.dongsu.timely.data.remote.dto.request.GroupScheduleRequest
import com.dongsu.timely.data.remote.dto.request.LocationRequest
import com.dongsu.timely.data.remote.dto.response.CommonResponse
import com.dongsu.timely.data.remote.dto.response.TotalGroupScheduleInfoResponse
import com.dongsu.timely.data.remote.dto.response.map.GroupMeetingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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
    ): Response<List<TotalGroupScheduleInfoResponse>>

    @POST(GROUP_SCHEDULE_PARTICIPATION)
    suspend fun participationSchedule(
        @Path("groupId") groupId: Int,
        @Path("scheduleId") scheduleId: Int
    ): Response<Unit>

    @PATCH(GROUP_SCHEDULE_PARTICIPATION)
    suspend fun cancelParticipationSchedule(
        @Path("groupId") groupId: Int,
        @Path("scheduleId") scheduleId: Int
    ): Response<Unit>

    @PUT(LOCATION)
    suspend fun updateMyLocation(
        @Path("scheduleId") scheduleId: Int,
        @Body locationRequest: LocationRequest
    ): Response<CommonResponse>

    @GET(LOCATION)
    suspend fun getGroupLocation(
        @Path("scheduleId") scheduleId: Int
    ): Response<GroupMeetingResponse>
}
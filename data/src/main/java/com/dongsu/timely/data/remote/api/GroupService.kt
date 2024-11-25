package com.dongsu.timely.data.remote.api

import com.dongsu.timely.data.common.GROUP
import com.dongsu.timely.data.common.INVITE_GROUP
import com.dongsu.timely.data.common.JOIN_GROUP
import com.dongsu.timely.data.remote.dto.request.GroupRequest
import com.dongsu.timely.data.remote.dto.response.GroupResponse
import com.dongsu.timely.data.remote.dto.response.InviteCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupService {

    @POST(GROUP)
    suspend fun createGroup(
        @Body groupRequest: GroupRequest
    ):Response<GroupResponse>

    @GET(GROUP)
    suspend fun fetchMyGroupList(): Response<List<GroupResponse>>

    @POST(INVITE_GROUP)
    suspend fun createInviteCode(
        @Path("groupId") groupId: Int
    ): Response<InviteCodeResponse>

    @POST(JOIN_GROUP)
    suspend fun joinGroup(
        @Path("inviteCode") inviteCode: String
    ): Response<Unit>
}
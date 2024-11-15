package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.InviteCodeResponse
import com.dongsu.timely.domain.model.InviteCode

object InviteCodeMapper {
    fun toDomain(inviteCodeResponse: InviteCodeResponse): InviteCode {
        return InviteCode(
            groupId = inviteCodeResponse.groupId,
            inviteCode = inviteCodeResponse.inviteCode
        )
    }
}
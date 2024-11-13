package com.dongsu.timely.data.mapper

import com.dongsu.timely.data.remote.dto.response.ParticipationMemberResponse
import com.dongsu.timely.domain.model.ParticipationMember

object ParticipationMemberMapper {
    fun toDomain(participationMemberResponse: ParticipationMemberResponse): ParticipationMember {
        return ParticipationMember(
            user = UserMapper.toDomain(participationMemberResponse.user),
            userLocation = LocationMapper.toDomain(participationMemberResponse.location)
        )

    }
}
package com.dongsu.timely.domain.model

import com.dongsu.timely.domain.model.map.User

data class Group(
    val groupId: Int,
    val groupName: String,
    val users: User,
    val groupColor: Int,
    val createdAt: String,
    val memberNumber: Int
)

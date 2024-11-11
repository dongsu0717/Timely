package com.dongsu.timely.domain.model

data class Group(
    val groupId: Int,
    val groupName: String,
    val users: User,
    val createdAt: String,
    val memberNumber: Int
)

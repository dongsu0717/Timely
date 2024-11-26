package com.dongsu.timely.domain.model

data class InviteCode(
    val groupId: Int = 0,
    val inviteCode: String = ""
) {
    companion object {
        val EMPTY = InviteCode()
    }
}

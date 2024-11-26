package com.dongsu.timely.domain.model.map

data class User(
    val userId: Int = 0,
    val email: String? = "",
    val nickname: String = "",
) {
    companion object {
        val EMPTY = User()
    }
}
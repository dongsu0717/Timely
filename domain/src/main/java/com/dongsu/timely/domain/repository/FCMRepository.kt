package com.dongsu.timely.domain.repository

interface FCMRepository {
    suspend fun getFCMToken(): String
}
package com.dongsu.timely.domain.repository

interface SettingRepository {
    suspend fun changedMode()
}
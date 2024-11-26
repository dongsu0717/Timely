package com.dongsu.timely.data.datasource.local

import com.dongsu.timely.data.local.preferences.PreferencesManager
import javax.inject.Inject

class UserLocalDatasourceImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
): UserLocalDatasource {
    override fun getToken(): String =
        preferencesManager.loadToken()

    override suspend fun saveTokenLocal(accessToken: String, refreshToken: String) =
        preferencesManager.saveTokenLocal(accessToken, refreshToken)

    override suspend fun isLoggedIn(): Boolean =
        preferencesManager.isLoggedIn()
}

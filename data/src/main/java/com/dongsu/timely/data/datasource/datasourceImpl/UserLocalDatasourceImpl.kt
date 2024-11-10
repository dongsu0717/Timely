package com.dongsu.timely.data.datasource.datasourceImpl

import android.content.SharedPreferences
import android.util.Log
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.datasource.UserLocalDatasource
import javax.inject.Inject

class UserLocalDatasourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): UserLocalDatasource {
    override suspend fun getToken() {

    }

    override suspend fun saveLoginStatus(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            Log.e("sharedPreferences저장성공","$accessToken, $refreshToken")
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    override suspend fun isLoggedIn(): TimelyResult<Boolean> {
        return when(sharedPreferences.getBoolean("is_logged_in", false)) {
            true -> TimelyResult.Success(true)
            false -> TimelyResult.Success(false)
        }
    }
}

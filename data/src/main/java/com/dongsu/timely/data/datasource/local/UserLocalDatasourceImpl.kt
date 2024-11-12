package com.dongsu.timely.data.datasource.local

import android.content.SharedPreferences
import android.util.Log
import com.dongsu.timely.common.TimelyResult
import com.dongsu.timely.data.common.ACCESS_TOKEN
import com.dongsu.timely.data.common.EMPTY_STRING
import com.dongsu.timely.data.common.IS_LOGGED_IN
import com.dongsu.timely.data.common.REFRESH_TOKEN
import javax.inject.Inject

class UserLocalDatasourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): UserLocalDatasource {
    override fun getToken(): String = sharedPreferences.getString(ACCESS_TOKEN, EMPTY_STRING).toString()

    override suspend fun saveLoginStatus(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            Log.e("sharedPreferences저장성공","$accessToken, $refreshToken")
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
            putBoolean(IS_LOGGED_IN, true)
            apply()
        }
    }

    override suspend fun isLoggedIn(): TimelyResult<Boolean> {
        return when(sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            true -> TimelyResult.Success(true)
            false -> TimelyResult.Success(false)
        }
    }
}

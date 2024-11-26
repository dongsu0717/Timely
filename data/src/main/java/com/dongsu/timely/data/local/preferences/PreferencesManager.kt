package com.dongsu.timely.data.local.preferences

import android.content.SharedPreferences
import android.util.Log
import com.dongsu.timely.data.common.ACCESS_TOKEN
import com.dongsu.timely.data.common.EMPTY_STRING
import com.dongsu.timely.data.common.IS_LOGGED_IN
import com.dongsu.timely.data.common.REFRESH_TOKEN
import javax.inject.Inject

class PreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun loadToken(): String =
        sharedPreferences.getString(ACCESS_TOKEN, EMPTY_STRING).toString()

    fun saveTokenLocal(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            Log.e("sharedPreferences저장성공","$accessToken, $refreshToken")
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
            putBoolean(IS_LOGGED_IN, true)
            apply()
        }
    }

    fun isLoggedIn(): Boolean =
        sharedPreferences.getBoolean(IS_LOGGED_IN, false)
}
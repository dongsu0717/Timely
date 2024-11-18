package com.dongsu.timely.data.common

import android.content.Context
import android.widget.Toast
import com.dongsu.data.BuildConfig.TIMELY_HOST_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
suspend fun showToast(context: Context, message: String) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
//Auth
const val AUTH_HEADER = "Authorization"
const val BEARER = "Bearer"
const val ACCESS_TOKEN = "access_token"
const val REFRESH_TOKEN = "refresh_token"
const val IS_LOGGED_IN = "is_logged_in"

//TMP_URL
const val TMAP_BASE_URL = "https://apis.openapi.sk.com/"
const val TMAP_POI = "tmap/pois"
const val TMAP_CAR_URL = "tmap/routes"
const val TMAP_WALK_URL = "tmap/routes/pedestrian"

//TIMELY_URL
const val PROTOCOL = "http"
const val HOST = TIMELY_HOST_URL
const val PORT = 8080
const val API_VERSION = "v1"
const val ADDRESS_PORT = "$HOST:$PORT"
const val TIMELY_BASE_URL = "$PROTOCOL://$ADDRESS_PORT/"

//account
const val LOGIN_TOKEN = "auth/mobile/kakao"
const val USER_INFO = "$API_VERSION/users/me"
const val REISSUED_ACCESS_TOKEN = "auth/token"
const val REISSUED_ALL_TOKEN = "auth/token/refresh"

//group
const val GROUP = "$API_VERSION/groups"
const val GROUP_INFO = "$API_VERSION/groups/{groupId}"
const val INVITE_GROUP = "$API_VERSION/groups/{groupId}/invite-code"
const val JOIN_GROUP = "$API_VERSION/groups/signup/{inviteCode)"

//group schedule
const val GROUP_SCHEDULE = "$API_VERSION/groups/{groupId}/schedule"
const val GROUP_SCHEDULE_PARTICIPATION = "$API_VERSION/groups/{groupId}/schedule/{scheduleId}/participation"
const val LOCATION = "$API_VERSION/locations/{scheduleId}"
const val STATE_MESSAGE = "$API_VERSION/locations/{scheduleId}/state-message"

//fcm
const val FCM = "$API_VERSION/fcm"

//another
const val EMPTY_STRING = ""

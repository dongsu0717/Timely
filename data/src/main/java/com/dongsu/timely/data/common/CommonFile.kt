package com.dongsu.timely.data.common

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
suspend fun showToast(context: Context, message: String) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

//TMP_URL
const val TMAP_BASE_URL = "https://apis.openapi.sk.com/"
const val TMAP_POI = "tmap/pois"
const val TMAP_CAR_URL = "tmap/routes"
const val TMAP_WALK_URL = "tmap/routes/pedestrian"
const val TMAP_PACKAGE_NAME = "com.skt.tmap.ku"
const val TMAP_ROUTE_URL = "tmap://route?goalname=%s&goalx=%s&goaly=%s&startx=%s&starty=%s"
const val TMAP_MARKET_URL = "market://details?id=%s"



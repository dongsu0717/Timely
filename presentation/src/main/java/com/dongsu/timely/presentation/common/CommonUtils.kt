package com.dongsu.timely.presentation.common

import android.content.Context
import android.widget.Toast
import com.dongsu.presentation.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


object CommonUtils {

    fun toastShort(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    fun setScheduleBackgroundColor(color: Int) =
        when (color) {
            EnumScheduleColor.LAVENDER.order -> R.color.dark_lavender
            EnumScheduleColor.SAGE.order -> R.color.dark_sage
            EnumScheduleColor.GRAPE.order -> R.color.dark_grape
            EnumScheduleColor.FLAMINGO.order -> R.color.dark_flamingo
            EnumScheduleColor.BANANA.order -> R.color.dark_banana
            else -> {
                R.color.dark_lavender
            }
        }

    fun setGroupBackgroundColor(color: Int) =
        when (color) {
            EnumGroupScheduleColor.TANGERINE.order -> R.color.dark_tangerine
            EnumGroupScheduleColor.PEACOCK.order -> R.color.dark_peacock
            EnumGroupScheduleColor.BLUEBERRY.order -> R.color.dark_blueberry
            EnumGroupScheduleColor.BASIL.order -> R.color.dark_basil
            EnumGroupScheduleColor.TOMATO.order -> R.color.dark_tomato
            else -> {
                R.color.dark_tangerine
            }
        }
}
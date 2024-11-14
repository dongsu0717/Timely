package com.dongsu.timely.data.local.fusedlocation

import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import javax.inject.Inject

class LocationProvider @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) {

    private var locationCallback: ((Location) -> Unit)? = null

    fun startLocationUpdates(callback: (Location) -> Unit) {
        locationCallback = callback

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2_000L
        ).apply {
            setMinUpdateIntervalMillis(2_000L)
            setMaxUpdateDelayMillis(3_000L)
        }.build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationListener,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationListener)
    }

    private val locationListener = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            location?.let {
                locationCallback?.invoke(it)
            }
        }
    }
}
package com.dongsu.timely.presentation.common
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtils {

    fun isLocationServiceEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun requestSinglePermission(
        fragment: Fragment,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                onGranted()
            } else {
                onDenied()
            }
        }

    fun requestMultiplePermissions(
        fragment: Fragment,
        permissions: Array<String>,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) = fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantedPermissions ->
        if (permissions.all { grantedPermissions[it] == true }) {
            onGranted()
        } else {
            onDenied()
        }
    }

    fun arePermissionsGranted(context: Context, permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun shouldShowRequestPermissionRationale(fragment: Fragment, permissions: Array<String>): Boolean {
        return permissions.any { fragment.shouldShowRequestPermissionRationale(it) }
    }
}
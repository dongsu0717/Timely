import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtils {

    // Request permission launcher
    fun registerLocationPermissions(
        fragment: Fragment,
        onPermissionsGranted: () -> Unit,
        onPermissionsDenied: () -> Unit
    ) = fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            onPermissionsGranted()
        } else {
            onPermissionsDenied()
        }
    }

    // Check if location service is enabled
    fun isLocationServiceEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Check if location permissions are granted
    fun areLocationPermissionsGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    fun shouldShowRequestPermissionRationaleForLocation(fragment: Fragment): Boolean {
        return fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    // Show dialogs for various permission-related actions
    fun showLocationServiceDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("위치 서비스 활성화 필요")
            .setMessage("위치 서비스를 활성화해야 이 기능을 사용할 수 있습니다. 설정으로 이동하시겠습니까?")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
            .setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun showExplanationDialog(context: Context, onRetry: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("위치 권한 필요")
            .setMessage("이 기능을 사용하려면 위치 권한이 필요합니다. 권한을 허용해 주세요.")
            .setPositiveButton("확인") { dialog, _ ->
                onRetry()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun showPermissionsDeniedDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("위치 권한 거부")
            .setMessage("위치 권한이 거부되었습니다. 이 기능을 사용하려면 설정에서 권한을 수동으로 허용해야 합니다.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            .setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
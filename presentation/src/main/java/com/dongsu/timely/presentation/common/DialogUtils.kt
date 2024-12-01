package com.dongsu.timely.presentation.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogUtils {

    fun showLoginDialog( //안쓰지만 그냥 냅두자
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String = "확인",
        negativeButtonText: String = "취소",
        action: () -> Unit
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                action()
            }
            .setNegativeButton(negativeButtonText) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    fun showInputDialog(
        context: Context,
        hintMessage:String= "상태 메세지를 입력해주세요",
        title: String = "상태 메세지 입력",
        positiveButtonText: String = "확인",
        negativeButtonText: String = "취소",
        action: (String) -> Unit
    ) {
        val inputField = EditText(context).apply{
            hint = hintMessage
            textSize = 16f
        }
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setView(inputField)
            .setPositiveButton(positiveButtonText) { _, _ ->
                val inputText = inputField.text.toString()
                if (inputText.isNotEmpty()) {
                    action(inputText)
                } else {
                    CommonUtils.toastShort(context,"다시 입력해주세요")
                }
            }
            .setNegativeButton(negativeButtonText) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }

    fun showLocationServiceActivationDialog(context: Context, manager: FragmentManager) = CommonDialogFragment(
        LOCATION_SERVICE_ACTIVATION_TITLE,
        LOCATION_SERVICE_ACTIVATION_MESSAGE,
        LOCATION_SERVICE_ACTIVATION_POSITIVE_BUTTON
    ) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }.show(manager, LOCATION_SERVICE_ACTIVATION_TAG)

    fun showLocationPermissionsNeededDialog(manager: FragmentManager, onRetry: () -> Unit) = CommonDialogFragment(
        LOCATION_PERMISSION_NEEDED_TITLE,
        LOCATION_PERMISSION_NEEDED_MESSAGE
    ) {
        onRetry()
    }.show(manager, LOCATION_PERMISSION_NEEDED_TAG)

    fun showLocationPermissionsDeniedDialog(context: Context, manager: FragmentManager) = CommonDialogFragment(
        LOCATION_PERMISSION_DENIED_TITLE,
        LOCATION_PERMISSION_DENIED_MESSAGE,
        LOCATION_PERMISSION_DENIED_POSITIVE_BUTTON
    ){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }.show(manager, LOCATION_PERMISSION_DENIED_TAG)

}

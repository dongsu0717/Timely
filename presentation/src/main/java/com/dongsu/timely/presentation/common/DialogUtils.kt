package com.dongsu.timely.presentation.common

import android.content.Context
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogUtils {
    fun showLoginDialog(
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
}

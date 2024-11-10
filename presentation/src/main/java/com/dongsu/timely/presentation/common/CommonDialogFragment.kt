package com.dongsu.timely.presentation.common

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.app.Dialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CommonDialogFragment(
    private val title: String,
    private val message: String,
    private val positiveButtonText: String = "확인",
    private val negativeButtonText: String = "취소",
    private val action: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ -> action() }
            .setNegativeButton(negativeButtonText) { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .create()
    }
}

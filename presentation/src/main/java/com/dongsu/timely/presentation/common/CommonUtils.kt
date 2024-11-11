package com.dongsu.timely.presentation.common

import android.content.Context
import android.widget.Toast

object CommonUtils {
    fun toastShort(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
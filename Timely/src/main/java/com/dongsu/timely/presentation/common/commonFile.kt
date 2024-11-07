package com.dongsu.timely.presentation.common

import android.widget.Toast
import com.dongsu.timely.presentation.application.TimelyApplication


fun toastShort(message: String){
    Toast.makeText(TimelyApplication.getInstance(), message, Toast.LENGTH_SHORT).show()
}

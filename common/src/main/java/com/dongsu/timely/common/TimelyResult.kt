package com.dongsu.timely.common

sealed class TimelyResult<out T> {
    data object No : TimelyResult<Nothing>()
    data object Loading : TimelyResult<Nothing>()
    data class Success<T>(val resultData: T) : TimelyResult<T>()
    data class Error(val exception: Throwable) : TimelyResult<Nothing>()
}
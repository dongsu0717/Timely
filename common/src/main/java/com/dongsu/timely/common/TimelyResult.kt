package com.dongsu.timely.common

sealed class TimelyResult<out T> {
    data object Empty : TimelyResult<Nothing>()
    data object Loading : TimelyResult<Nothing>()
    data class Success<T>(val resultData: T) : TimelyResult<T>()
    data class NetworkError(val exception: Throwable) : TimelyResult<Nothing>()
    data class RoomDBError(val exception: Throwable) : TimelyResult<Nothing>()
}
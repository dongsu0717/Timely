package com.dongsu.timely.common

sealed class TimelyResult<out T> {
    data object NoConstructor : TimelyResult<Nothing>()
    data object Loading : TimelyResult<Nothing>()

    data class Success<T>(val resultData: T) : TimelyResult<T>()

    data class NetworkError(val exception: Throwable) : TimelyResult<Nothing>()
    data class RoomDBError(val exception: Throwable) : TimelyResult<Nothing>()
}

/*
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}
 */
package com.chat.joycom.network

sealed class ApiResult<T> {
    class OnSuccess<T>(val data: T) : ApiResult<T>()
    class OnFail<T>(val code: Int?, val message: String?, e: Throwable? = null) : ApiResult<T>()
}

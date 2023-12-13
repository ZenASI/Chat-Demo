package com.chat.joycom.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
    @field:Json(name = "code") val code: Int,
    @field:Json(name = "msg") val msg: String,
    @field:Json(name = "data") val data: T?,
)
package com.chat.joycom.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SocketBaseResponse<T>(
    @field:Json(name = "Status") val status: Int,
    @field:Json(name = "Type") val type: Int,
    @field:Json(name = "Data") val data: T,
)

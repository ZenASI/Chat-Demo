package com.chat.joycom.model.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageServerRequest(
    @field:Json(name = "cmd") val cmd: String,
    @field:Json(name = "uid") val uid: Long,
    @field:Json(name = "token") val token: String,
    @field:Json(name = "lastackmid") val lastackmid: String,
    @field:Json(name = "devicetype") val devicetype: Int = 2,
)

package com.chat.joycom.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Member(
    @field:Json(name = "UserId") val userId: Int,
    @field:Json(name = "AccountName") val accountName: String,
    @field:Json(name = "Nickname") val nickname: String,
    @field:Json(name = "Avatar") val avatar: String,
)
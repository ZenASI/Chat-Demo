package com.chat.joycom.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfo(
    @field:Json(name = "Profile") val profile: Profile,
    @field:Json(name = "Contacts") val contacts: List<Contact>?,
    @field:Json(name = "Groups") val groups: List<Group>?,
    @field:Json(name = "LastAckId") val lastAckId: String,
)

@JsonClass(generateAdapter = true)
data class Profile(
    @field:Json(name = "UserId") val userId: String,
    @field:Json(name = "Nickname") val nickname: String,
    @field:Json(name = "Avatar") val avatar: String,
    @field:Json(name = "NeedPin") val needPin: String,
)
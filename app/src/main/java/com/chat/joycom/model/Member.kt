package com.chat.joycom.model

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Member(
    @field:Json(name = "UserId") val userId: Long,
    @field:Json(name = "AccountName") val accountName: String,
    @field:Json(name = "Nickname") val nickname: String,
    @field:Json(name = "Avatar") val avatar: String,
) : Parcelable {

    @IgnoredOnParcel
    var isSelect by mutableStateOf(false)
}
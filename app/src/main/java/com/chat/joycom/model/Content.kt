package com.chat.joycom.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Content(
    @field:Json(name = "Guid") val guid: String,
    @field:Json(name = "FileName") val fileName: String,
    @field:Json(name = "FileType") val fileType: String,
    @field:Json(name = "Width") val width: Long,
    @field:Json(name = "Height") val height: Long,
    @field:Json(name = "Sender") val sender: String,
    @field:Json(name = "AtUser") val atUser: ArrayList<String>,
    @field:Json(name = "Text") val text: String,
) : Parcelable

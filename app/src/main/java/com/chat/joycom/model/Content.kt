package com.chat.joycom.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Content(
    @field:Json(name = "Guid") val guid: String? = null,
    @field:Json(name = "FileName") val fileName: String? = null,
    @field:Json(name = "FileType") val fileType: String? = null,
    @field:Json(name = "Width") val width: Long? = null,
    @field:Json(name = "Height") val height: Long? = null,
    @field:Json(name = "Sender") val sender: String? = null,
    @field:Json(name = "AtUser") val atUser: List<String> = emptyList(),
    @field:Json(name = "Text") val text: String? = null,
) : Parcelable

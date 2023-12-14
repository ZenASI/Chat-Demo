package com.chat.joycom.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Config(
    @field:Json(name = "userid") val userid: Int? = 0,
    @field:Json(name = "cookie") var cookie: String = "",
    @field:Json(name = "api") val api: Api? = null,
    @field:Json(name = "websocket") val websocket: Websocket? = null,
    @field:Json(name = "signal") val signal: Signal? = null,
    @field:Json(name = "relay") val relay: Relay? = null,
)

@JsonClass(generateAdapter = true)
data class Api(
    @field:Json(name = "Name") val name: String,
    @field:Json(name = "Protocol") val protocol: String,
    @field:Json(name = "Domain") val domain: String,
    @field:Json(name = "Port1") val port1: String?,
    @field:Json(name = "Port2") val port2: String?,
    @field:Json(name = "Uri") val uri: String?,
) {

//    fun getDomainUrl(): String = "https://120.79.151.1:3389" + uri
    fun getDomainUrl():String = protocol + domain + uri
}

@JsonClass(generateAdapter = true)
data class Websocket(
    @field:Json(name = "Name") val name: String,
    @field:Json(name = "Protocol") val protocol: String,
    @field:Json(name = "Domain") val domain: String,
    @field:Json(name = "Port1") val port1: String?,
    @field:Json(name = "Port2") val port2: String?,
    @field:Json(name = "Uri") val uri: String?,
){
    fun getDomainUrl():String = protocol + domain + uri
}

@JsonClass(generateAdapter = true)
data class Signal(
    @field:Json(name = "Name") val name: String,
    @field:Json(name = "Protocol") val protocol: String,
    @field:Json(name = "Domain") val domain: String,
    @field:Json(name = "Port1") val port1: String?,
    @field:Json(name = "Port2") val port2: String?,
    @field:Json(name = "Uri") val uri: String?,
)

@JsonClass(generateAdapter = true)
data class Relay(
    @field:Json(name = "Name") val name: String,
    @field:Json(name = "Protocol") val protocol: String,
    @field:Json(name = "Domain") val domain: String,
    @field:Json(name = "Port1") val port1: String?,
    @field:Json(name = "Port2") val port2: String?,
    @field:Json(name = "Uri") val uri: String?,
)

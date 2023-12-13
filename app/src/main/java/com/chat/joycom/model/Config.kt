package com.chat.joycom.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Config(
    @field:Json(name = "userid") val userid: Int?,
    @field:Json(name = "cookie") val cookie: String,
    @field:Json(name = "api") val api: Api,
    @field:Json(name = "websocket") val websocket: Websocket,
    @field:Json(name = "signal") val signal: Signal,
    @field:Json(name = "relay") val relay: Relay,
)

@JsonClass(generateAdapter = true)
data class Api(
    @field:Json(name = "Name") val name: String,
    @field:Json(name = "Protocol") val protocol: String,
    @field:Json(name = "Domain") val domain: String,
    @field:Json(name = "Port1") val port1: String?,
    @field:Json(name = "Port2") val port2: String?,
    @field:Json(name = "Uri") val uri: String?,
){
    //https://120.79.151.1:3389
    fun getDomainUrl():String = "https://120.79.151.1:3389" + uri
//    fun getDomainUrl():String = protocol + domain + uri
}

@JsonClass(generateAdapter = true)
data class Websocket(
    @field:Json(name = "Name") val name: String,
    @field:Json(name = "Protocol") val protocol: String,
    @field:Json(name = "Domain") val domain: String,
    @field:Json(name = "Port1") val port1: String?,
    @field:Json(name = "Port2") val port2: String?,
    @field:Json(name = "Uri") val uri: String?,
)

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

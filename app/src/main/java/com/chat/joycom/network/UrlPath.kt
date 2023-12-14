package com.chat.joycom.network

import com.chat.joycom.model.Config

object UrlPath {

    var config: Config = Config()

    const val LOGIN = "/user/login"

    const val SEND_SMS = "/sendSms"

    const val GET_FILE = "/file/get"

    const val QUERY_MEMBER = "/member"

    const val REGISTER = "/user/register"

    const val CONFIG = "/user/getRegionConfig"

    const val LOGOUT = "/member/logout"

    fun String.getFullUrlPath() = config.api?.getDomainUrl() + this

    fun getSocketFullUrl() = config.websocket?.getDomainUrl() ?: ""

    fun String.getFileFullUrl() = config.api?.getDomainUrl() + this + "?guid="
}
package com.chat.joycom.network

import com.chat.joycom.model.Config

object UrlPath {

    var config: Config? = null

    const val LOGIN = "/user/login"

    const val SEND_SMS = "/sendSms"

    const val GET_FILE = "/file/get"

    const val QUERY_MEMBER = "/member"

    const val REGISTER = "/user/register"

    fun String.getFullUrlPath(): String {
        return if (config != null) {
            return config?.api?.getDomainUrl() + this
        } else {
            this
        }
    }

    fun String.getFileFullUrl(): String {
        return if (config != null) {
            return config?.api?.getDomainUrl() + this + "?guid="
        } else {
            this
        }
    }
}
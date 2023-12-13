package com.chat.joycom.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CusCookieJar @Inject constructor() : CookieJar {
    private val list = mutableListOf<Cookie>()
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        Timber.d("loadForRequest $url")
        return list
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (url.toString().contains("/user/getRegionConfig")) {
            Timber.d("saveFromResponse $url")
            list.clear()
            list.addAll(cookies)
        }
    }
}
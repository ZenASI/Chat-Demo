package com.chat.joycom.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.chat.joycom.ds.DSKey
import com.chat.joycom.utils.DataStoreUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetCookieJar @Inject constructor(private val dataStoreUtils: DataStoreUtils) : CookieJar {
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val syncData = runBlocking {
            dataStoreUtils.readDataStoreValue(
                stringSetPreferencesKey(DSKey.COOKIE_SET_KEY),
                setOf()
            )
        }
        val list = mutableListOf<Cookie>()
        syncData.forEach {
            Cookie.parse(url, it)?.let { cookie ->
                list.add(cookie)
            }
        }
        return list
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (url.toString().contains(UrlPath.LOGIN)) {
            val cookieSet = HashSet<String>()
            cookies.forEach { cookieSet.add(it.toString()) }
            runBlocking {
                withTimeout(2000){
                    dataStoreUtils.saveDataStoreValue(stringSetPreferencesKey(DSKey.COOKIE_SET_KEY), cookieSet)
                }
            }
        }
    }
}
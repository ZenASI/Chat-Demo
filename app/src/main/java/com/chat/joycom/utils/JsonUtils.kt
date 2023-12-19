package com.chat.joycom.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import timber.log.Timber
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class JsonUtils @Inject constructor(
    val moshi: Moshi,
) {
    //    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> toJson(data: T?): String {
        return try {
            // 兩者都適用
//            val adapter = moshi.adapter<T>().lenient()
//            adapter.lenient().toJson(data)
            val adapter: JsonAdapter<T> = moshi.adapter(T::class.java)
            adapter.lenient().toJson(data)
        } catch (e: Exception) {
            Timber.d("moshi2Json: ${e.message}")
            ""
        }
    }

    //    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> fromJson(data: String): List<T> {
        return try {
            // 兩者都適用
//            val adapter = moshi.adapter<List<T>>().lenient()
//            val list: List<T> = adapter.fromJson(data)!!
//            list
            val type: Type = Types.newParameterizedType(
                MutableList::class.java,
                T::class.java
            )
            val adapter: JsonAdapter<List<T>> = moshi.adapter(type)
            adapter.fromJson(data)!!
        } catch (e: Exception) {
            Timber.d("fromJson: ${e.message}")
            emptyList<T>()
        }
    }
}
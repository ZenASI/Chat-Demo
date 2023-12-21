package com.chat.joycom.room

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.chat.joycom.model.Message
import com.squareup.moshi.Moshi
import timber.log.Timber
import javax.inject.Inject

@ProvidedTypeConverter
class MessageConverter @Inject constructor (moshi: Moshi) {
    @TypeConverter
    fun toJson(data: Message): String {
        Timber.d("toJson =>  $data")
        return ""
    }

    @TypeConverter
    fun fromJson(json: String): Message? {
        Timber.d("fromJson => $json")
        return null
    }
}
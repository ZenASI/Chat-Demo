package com.chat.joycom.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.model.GroupContact
import com.chat.joycom.model.Message

@Database(
    entities = [
        Contact::class,
        Group::class,
        Message::class,
        GroupContact::class,
    ],
    version = 1
)
abstract class RoomDB : RoomDatabase() {
    abstract fun getRoomDAO(): RoomDAO
}
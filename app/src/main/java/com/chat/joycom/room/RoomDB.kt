package com.chat.joycom.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group

@Database(
    entities = [
        Contact::class,
        Group::class
    ],
    version = 1
)
abstract class RoomDB : RoomDatabase() {
    abstract fun getRoomDAO(): RoomDAO
}
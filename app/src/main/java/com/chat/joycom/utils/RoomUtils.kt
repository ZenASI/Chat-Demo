package com.chat.joycom.utils

import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.room.RoomDAO
import com.chat.joycom.room.RoomDB
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomUtils @Inject constructor(
    private val dao: RoomDAO,
    private val db: RoomDB
) {

    fun clearAllTable() = db.clearAllTables()

    suspend fun insertContact(item: Contact) = dao.insertContact(item)

    suspend fun insertContact(list: List<Contact>?) = kotlin.run {
        list?.forEach {
            dao.insertContact(it)
        }
    }

    suspend fun insertGroup(item: Group) = dao.insertGroup(item)

    suspend fun insertGroup(list: List<Group>?) = kotlin.run {
        list?.forEach {
            dao.insertGroup(it)
        }
    }

     fun findAllGroup() = dao.findAllGroup()

     fun findAllContact() = dao.findAllContact()
}
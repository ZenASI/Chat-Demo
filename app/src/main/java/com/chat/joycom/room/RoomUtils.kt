package com.chat.joycom.room

import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomUtils @Inject constructor(
    private val dao: RoomDAO,
    private val db: RoomDB
) {
    private val TAG = this::class.java.simpleName

    suspend fun clearAllTable() = db.clearAllTables()

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
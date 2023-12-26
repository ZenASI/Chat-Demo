package com.chat.joycom.utils

import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.model.GroupContact
import com.chat.joycom.model.Message
import com.chat.joycom.room.RoomDAO
import com.chat.joycom.room.RoomDB
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomUtils @Inject constructor(
    private val dao: RoomDAO,
    val db: RoomDB
) {

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

    suspend fun insertMessage(item: Message) = dao.insertMessage(item)

    suspend fun insertMessage(list: List<Message>?) = kotlin.run {
        list?.forEach {
            dao.insertMessage(it)
        }
    }

    fun queryMessageByUserId(id: Long, selfId: Long) = dao.queryMessageByUserId(id, selfId)

    fun queryMessageByGroupId(groupId: Long) = dao.queryMessageByGroupId(groupId = groupId)

    suspend fun insertGroupContact(list: List<GroupContact>) = kotlin.run {
        list.forEach {
            dao.insertGroupContact(it)
        }
    }

    fun findGroupContact(groupId: Long) = dao.queryGroupContactById(groupId)

    fun pagingMessageByGroupId(groupId: Long, offset: Int, limit: Int) =
        dao.pagingMessageByGroupId(groupId = groupId, offset = offset, limit = limit)

    fun pagingMessageByUserId(selfId: Long, userId: Long, offset: Int, limit: Int) =
        dao.pagingMessageByUserId(selfId = selfId, userId = userId, offset = offset, limit = limit)
}
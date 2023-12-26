package com.chat.joycom.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
import com.chat.joycom.model.GroupContact
import com.chat.joycom.model.Message
import kotlinx.coroutines.flow.Flow


@Dao
interface RoomDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(item: Contact): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(item: Group): Long

    @Query("Select * From ${Group.TABLE_NAME}")
    fun findAllGroup(): Flow<List<Group>>

    @Query("Select * From ${Contact.TABLE_NAME}")
    fun findAllContact(): Flow<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(item: Message): Long

    @Query("Select * From ${Message.TABLE_NAME} Where (from_user_id = :id And to_user_id = :selfId) Or (from_user_id = :selfId And to_user_id = :id)")
    fun queryMessageByUserId(id: Long, selfId: Long): Flow<List<Message>>

    @Query("Select * From ${Message.TABLE_NAME} Where (from_user_id = :userId And to_user_id = :selfId) Or (from_user_id = :selfId And to_user_id = :userId) Order By id Desc Limit :limit Offset :offset")
    fun pagingMessageByUserId(selfId: Long, userId: Long, offset: Int, limit: Int): List<Message>

    @Query("Select * From ${Message.TABLE_NAME} Where to_group_id = :groupId")
    fun queryMessageByGroupId(groupId: Long): Flow<List<Message>>

    @Query("Select * From ${Message.TABLE_NAME} Where to_group_id = :groupId Order By id Desc Limit :limit Offset :offset")
    fun pagingMessageByGroupId(groupId: Long, offset: Int, limit: Int): List<Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupContact(item: GroupContact): Long

    @Query("Select * From ${GroupContact.TABLE_NAME} Where group_id = :groupId")
    fun queryGroupContactById(groupId: Long): Flow<List<GroupContact>>
}
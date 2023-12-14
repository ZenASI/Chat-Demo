package com.chat.joycom.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chat.joycom.model.Contact
import com.chat.joycom.model.Group
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
}
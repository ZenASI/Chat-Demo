package com.chat.joycom.room

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    private val dbName = "joycom.db"

    @Singleton
    @Provides
    fun provideRoomDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RoomDB::class.java, dbName)
            .build()


    @Singleton
    @Provides
    fun provideRoomDAO(db: RoomDB) = db.getRoomDAO()
}
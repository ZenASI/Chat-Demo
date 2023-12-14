package com.chat.joycom.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DataStoreUtils @Inject constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun <T> saveDataStoreValue(key: Preferences.Key<T>, value: T) = dataStore.edit { it[key] = value }

    suspend fun <T> readDataStoreValue(key: Preferences.Key<T>, defaultValue: T?): T {
        val result = coroutineScope {
            dataStore.data.mapNotNull {
                it[key] ?: defaultValue
            }.first()
        }
        return result
    }

    suspend fun clearAll() = runBlocking { dataStore.edit { it.clear() } }
}
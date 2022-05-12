package com.norbert.balazs.movies.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManager(private val datastore: DataStore<Preferences>) {

    suspend fun update(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        datastore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    fun observe(key: String): Flow<String?> = datastore.data.map { preferences ->
        preferences[stringPreferencesKey(key)]
    }
}
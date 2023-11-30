package com.dicoding.nyenyak.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionPreference private constructor(private val dataStore: DataStore<Preferences>){

    companion object {
        @Volatile
        private var INSTANCE: SessionPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    private val SESSION_KEY = booleanPreferencesKey("session")
    private val TOKEN_KEY = stringPreferencesKey("token")

    fun getSessionSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SESSION_KEY] ?: false
        }
    }

    fun getToken(): Flow<DataModel>{
        return dataStore.data.map { preferences ->
            DataModel(
                preferences[TOKEN_KEY] ?: ""
            )
        }
    }

    suspend fun saveSessionSetting(token : String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[SESSION_KEY] = true
        }
    }

    suspend fun sessiondestroy(){
        dataStore.edit { preference ->
            preference[TOKEN_KEY] = ""
            preference[SESSION_KEY] = false
        }
    }
}
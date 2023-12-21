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

        private val SESSION_KEY = booleanPreferencesKey("session")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val USER_ID = stringPreferencesKey("userId")

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getSessionSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SESSION_KEY] ?: false
        }
    }

    fun getToken(): Flow<DataModel>{
        return dataStore.data.map { preferences ->
            DataModel(
                preferences[TOKEN_KEY].toString(),
                preferences[NAME_KEY].toString(),
                preferences[USER_ID].toString(),
                preferences[SESSION_KEY] ?: false
            )
        }
    }

    suspend fun saveSessionSetting(user : DataModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = user.token
            preferences[NAME_KEY] = user.name
            preferences[USER_ID] = user.userId
            preferences[SESSION_KEY] = true
        }
    }

    suspend fun sessiondestroy(){
        dataStore.edit { preference ->
            preference[TOKEN_KEY] = ""
            preference[NAME_KEY] = ""
            preference[USER_ID] = ""
            preference[SESSION_KEY] = false
        }
    }
}
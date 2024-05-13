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
        private val EXPIRED_KEY = stringPreferencesKey("expire")

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    fun getToken(): Flow<DataModel>{
        return dataStore.data.map { preferences ->
            DataModel(
                preferences[TOKEN_KEY].toString(),
                preferences[EXPIRED_KEY].toString(),
                preferences[SESSION_KEY] ?: false
            )
        }
    }

    suspend fun saveSessionSetting(user : DataModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = user.token
            preferences[EXPIRED_KEY] = user.expireTime
            preferences[SESSION_KEY] = true
        }
    }

    suspend fun sessionDestroy(){
        dataStore.edit { preference ->
            preference[TOKEN_KEY] = ""
            preference[EXPIRED_KEY] = ""
            preference[SESSION_KEY] = false
        }
    }
}
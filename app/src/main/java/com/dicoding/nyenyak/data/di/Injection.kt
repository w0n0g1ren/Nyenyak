package com.dicoding.nyenyak.data.di

import android.content.Context
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): AppRepository = runBlocking  {
        val pref = SessionPreference.getInstance(context.datastore)
        val token = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiService(token.token)
        AppRepository.getInstance(apiService, pref)
    }
}
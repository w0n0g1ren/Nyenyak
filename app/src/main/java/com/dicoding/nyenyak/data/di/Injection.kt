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
        val apiService = ApiConfig.getApiService()
        AppRepository.getInstance(apiService, pref)
    }
}
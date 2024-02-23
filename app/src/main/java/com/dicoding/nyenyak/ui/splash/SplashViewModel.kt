package com.dicoding.nyenyak.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.session.DataModel
import com.dicoding.nyenyak.session.SessionPreference

class SplashViewModel(private val pref: SessionPreference) : ViewModel() {
    fun gettoken(): LiveData<DataModel> {
        return pref.getToken().asLiveData()
    }
}
package com.dicoding.nyenyak.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.session.DataModel

class SplashViewModel(private val repository: AppRepository) : ViewModel() {
    fun getSession(): LiveData<DataModel> {
        return repository.getSession().asLiveData()
    }
}
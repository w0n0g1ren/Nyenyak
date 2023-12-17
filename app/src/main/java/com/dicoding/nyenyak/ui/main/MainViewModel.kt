package com.dicoding.nyenyak.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.session.DataModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppRepository): ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
package com.dicoding.nyenyak.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.nyenyak.data.repository.AppRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AppRepository): ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
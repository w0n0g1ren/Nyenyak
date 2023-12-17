package com.dicoding.nyenyak.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.RegisterResponse

class RegisterViewModel(private val repository: AppRepository): ViewModel() {
    suspend fun register(email: String, password: String, name: String, gender: String, birthdate: String): RegisterResponse {
        return repository.register(email, password, name, gender, birthdate)
    }
}
package com.dicoding.nyenyak.ui.updatepassword

import androidx.lifecycle.ViewModel
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.ForgotResponse

class UpdatePasswordViewModel(private val repository: AppRepository): ViewModel() {
    suspend fun updatePassword(password: String): ForgotResponse{
        return repository.updatePassword(password)
    }
}

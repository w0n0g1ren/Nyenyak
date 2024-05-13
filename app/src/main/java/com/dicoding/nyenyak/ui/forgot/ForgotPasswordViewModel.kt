package com.dicoding.nyenyak.ui.forgot

import androidx.lifecycle.ViewModel
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.ForgotResponse

class ForgotPasswordViewModel(private val repository: AppRepository): ViewModel() {

    suspend fun forgot(email: String): ForgotResponse{
        return repository.forgot(email)
    }
}

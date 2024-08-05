package com.dicoding.nyenyak.ui.update

import androidx.lifecycle.ViewModel
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.UpdateUserResponse

class UpdateUserViewModel(private val repository: AppRepository): ViewModel() {

    suspend fun updateUser(name : String, birthDate: String, gender: String): UpdateUserResponse{
        return repository.updateUser(name, birthDate, gender)
    }
}

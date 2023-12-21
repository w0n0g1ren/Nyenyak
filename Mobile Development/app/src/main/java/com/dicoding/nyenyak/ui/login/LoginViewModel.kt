package com.dicoding.nyenyak.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.LoginResponse
import com.dicoding.nyenyak.session.DataModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AppRepository): ViewModel() {
    var isLoading: LiveData<Boolean> = repository.isLoading
    var loginResponse: MutableLiveData<LoginResponse> = repository.loginResponse

    fun login(email: String, password: String) {
        return repository.login(email, password)
    }

    fun saveSession(user: DataModel){
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
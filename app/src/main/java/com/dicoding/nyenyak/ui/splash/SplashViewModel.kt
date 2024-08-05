package com.dicoding.nyenyak.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.GetDetailUserResponse

class SplashViewModel(private val repository: AppRepository) : ViewModel() {

    var getDetailUserResponse: LiveData<GetDetailUserResponse> = repository.getDetailUserResponse

    fun getDetailUser(){
        return repository.getDetailUser()
    }
}
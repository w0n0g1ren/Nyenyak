package com.dicoding.nyenyak.ui.fragment.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.GetDetailUserResponse

class UserFragmentViewModel(private val repository: AppRepository): ViewModel() {

    var getDetailUserResponse: LiveData<GetDetailUserResponse> = repository.getDetailUserResponse

    fun getDetailUser(){
        return repository.getDetailUser()
    }

    suspend fun logout(){
        return repository.logout()
    }
}


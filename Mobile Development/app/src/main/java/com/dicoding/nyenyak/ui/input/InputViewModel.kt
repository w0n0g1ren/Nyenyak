package com.dicoding.nyenyak.ui.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.response.InputResponse
import com.dicoding.nyenyak.session.DataModel
import com.dicoding.nyenyak.session.SessionPreference

class InputViewModel(private val pref: SessionPreference): ViewModel() {

    fun gettoken(): LiveData<DataModel> {
        return pref.getToken().asLiveData()
        }
    }

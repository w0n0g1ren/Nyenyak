package com.dicoding.nyenyak.ui.fragment.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.nyenyak.session.DataModel
import com.dicoding.nyenyak.session.SessionPreference

class DashboardFragmentViewModel(private val pref : SessionPreference): ViewModel() {
    fun getToken(): LiveData<DataModel> {
        return pref.getToken().asLiveData()
    }
}
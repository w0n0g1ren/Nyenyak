package com.dicoding.nyenyak.ui.fragment.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.nyenyak.session.DataModel
import com.dicoding.nyenyak.session.SessionPreference
import kotlinx.coroutines.launch

class UserFragmentViewModel(private val pref: SessionPreference): ViewModel() {

    fun getToken(): LiveData<DataModel>{
        return pref.getToken().asLiveData()
    }

    fun destroySession(){
        viewModelScope.launch {
            pref?.sessiondestroy()
        }
    }
}
package com.dicoding.nyenyak

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DashboardViewModel {
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}
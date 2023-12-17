package com.dicoding.nyenyak.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ashboardViewModel {
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}
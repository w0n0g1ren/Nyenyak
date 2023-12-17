package com.dicoding.nyenyak.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.ui.detail.DetailViewModel
import com.dicoding.nyenyak.ui.input.InputViewModel
import com.dicoding.nyenyak.ui.login.LoginViewModel
import com.dicoding.nyenyak.ui.main.MainViewModel
import com.dicoding.nyenyak.ui.register.RegisterViewModel

class FragmentViewModelFactory(private val pref: SessionPreference) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListFragmentViewModel::class.java) -> {
                ListFragmentViewModel(pref) as T
            }

            modelClass.isAssignableFrom(DashboardFragmentViewModel::class.java) -> {
                DashboardFragmentViewModel(pref) as T
            }

            modelClass.isAssignableFrom(InputViewModel::class.java) ->{
                InputViewModel(pref) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) ->{
                DetailViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
package com.dicoding.nyenyak.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.ui.detail.DetailViewModel
import com.dicoding.nyenyak.ui.fragment.dashboard.DashboardFragmentViewModel
import com.dicoding.nyenyak.ui.fragment.list.ListFragmentViewModel
import com.dicoding.nyenyak.ui.fragment.user.UserFragmentViewModel
import com.dicoding.nyenyak.ui.input.InputViewModel
import com.dicoding.nyenyak.ui.splash.SplashViewModel
import com.dicoding.nyenyak.ui.update.UpdateUserViewModel

class SecondViewModelFactory(private val pref: SessionPreference) : ViewModelProvider.NewInstanceFactory() {
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

            modelClass.isAssignableFrom(UserFragmentViewModel::class.java) ->{
                UserFragmentViewModel(pref) as T
            }

            modelClass.isAssignableFrom(UpdateUserViewModel::class.java) ->{
                UpdateUserViewModel(pref) as T
            }

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->{
                SplashViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
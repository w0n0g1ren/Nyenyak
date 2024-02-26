package com.dicoding.nyenyak.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.nyenyak.data.di.Injection
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.ui.input.InputViewModel
import com.dicoding.nyenyak.ui.login.LoginViewModel
import com.dicoding.nyenyak.ui.main.MainViewModel
import com.dicoding.nyenyak.ui.register.RegisterViewModel
import com.dicoding.nyenyak.ui.splash.SplashViewModel

class ViewModelFactory(
    private val repository: AppRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }

        fun clearInstance() {
            AppRepository.clearInstance()
            INSTANCE = null
        }
    }
}
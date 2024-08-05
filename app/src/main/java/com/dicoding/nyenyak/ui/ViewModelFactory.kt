package com.dicoding.nyenyak.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.nyenyak.data.di.Injection
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.ui.detail.DetailViewModel
import com.dicoding.nyenyak.ui.forgot.ForgotPasswordViewModel
import com.dicoding.nyenyak.ui.fragment.calculator.CalculatorViewModel
import com.dicoding.nyenyak.ui.fragment.dashboard.DashboardFragmentViewModel
import com.dicoding.nyenyak.ui.fragment.list.ListFragmentViewModel
import com.dicoding.nyenyak.ui.fragment.user.UserFragmentViewModel
import com.dicoding.nyenyak.ui.input.InputViewModel
import com.dicoding.nyenyak.ui.login.LoginViewModel
import com.dicoding.nyenyak.ui.register.RegisterViewModel
import com.dicoding.nyenyak.ui.splash.SplashViewModel
import com.dicoding.nyenyak.ui.update.UpdateUserViewModel
import com.dicoding.nyenyak.ui.updatepassword.UpdatePasswordViewModel

class ViewModelFactory(
    private val repository: AppRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java) -> {
                ForgotPasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DashboardFragmentViewModel::class.java) -> {
                DashboardFragmentViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ListFragmentViewModel::class.java) -> {
                ListFragmentViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UserFragmentViewModel::class.java) -> {
                UserFragmentViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CalculatorViewModel::class.java) -> {
                CalculatorViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(InputViewModel::class.java) -> {
                InputViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpdatePasswordViewModel::class.java) -> {
                UpdatePasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpdateUserViewModel::class.java) -> {
                UpdateUserViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) ->{
                SplashViewModel(repository) as T
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
package com.dicoding.nyenyak.ui.splash

import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.response.GetDetailUserResponse
import com.dicoding.nyenyak.databinding.ActivitySplashBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.fragment.SecondViewModelFactory
import com.dicoding.nyenyak.ui.fragment.user.UserFragment
import com.dicoding.nyenyak.ui.login.LoginActivity
import com.dicoding.nyenyak.ui.main.MainActivity
import com.dicoding.nyenyak.ui.welcome.WelcomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    private lateinit var intent : Intent
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        delayTime()
        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun delayTime() {
        val pref = SessionPreference.getInstance(application.datastore)
        val viewmodel = ViewModelProvider(this,SecondViewModelFactory(pref)).get(
            SplashViewModel::class.java
        )

        lifecycleScope.launchWhenResumed {
            delay(2000)
            viewmodel.gettoken().observe(this@SplashActivity){
                val apiService = ApiConfig.getApiService(it.token).getUser()
                apiService.enqueue(object : Callback<GetDetailUserResponse>{
                    override fun onResponse(
                        call: Call<GetDetailUserResponse>,
                        response: Response<GetDetailUserResponse>
                    ) {
                        if (response.isSuccessful){
                            intent = Intent(this@SplashActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        else{
                            intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<GetDetailUserResponse>, t: Throwable) {
                        Log.e(SplashActivity.TAG, "onFailure: ${t.message}")
                    }

                })
            }
        }
    }

//    private fun navigateToMain() {
//        val intentToMain = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        }
//        startActivity(intentToMain)
//    }
//
//    private fun navigateToWelcome() {
//        val intentToWelcome = Intent(this, WelcomeActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        }
//        startActivity(intentToWelcome)
//    }

    companion object{
        private const val TAG = "SplashActivity"
    }
}
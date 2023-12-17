package com.dicoding.nyenyak.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.databinding.ActivitySplashBinding
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.main.MainActivity
import com.dicoding.nyenyak.ui.welcome.WelcomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this)
    }

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
        lifecycleScope.launchWhenResumed {
            delay(2000)
            viewModel.getSession().observe(this@SplashActivity) { user ->
                if (!user.isLogin) {
                    navigateToWelcome()
                    finish()
                } else {
                    navigateToMain()
                }
            }
        }
    }

    private fun navigateToMain() {
        val intentToMain = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intentToMain)
    }

    private fun navigateToWelcome() {
        val intentToWelcome = Intent(this, WelcomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intentToWelcome)
    }
}
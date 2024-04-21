package com.dicoding.nyenyak.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.api.ApiService
import com.dicoding.nyenyak.data.repository.AppRepository
import com.dicoding.nyenyak.data.repository.ModelProto
import com.dicoding.nyenyak.data.response.LoginResponse
import com.dicoding.nyenyak.databinding.ActivityLoginBinding
import com.dicoding.nyenyak.session.DataModel
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.forgot.ForgotPasswordActivity
import com.dicoding.nyenyak.ui.main.MainActivity
import com.dicoding.nyenyak.ui.welcome.WelcomeActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var message: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
        setClickListener()

    }

    private fun setClickListener() {
        binding.ivBackBtn.setOnClickListener {
            navigateToBack()
        }
        binding.tvForgotPwdKlik.setOnClickListener {
            navigateToForgot()
        }
    }

    private fun navigateToBack() {
        val intentToWelcome = Intent(this, WelcomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intentToWelcome)
    }

    private fun navigateToForgot(){
        startActivity(Intent(this,ForgotPasswordActivity::class.java))
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

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            showLoading(true)
            try {
                val email = binding.layoutSignForm.editTextEmail.text.toString()
                val password = binding.layoutSignForm.editTextPassword.text.toString()

                when {
                    email.isEmpty() -> binding.layoutSignForm.editTextEmail.error = getString(R.string.alert_email_login)
                    password.isEmpty() -> binding.layoutSignForm.editTextPassword.error = getString(R.string.alert_password_login)
                }
                viewModel.login(email, password)

                viewModel.loginResponse.observe(this) {
                    when{
                        (it.status == "success") -> {
                            save(
                                DataModel(
                                    it.token.toString(),
                                    it.message.toString(),
                                    it.expirateTime.toString(),
                                    true
                                )
                            )
                            showLoading(false)
                            showToast(it.message)
                        }
                        (it.status != "success") ->{
                            showLoading(false)
                            showToast(it.message)
                        }
                    }
                }
            } catch (e: HttpException) {
                showLoading(false)

            }
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading == true) View.VISIBLE else View.GONE

    }

    private fun save(session: DataModel) {
        lifecycleScope.launch {
            viewModel.saveSession(session)

            startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            ViewModelFactory.clearInstance()
        }
    }

    private fun showToast(message: String?) {
        Log.e("toast", message.toString())
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
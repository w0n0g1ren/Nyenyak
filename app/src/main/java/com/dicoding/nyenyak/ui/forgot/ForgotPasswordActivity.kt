package com.dicoding.nyenyak.ui.forgot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.response.RegisterResponse
import com.dicoding.nyenyak.databinding.ActivityForgotPasswordBinding
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.welcome.WelcomeActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel by viewModels<ForgotPasswordViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupAction()
        anotherButton()
    }

    private fun anotherButton() {
        binding.ivBackBtn.setOnClickListener{
            val intent = Intent(this@ForgotPasswordActivity,WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun setupAction() {
        binding.btnForgot.setOnClickListener {
            var email = binding.editTextEmail.text.toString()

            when{
                email.isEmpty() -> binding.editTextEmail.error = getString(R.string.alert_email)
                Patterns.EMAIL_ADDRESS.matcher(email).
                matches().not() -> binding.editTextEmail.error = getString(R.string.alert_email_pattern)
                else ->
                    lifecycleScope.launch {
                    try {
                        val response = viewModel.forgot(email)
                        showLoading(false)
                        showToast(response.message)
                        val intent = Intent(this@ForgotPasswordActivity,
                            WelcomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } catch (e: HttpException){
                        showLoading(false)
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                        showToast(errorResponse.message)
                    }
                }
            }


        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnForgot.isEnabled = !isLoading
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
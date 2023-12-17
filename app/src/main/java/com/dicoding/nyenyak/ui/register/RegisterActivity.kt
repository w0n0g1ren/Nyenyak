package com.dicoding.nyenyak.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.data.response.RegisterResponse
import com.dicoding.nyenyak.databinding.ActivityRegisterBinding
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.login.LoginActivity
import com.dicoding.nyenyak.ui.welcome.WelcomeActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
        setClickListener()
    }

    private fun setClickListener() {
        binding.ivBackBtn.setOnClickListener {
            navigateToBack()
        }
    }

    private fun navigateToBack() {
        val intentToWelcome = Intent(this, WelcomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intentToWelcome)
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
        binding.btnRegister.setOnClickListener {
            showLoading(true)
            val name = binding.layoutSignForm.editTextName.text.toString()
            val email = binding.layoutSignForm.editTextEmail.text.toString()
            val password = binding.layoutSignForm.editTextPassword.text.toString()
            val gender = binding.layoutSignForm.editTextGender.text.toString()
            val birthdate = binding.layoutSignForm.editTextBirthDate.text.toString()

            when {
                name.isEmpty() -> binding.layoutSignForm.editTextName.error = "Nama harus diisi!"
                email.isEmpty() -> binding.layoutSignForm.editTextEmail.error = "Email harus diisi!"
                password.isEmpty() -> binding.layoutSignForm.editTextPassword.error = "Password harus diisi!"
                gender.isEmpty() -> binding.layoutSignForm.editTextGender.error = "Jenis Kelamin harus diisi!"
                birthdate.isEmpty() -> binding.layoutSignForm.editTextBirthDate.error = "Tanggal Lahir harus diisi!"
            }

            lifecycleScope.launch {
                try {
                    val response = viewModel.register(email, password, name, gender, birthdate)
                    showLoading(false)
                    showToast(response.message)
                    AlertDialog.Builder(this@RegisterActivity).apply {
                        setTitle("Selamat!")
                        setMessage("Registrasi berhasil. Siap untuk persiapkan karir?")
                        setPositiveButton("Ya") { _, _ ->
                            val intent = Intent(context, LoginActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                } catch (e: HttpException) {
                    showLoading(false)
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    showToast(errorResponse.message)
                }
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !isLoading
    }
}
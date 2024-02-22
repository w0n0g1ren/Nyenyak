package com.dicoding.nyenyak.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.response.RegisterResponse
import com.dicoding.nyenyak.databinding.ActivityRegisterBinding
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.login.LoginActivity
import com.dicoding.nyenyak.ui.welcome.WelcomeActivity
import com.dicoding.nyenyak.utils.DatePickerFragment
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener{
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var dueDateMillis: Long = System.currentTimeMillis()
    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
        setClickListener()
        setupDropdownGender()
    }

    private fun setupDropdownGender() {
        val itemsGender = listOf("Male", "Female")
        val autoComplete: AutoCompleteTextView = findViewById(R.id.ac_gender)
        val adapterGender = ArrayAdapter(this, R.layout.list_item, itemsGender)

        autoComplete.setAdapter(adapterGender)
        autoComplete.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView, view, i, l ->
            selectedGender = adapterView.getItemAtPosition(i).toString()
        }
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
            val gender = selectedGender ?: ""
            val birthdate = binding.layoutSignForm.addTvDueDate.text.toString()

            when {
                name.isEmpty() -> binding.layoutSignForm.editTextName.error = getString(R.string.alert_nama)
                email.isEmpty() -> binding.layoutSignForm.editTextEmail.error = getString(R.string.alert_email)
                password.isEmpty() -> binding.layoutSignForm.editTextPassword.error = getString(R.string.alert_password)
                gender.isEmpty() -> binding.layoutSignForm.acGender.error = getString(R.string.alert_gender)
                birthdate == getString(R.string.due_date) -> false
            }

            lifecycleScope.launch {
                try {
                    val response = viewModel.register(email, password, name, gender, birthdate)
                    showLoading(false)
                    showToast(response.message)
                    AlertDialog.Builder(this@RegisterActivity).apply {
                        setTitle(getString(R.string.selamat))
                        setMessage(getString(R.string.registrasi_berhasil))
                        setPositiveButton(getString(R.string.ya)) { _, _ ->
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

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }
}
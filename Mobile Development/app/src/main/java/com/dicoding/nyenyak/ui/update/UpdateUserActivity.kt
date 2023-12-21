package com.dicoding.nyenyak.ui.update

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.response.InputResponse
import com.dicoding.nyenyak.databinding.ActivityUpdateUserBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.fragment.FragmentViewModelFactory
import com.dicoding.nyenyak.ui.main.MainActivity
import com.dicoding.nyenyak.utils.DatePickerFragment
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateUserActivity : AppCompatActivity(),DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var binding: ActivityUpdateUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = SessionPreference.getInstance(application.datastore)
        val viewModel = ViewModelProvider(this, FragmentViewModelFactory(pref)).get(
            UpdateUserViewModel::class.java
        )

        binding.ivUpdateUser.setOnClickListener {
            showDatePickerUser()
        }

        binding.btnUpdateUser.setOnClickListener {
            var nama : String
            var tanggal : String
            var gender : String

            nama = binding.namaUpdateUser.text.toString()
            tanggal = binding.tanggalUpdateUser.text.toString()

            if (binding.radiobutton1.isChecked){
                gender = binding.radiobutton1.text.toString()
            }else if (binding.radiobutton2.isChecked){
                gender = binding.radiobutton2.text.toString()
            }else{
                gender = ""
            }

            viewModel.getToken().observe(this){
                if (it.token != null){
                    lifecycleScope.launch {
                        try {
                            val config = ApiConfig.getApiService(it.token)
                            val response = config.updateUser(nama,tanggal,gender)
                            showToast(response.message.toString())
                            val intent = Intent(this@UpdateUserActivity,MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }catch (e : HttpException){
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorResponse = Gson().fromJson(errorBody, InputResponse::class.java)
                            showToast(errorResponse.message.toString())
                        }
                    }
                }
            }
        }
    }

    fun showDatePickerUser() {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.tanggal_update_user).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
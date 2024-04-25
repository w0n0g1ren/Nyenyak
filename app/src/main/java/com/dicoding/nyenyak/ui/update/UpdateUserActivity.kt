package com.dicoding.nyenyak.ui.update

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.compose.animation.core.animate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.response.InputResponse
import com.dicoding.nyenyak.databinding.ActivityUpdateUserBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.SecondViewModelFactory
import com.dicoding.nyenyak.ui.main.MainActivity
import com.dicoding.nyenyak.utils.DatePickerFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateUserActivity : AppCompatActivity(),DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var binding: ActivityUpdateUserBinding
    private lateinit var tanggalInput: String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        showLoading(false)

        binding.ivUpdateUser.setOnClickListener {
            showDatePickerUserAlt()
//            showDatePickerUser()
        }
        binding.hintUpdateUser.setOnClickListener{
            setOnClickListener(R.id.hint_update_user, R.string.petunjuk,
                R.string.jika_data_tidak_diisi_akan_diisikan_dengan_data_lama)
        }

        binding.btnUpdateUser.setOnClickListener {
            var nama : String
            var tanggal : String
            var gender : String

            nama = binding.namaUpdateUser.text.toString()
            tanggal = tanggalInput

            if (binding.radiobutton1.isChecked){
                gender = "male"
            }else if (binding.radiobutton2.isChecked){
                gender = "female"
            }else{
                gender = ""
            }

            val pref = SessionPreference.getInstance(application.datastore)
            val viewModel = ViewModelProvider(this, SecondViewModelFactory(pref)).get(
                UpdateUserViewModel::class.java
            )
            viewModel.getToken().observe(this){
                if (it.token != null){
                    showLoading(true)
                    lifecycleScope.launch {
                        try {
                            val config = ApiConfig.getApiService(it.token)
                            val response = config.updateUser(nama,tanggal,gender)
                            showToast(response.message.toString())
                            val intent = Intent(this@UpdateUserActivity,MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            showLoading(false)
                            startActivity(intent)
                        }catch (e : HttpException){
                            val errorBody = e.response()?.errorBody()?.string()
                            val errorResponse = Gson().fromJson(errorBody, InputResponse::class.java)
                            showToast(errorResponse.message.toString())
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerUserAlt() {
        val dialogView = layoutInflater.inflate(R.layout.date_picker_alt,null)
        val dialog = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        val window = dialog.window ?:return
        val params = window.attributes // Dapatkan parameter jendela
        params.dimAmount = 0.7f
        window.attributes = params
        val datePickerSpinner = dialog.findViewById<DatePicker>(R.id.date_alt)
        datePickerSpinner?.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, monthOfYear)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val formattedDate1 = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)
            val formattedDate2 = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(selectedDate.time)
            tanggalInput = formattedDate1
            findViewById<TextView>(R.id.tanggal_update_user).text = formattedDate2
        }
        dialog.show()
    }

    private fun setOnClickListener(viewId: Int, titleId:Int, messageId: Int) {
        findViewById<View>(viewId).setOnClickListener {
            showInfoDialog(titleId, messageId)
        }
    }

    private fun showInfoDialog(titleId:Int, messageId: Int) {
        AlertDialog.Builder(this, R.style.RoundedMaterialDialog)
            .setTitle(getString(titleId))
            .setMessage(getString(messageId))
            .setPositiveButton(getString(R.string.okey)) { _, _ ->
                // do nothing
            }.create().show()
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressUpdateUser.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressUpdateUser.isEnabled = !isLoading
    }
}
package com.dicoding.nyenyak.ui.input

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.response.InputResponse
import com.dicoding.nyenyak.databinding.ActivityInputBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.fragment.FragmentViewModelFactory
import com.dicoding.nyenyak.ui.login.LoginActivity
import com.dicoding.nyenyak.ui.result.ResultActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    private var sldsleep: Int = 3
    private var sldstress: Int = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        showLoading(false)

        setOnClickListenerDialogInfoItem()

        binding.sliderratingstres.addOnChangeListener { slider, value, fromUser ->
            sldstress = value.toInt()
        }
        binding.sliderratingtidur.addOnChangeListener { slider, value, fromUser ->
            sldsleep = value.toInt()
        }

        binding.btnInput.setOnClickListener {

            var weight = binding.etBbInput.text.toString().toInt()
            var height = binding.etTinggiInput.text.toString().toInt()
            var sleepDuration = binding.etTidurInput.text.toString().toFloat()
            var bloodPressure = binding.spinnerbpinput.selectedItem.toString()
            var heartRate = binding.etJantungInput.text.toString().toInt()
            var dailySteps = binding.etLangkahInput.text.toString().toInt()
            var physicalActivityLevel = binding.etFisikInput.text.toString().toInt()

            val pref = SessionPreference.getInstance(application.datastore)
            val viewmodel = ViewModelProvider(this, FragmentViewModelFactory(pref)).get(
                InputViewModel::class.java
            )
            viewmodel.gettoken().observe(this){
            if(it.token != null){
                lifecycleScope.launch {
                    try {
                        showLoading(true)
                        val apiService = ApiConfig.getApiService(it.token)
                        val inputResponse = apiService.inputDiagnosis(
                            weight,height,sleepDuration,sldsleep,physicalActivityLevel,bloodPressure,sldstress,heartRate,dailySteps
                        )

                        showToast(inputResponse.message.toString())
                        val intent = Intent(this@InputActivity,ResultActivity::class.java)
                        intent.putExtra("tanggal", inputResponse.newDiagnosis?.date.toString())
                        intent.putExtra("diagnosis", inputResponse.newDiagnosis?.sleepDisorder.toString())
                        intent.putExtra("solusi", inputResponse.newDiagnosis?.solution.toString())
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        showLoading(false)
                        startActivity(intent)

                    }catch (e: HttpException){
                        showLoading(true)
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, InputResponse::class.java)
                        showToast(errorResponse.message.toString())
                        showLoading(false)
                        startActivity(Intent(this@InputActivity,LoginActivity::class.java))
                    }
                }
            }
            }
        }
    }

    private fun setOnClickListenerDialogInfoItem() {
        setOnClickListener(R.id.iv_tinggi_dialog, R.string.tinggi, R.string.tinggi_info)
        setOnClickListener(R.id.iv_berat_dialog, R.string.berat_badan, R.string.berat_info)
        setOnClickListener(R.id.iv_durasi_dialog, R.string.durasi_tidur, R.string.durasi_info)
        setOnClickListener(R.id.iv_rating_tidur_dialog, R.string.rating_tidur, R.string.rating_tidur_info)
        setOnClickListener(R.id.iv_rating_stres_dialog, R.string.rating_stres, R.string.rating_stres_info)
        setOnClickListener(R.id.iv_rating_aktivitas_dialog, R.string.rating_aktivitas_fisik, R.string.aktivitas_fisik_info)
        setOnClickListener(R.id.iv_tekanan_darah_dialog, R.string.tekanan_darah, R.string.tekanan_darah_info)
        setOnClickListener(R.id.iv_detak_jantung_dialog, R.string.detak_jantung, R.string.detak_jantung_info)
        setOnClickListener(R.id.iv_langkah_harian_dialog, R.string.langkah_harian, R.string.langkah_info)
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
            .setPositiveButton("Okey") { _, _ ->
                // do nothing
            }.create().show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressInput.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressInput.isEnabled = !isLoading
    }
}
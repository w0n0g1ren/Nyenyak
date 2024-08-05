package com.dicoding.nyenyak.ui.input

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.response.InputResponse
import com.dicoding.nyenyak.databinding.ActivityInputBinding
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.result.ResultActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    private var sldsleep: Int = 3
    private var sldstress: Int = 3
    private var bloodpressure: String = ""
    private val viewModel by viewModels<InputViewModel> {
        ViewModelFactory.getInstance(this)
    }
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
        binding.spinnerbpinput.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem,
                                                                          newIndex, newText ->
            bloodpressure = newText
        }

        binding.btnInput.setOnClickListener {
            showLoading(true)
            var weight = binding.etBbInput.text.toString().toIntOrNull()
            var height = binding.etTinggiInput.text.toString().toIntOrNull()
            var sleepDuration = binding.etTidurInput.text.toString().toFloatOrNull()
            var heartRate = binding.etJantungInput.text.toString().toIntOrNull()
            var dailySteps = binding.etLangkahInput.text.toString().toIntOrNull()
            var physicalActivityLevel = binding.etFisikInput.text.toString().toIntOrNull()

            when {
                weight == null -> {binding.etBbInput.error = getString(R.string.error_input)}
                height == null -> {binding.etTinggiInput.error = getString(R.string.error_input)}
                sleepDuration == null -> {binding.etTidurInput.error = getString(R.string.error_input)}
                heartRate == null -> {binding.etJantungInput.error = getString(R.string.error_input)}
                dailySteps == null -> {binding.etLangkahInput.error = getString(R.string.error_input)}
                physicalActivityLevel == null -> {binding.etFisikInput.error = getString(R.string.error_input)}
                bloodpressure == null -> {binding.spinnerbpinput.error = getString(R.string.error_input)}
            }
            if (weight==null || height == null || sleepDuration == null ||
                heartRate == null || dailySteps == null || physicalActivityLevel == null){
                showToast(getString(R.string.peringatan))
            }else{
                val scope = CoroutineScope(SupervisorJob() + IO)
                lifecycleScope.launch() {
                    try {
                        showLoading(true)
                        val response = viewModel.inputDiagnosis(
                            weight,height,sleepDuration,
                            sldsleep,physicalActivityLevel,
                            bloodpressure,sldstress,
                            heartRate,dailySteps)

                        showToast(response.message.toString())
                        val intent = Intent(this@InputActivity,
                            ResultActivity::class.java)
                        intent.putExtra("tanggal", response.newDiagnosis?.
                        date.toString())
                        intent.putExtra("diagnosis", response.newDiagnosis?.
                        sleepDisorder.toString())
                        intent.putExtra("solusi", response.newDiagnosis?.
                        solution.toString())
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.
                        FLAG_ACTIVITY_NEW_TASK
                        showLoading(false)
                        startActivity(intent)

                    }catch (e: HttpException){
                        showLoading(true)
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody,
                            InputResponse::class.java)
                        showToast(errorResponse.message.toString())
                        showLoading(false)
                    }catch (e: TimeoutException){
                        showLoading(true)
                        showToast("Server tidak menanggapi mohon coba lagi")
                        showLoading(false)
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
            .setPositiveButton(getString(R.string.okey)) { _, _ ->
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
package com.dicoding.nyenyak.ui.input

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.response.InputResponse
import com.dicoding.nyenyak.databinding.ActivityInputBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.FragmentViewModelFactory
import com.dicoding.nyenyak.ui.login.LoginActivity
import com.dicoding.nyenyak.ui.result.ResultActivity
import com.google.android.material.slider.Slider
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnInput.setOnClickListener {

            var physicalActivityLevel : Int = 3
            var sldsleep: Int = 3
            var sldstress: Int = 3

            binding.sliderratingstres.addOnChangeListener { slider, value, fromUser ->
                sldstress = value.toString().toInt()
            }
            binding.sliderratingtidur.addOnChangeListener { slider, value, fromUser ->
                sldsleep = value.toString().toInt()
            }
            binding.sliderratingphyisical.addOnChangeListener { slider, value, fromUser ->
                physicalActivityLevel = value.toString().toInt()
            }
            var weight = binding.etBbInput.text.toString().toInt()
            var height = binding.etTinggiInput.text.toString().toInt()
            var sleepDuration = binding.etTidurInput.text.toString().toFloat()
            var bloodPressure = binding.spinnerbpinput.selectedItem.toString()
            var heartRate = binding.etJantungInput.text.toString().toInt()
            var dailySteps = binding.etLangkahInput.text.toString().toInt()


            val pref = SessionPreference.getInstance(application.datastore)
            val viewmodel = ViewModelProvider(this,FragmentViewModelFactory(pref)).get(
                InputViewModel::class.java
            )
            viewmodel.gettoken().observe(this){
            if(it.token != null){
                lifecycleScope.launch {
                    try {
                        val apiService = ApiConfig.getApiService(it.token)


//                      binding.sldTidurInput.addOnChangeListener { slider, value, fromUser ->
//                      sldsleep = value.toString().toInt()
//                      }
//                      binding.sldStresInput.addOnChangeListener { slider, value, fromUser ->
//                      sldstress = value.toString().toInt()
//                      }
                        val inputResponse = apiService.inputDiagnosis(
                            weight,height,sleepDuration,sldsleep,physicalActivityLevel,bloodPressure,sldstress,heartRate,dailySteps
                        )

                        showToast(inputResponse.message.toString())
                        val intent = Intent(this@InputActivity,ResultActivity::class.java)
                        intent.putExtra("tanggal", inputResponse.newDiagnosis?.date.toString())
                        intent.putExtra("diagnosis", inputResponse.newDiagnosis?.sleepDisorder.toString())
                        intent.putExtra("solusi", inputResponse.newDiagnosis?.solution.toString())
                        startActivity(intent)

                    }catch (e: HttpException){
                        val errorBody = e.response()?.errorBody()?.string()
                        if (errorBody == "401"){
                            val intent = Intent(this@InputActivity,LoginActivity::class.java)
                            val errorResponse = Gson().fromJson(errorBody, InputResponse::class.java)
                            showToast(errorResponse.message.toString())
                            startActivity(intent)
                        }

                    }
                }
            }
            }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
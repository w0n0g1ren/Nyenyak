package com.dicoding.nyenyak.ui.input

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.data.ApiConfig
import com.dicoding.nyenyak.data.InputResponse
import com.dicoding.nyenyak.databinding.ActivityInputBinding
import com.dicoding.nyenyak.ui.login.LoginActivity
import com.dicoding.nyenyak.ui.main.MainActivity
import com.dicoding.nyenyak.ui.result.ResultActivity
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

            var sldsleep: Int = 3
            var sldstress: Int = 3
            var gender = "Male"
            var age = 25
            var weight = 70
            var height = 160
            var sleepDuration: Float = 7.5F
            var physicalActivityLevel = 5
            var bloodPressure = "stage 1"
            var heartRate = 80
            var dailySteps = 5000

            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()

//                binding.sldTidurInput.addOnChangeListener { slider, value, fromUser ->
//                     sldsleep = value.toString().toInt()
//                }
//                binding.sldStresInput.addOnChangeListener { slider, value, fromUser ->
//                    sldstress = value.toString().toInt()
//                }

                    val Inputresponse = apiService.inputDiagnosis(
                        weight,height,sleepDuration,sldsleep,physicalActivityLevel,bloodPressure,sldstress,heartRate,dailySteps
                    )
                    showToast(Inputresponse.message.toString())
                    val intent = Intent(this@InputActivity,ResultActivity::class.java)
                    intent.putExtra("tanggal", Inputresponse.newDiagnosis?.date.toString())
                    intent.putExtra("diagnosis", Inputresponse.newDiagnosis?.sleepDisorder.toString())
                    intent.putExtra("solusi", Inputresponse.newDiagnosis?.solution.toString())
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
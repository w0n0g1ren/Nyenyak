package com.dicoding.nyenyak.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem
import com.dicoding.nyenyak.databinding.ActivityDetailBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.fragment.FragmentViewModelFactory
import com.dicoding.nyenyak.ui.main.MainActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = SessionPreference.getInstance(application.datastore)
        val viewModel = ViewModelProvider(this, FragmentViewModelFactory(pref)).get(
            DetailViewModel::class.java)

        var id = intent.getStringExtra("uid")

        viewModel.getToken().observe(this){
            if (it.token != null){
                val client = ApiConfig.getApiService(it.token).getdetaildiagnosis(id.toString())
                client.enqueue(object : Callback<GetDiagnosisResponseItem>{
                    override fun onResponse(
                        call: Call<GetDiagnosisResponseItem>,
                        response: Response<GetDiagnosisResponseItem>
                    ) {
                        if (response != null){
                            val responseBody = response.body()
                            if (responseBody != null){
                                binding.tvBmiDetail.text = responseBody.bMIcategory
                                binding.tvUmurDetail.text = responseBody.age.toString()
                                binding.tvTekdarahDetail.text = responseBody.bloodPressure
                                binding.tvLangkahDetail.text = responseBody.dailySteps.toString()
                                binding.tvJantungDetail.text = responseBody.heartRate.toString()
                                binding.tvPhysicsDetail.text = responseBody.physicalActivityLevel.toString()
                                binding.tvDurasiDetail.text = responseBody.sleepDuration.toString()
                                binding.tvStressDetail.text = responseBody.stressLevel.toString()
                                binding.tvPenyakitDetail.text = responseBody.sleepDisorder
                                binding.tvSolusiDetail.text = responseBody.solution.toString()
                                binding.tvTanggalDetail.text = responseBody.date
                            }
                        }
                    }

                    override fun onFailure(call: Call<GetDiagnosisResponseItem>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }
        binding.btnDeleteDetail.setOnClickListener {


            viewModel.getToken().observe(this){
                if(it.token != null){
                    lifecycleScope.launch {
                        try {
                            val apiService = ApiConfig.getApiService(it.token).deletediagnosis(id.toString())
                            val message = apiService.message
                            Toast.makeText(this@DetailActivity,message.toString(),Toast.LENGTH_LONG).show()
                            val intent = Intent(this@DetailActivity,MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }catch (e:HttpException){

                        }
                    }
                }
            }
        }
    }
}
package com.dicoding.nyenyak.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.data.response.RegisterResponse
import com.dicoding.nyenyak.databinding.ActivityDetailBinding
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.login.LoginActivity
import com.dicoding.nyenyak.ui.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var status: String = ""
    private var message: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        showLoading(false)

        var id = intent.getStringExtra("uid")

        viewModel.getDetailDiagnosis(id.toString())
        viewModel.getDetailDiagnosisResponse.observe(this){
            showLoading(true)

            if (it.status == "failed"){
                showLoading(false)
                Toast.makeText(this@DetailActivity,it.message,
                    Toast.LENGTH_LONG).show()
                startActivity(Intent(this@DetailActivity,
                    LoginActivity::class.java))
            }else{
                binding.tvBmiDetail.text = it.bMIcategory
                binding.tvUmurDetail.text = it.age.toString()
                binding.tvTekdarahDetail.text = it.bloodPressure
                binding.tvLangkahDetail.text = it.dailySteps.toString()
                binding.tvJantungDetail.text = it.heartRate.toString()
                binding.tvPhysicsDetail.text = it.
                physicalActivityLevel.toString()
                binding.tvDurasiDetail.text = it.
                sleepDuration.toString()
                binding.tvStressDetail.text = it.
                stressLevel.toString()
                binding.tvPenyakitDetail.text = it.sleepDisorder
                binding.tvSolusiDetail.text = it.
                solution.toString()
                binding.tvTanggalDetail.text = it.date
                showLoading(false)
            }
        }

        binding.btnDeleteDetail.setOnClickListener {
            showLoading(true)
            lifecycleScope.launch {
                try {
                    val response = viewModel.deleteDiagnosis(id.toString())
                    showLoading(false)
                    showToast(response.message)
                    val intent = Intent(this@DetailActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } catch (e: HttpException){
                    showLoading(false)
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    showToast(errorResponse.message)
                    val intent = Intent(this@DetailActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressDetail.isEnabled = !isLoading
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private const val TAG = "DetailActivity"
    }
}
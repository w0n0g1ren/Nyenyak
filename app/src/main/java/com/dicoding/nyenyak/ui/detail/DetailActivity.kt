package com.dicoding.nyenyak.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.ApiConfig
import com.dicoding.nyenyak.data.ApiService
import com.dicoding.nyenyak.data.GetDiagnosisResponseItem
import com.dicoding.nyenyak.databinding.ActivityDetailBinding
import com.dicoding.nyenyak.ui.main.MainActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import javax.security.auth.callback.Callback

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tvBmiDetail.text = intent.getStringExtra("indeksmassa")
        binding.tvUmurDetail.text = intent.getIntExtra("umur",0).toString()
        binding.tvTekdarahDetail.text = intent.getStringExtra("tekdarah")
        binding.tvLangkahDetail.text = intent.getIntExtra("langkah",0).toString()
        binding.tvJantungDetail.text = intent.getIntExtra("jantung",0).toString()
        binding.tvPhysicsDetail.text = intent.getIntExtra("fisik",0).toString()
        binding.tvDurasiDetail.text = intent.getIntExtra("tidur",0).toString()
        binding.tvStressDetail.text = intent.getIntExtra("stres",0).toString()
        binding.tvPenyakitDetail.text = intent.getStringExtra("sleepdisorder").toString()
        binding.tvSolusiDetail.text = intent.getStringExtra("solusi").toString()
        binding.tvTanggalDetail.text = intent.getStringExtra("tanggal").toString()
        var id = intent.getStringExtra("uid")
        binding.btnDeleteDetail.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService().deletediagnosis(id.toString())
                    val message = apiService.message
                    Toast.makeText(this@DetailActivity,message.toString(),Toast.LENGTH_LONG).show()
                    val intent = Intent(this@DetailActivity,MainActivity::class.java)
                    startActivity(intent)
                }catch (e:HttpException){

                }
            }

        }
    }
}
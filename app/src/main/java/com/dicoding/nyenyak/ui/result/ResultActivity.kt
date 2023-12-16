package com.dicoding.nyenyak.ui.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.databinding.ActivityResultBinding
import com.dicoding.nyenyak.ui.main.MainActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.tvTanggalResult.text = intent.getStringExtra("tanggal").toString()
        binding.tvDiagnosisResult.text = intent.getStringExtra("diagnosis").toString()
        binding.tvSolusiResult.text = intent.getStringExtra("solusi").toString()

        binding.btnLanjutResult.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}
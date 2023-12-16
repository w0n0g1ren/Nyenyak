package com.dicoding.nyenyak.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
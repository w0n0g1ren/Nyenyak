package com.dicoding.nyenyak.ui.setelan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.nyenyak.databinding.ActivitySettingsBinding
import com.dicoding.nyenyak.ui.update.UpdateUserActivity
import com.dicoding.nyenyak.ui.updatepassword.UpdatePasswordActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.setInfo.setOnClickListener {
            val intent = Intent(this,UpdateUserActivity::class.java)
            startActivity(intent)
        }
        binding.setPassword.setOnClickListener {
            val intent = Intent(this,UpdatePasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.dicoding.nyenyak.ui.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
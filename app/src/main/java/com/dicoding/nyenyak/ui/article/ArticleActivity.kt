package com.dicoding.nyenyak.ui.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        isLoading(true)
        binding.articleTitle.text = intent.getStringExtra("judul")
        binding.articleContent.text = intent.getStringExtra("isi")
        Glide.with(this)
            .load(intent.getStringExtra("gambar"))
            .into(binding.articleImage)
        isLoading(false)
    }

    fun isLoading (condition : Boolean){
        if (condition == true) binding.progressArticle.visibility = View.VISIBLE else binding.progressArticle.visibility = View.GONE
    }
}
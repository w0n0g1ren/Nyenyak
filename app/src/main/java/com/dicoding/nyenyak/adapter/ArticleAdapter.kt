package com.dicoding.nyenyak.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.nyenyak.data.ArticleResponseItem
import com.dicoding.nyenyak.data.GetDiagnosisResponseItem
import com.dicoding.nyenyak.databinding.TipsItemBinding

class ArticleAdapter (private val context: Context): ListAdapter<ArticleResponseItem, ArticleAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: TipsItemBinding, private val context: Context): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ArticleResponseItem){
        binding.tvTipsItem.text = item.title.toString()
        Glide.with(itemView)
            .load(item.image.toString())
            .into(binding.ivTipsItem)
    }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = TipsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val context = context
        return ViewHolder(binding,context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bind = getItem(position)
        holder.bind(bind)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleResponseItem>() {
            override fun areItemsTheSame(oldItem: ArticleResponseItem, newItem: ArticleResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ArticleResponseItem, newItem: ArticleResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
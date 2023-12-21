package com.dicoding.nyenyak.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem
import com.dicoding.nyenyak.databinding.CekItemBinding
import com.dicoding.nyenyak.ui.detail.DetailActivity

class adapter(private val context: Context): ListAdapter<GetDiagnosisResponseItem, adapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: CekItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: GetDiagnosisResponseItem){
        binding.tvKondisiItem.text = item.sleepDisorder
        binding.tvTanggalItem.text = item.date

        binding.cekItem.setOnClickListener {
            val intent = Intent(context,DetailActivity::class.java)
            intent.putExtra("indeksmassa",item.bMIcategory)
            intent.putExtra("umur",item.age)
            intent.putExtra("tekdarah",item.bloodPressure)
            intent.putExtra("langkah",item.dailySteps)
            intent.putExtra("jantung",item.heartRate)
            intent.putExtra("fisik",item.physicalActivityLevel)
            intent.putExtra("tidur",item.sleepDuration)
            intent.putExtra("stres",item.stressLevel)
            intent.putExtra("sleepdisorder",item.sleepDisorder)
            intent.putExtra("solusi",item.solution)
            intent.putExtra("uid",item.id)
            intent.putExtra("tanggal",item.date)
            context.startActivity(intent)
        }
    }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetDiagnosisResponseItem>() {
            override fun areItemsTheSame(oldItem: GetDiagnosisResponseItem, newItem: GetDiagnosisResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: GetDiagnosisResponseItem, newItem: GetDiagnosisResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CekItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val context = context
        return ViewHolder(binding,context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bind = getItem(position)
        holder.bind(bind)
    }
}
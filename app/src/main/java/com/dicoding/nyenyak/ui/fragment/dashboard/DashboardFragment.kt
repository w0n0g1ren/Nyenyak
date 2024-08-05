package com.dicoding.nyenyak.ui.fragment.dashboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nyenyak.adapter.ArticleAdapter
import com.dicoding.nyenyak.adapter.adapter
import com.dicoding.nyenyak.data.response.ArticleResponseItem
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem
import com.dicoding.nyenyak.databinding.FragmentDashboardBinding
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.main.MainActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!
    private lateinit var intent : Intent

    private val viewModel by viewModels<DashboardFragmentViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        showarticle()
        showlatestdiagnosis()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showlatestdiagnosis() {
        viewModel.getDiagnosis()
        viewModel.getDiagnosisResponseItem.observe(context as MainActivity){
            if (it == null){
                binding.tvNull.visibility = View.VISIBLE
            }else{
                setLatestDiagnose(it)
                binding.tvNull.visibility = View.INVISIBLE
            }

        }
    }

    private fun showarticle() {
        viewModel.getArticle()
        viewModel.getArticleResponseItem.observe(context as MainActivity){
            setArticle(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLatestDiagnose(subList: List<GetDiagnosisResponseItem>) {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val data = subList.sortedByDescending { LocalDate.parse(it.date,dateTimeFormatter) }
        val layoutManager = LinearLayoutManager(context as? MainActivity)
        binding.rvList.setLayoutManager(layoutManager)
        binding.rvList.setHasFixedSize(true)
        val adapter = adapter(context as MainActivity)
        binding.rvList.adapter = adapter
        val limitedList = data.take(4)
        adapter.submitList(limitedList)
    }

    private fun setArticle(subList: List<ArticleResponseItem>) {
        val layoutManager = LinearLayoutManager(context as MainActivity,LinearLayoutManager.HORIZONTAL,false)
        binding?.rvTips?.setLayoutManager(layoutManager)
        binding.rvTips.setHasFixedSize(true)
        val adapter = ArticleAdapter(context as MainActivity)
        binding.rvTips.adapter = adapter
        adapter.submitList(subList)
    }

    companion object{
        private const val TAG = "DashboardFragment"
    }
}
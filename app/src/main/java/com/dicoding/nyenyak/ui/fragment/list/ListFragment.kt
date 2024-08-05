package com.dicoding.nyenyak.ui.fragment.list

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nyenyak.adapter.adapter
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem
import com.dicoding.nyenyak.databinding.FragmentListBinding
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.main.MainActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var intent : Intent

    private val viewModel by viewModels<ListFragmentViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater,container,false)
        val root: View = binding.root
        showdiagnosis()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showdiagnosis() {
        viewModel.getDiagnosis()
        viewModel.getDiagnosisResponseItem.observe(context as MainActivity){
            if (it == null){
                binding.tvNull.visibility = View.VISIBLE
            }else{
                setUserDiagnosis(it)
                binding.tvNull.visibility = View.INVISIBLE
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUserDiagnosis(diagnosisResponse: List<GetDiagnosisResponseItem?>?) {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val data = diagnosisResponse?.sortedByDescending { LocalDate.parse(it?.date,dateTimeFormatter) }
        val layoutmanager = LinearLayoutManager(context as? MainActivity)
        binding.rvList.setLayoutManager(layoutmanager)
        binding.rvList.setHasFixedSize(true)
        val adapter = (context as? MainActivity)?.let { adapter(it) }
        binding.rvList.adapter = adapter
        adapter?.submitList(data)
    }

    companion object{
        private const val TAG = "ListFragment"
    }
}
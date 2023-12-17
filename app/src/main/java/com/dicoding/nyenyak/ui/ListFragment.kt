package com.dicoding.nyenyak.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nyenyak.adapter.adapter
import com.dicoding.nyenyak.data.ApiConfig
import com.dicoding.nyenyak.data.GetDiagnosisResponseItem
import com.dicoding.nyenyak.databinding.FragmentListBinding
import com.dicoding.nyenyak.ui.input.InputActivity
import com.dicoding.nyenyak.ui.main.MainActivity
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater,container,false)
        val root: View = binding.root
        showdiagnosis()
        binding.btnToAdd.setOnClickListener {
            startActivity(Intent(context,InputActivity::class.java))
        }
        return root
    }

    private fun showdiagnosis() {
        val client = ApiConfig.getApiService().getalldiagnosis()
        client.enqueue(object : Callback<List<GetDiagnosisResponseItem>>{
            override fun onResponse(
                call: Call<List<GetDiagnosisResponseItem>>,
                response: Response<List<GetDiagnosisResponseItem>>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        setUserDiagnosis(responseBody.subList(0,responseBody.lastIndex+1))
                    }else{
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<GetDiagnosisResponseItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun setUserDiagnosis(diagnosisResponse: List<GetDiagnosisResponseItem?>?) {
        val layoutmanager = LinearLayoutManager(context as MainActivity)
        binding.rvList.setLayoutManager(layoutmanager)
        binding.rvList.setHasFixedSize(true)
        val adapter = adapter(context as MainActivity)
        binding.rvList.adapter = adapter
        adapter.submitList(diagnosisResponse)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val TAG = "ListFragment"

    }
}
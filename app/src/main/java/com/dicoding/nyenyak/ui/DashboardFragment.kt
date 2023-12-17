package com.dicoding.nyenyak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nyenyak.adapter.ArticleAdapter
import com.dicoding.nyenyak.adapter.adapter
import com.dicoding.nyenyak.data.response.ArticleResponseItem
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.databinding.FragmentDashboardBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

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

    private fun showlatestdiagnosis() {
        val pref = SessionPreference.getInstance(requireContext().datastore)
        val viewmodel =
            (context as? MainActivity)?.let {
                ViewModelProvider(it, FragmentViewModelFactory(pref)).get(
                    DashboardFragmentViewModel::class.java
                )
            }

        (context as? MainActivity)?.let {
            viewmodel?.getToken()?.observe(it){
                if (it.token != null){
                    val client = ApiConfig.getApiService(it.token).getalldiagnosis()
                    client.enqueue(object: Callback<List<GetDiagnosisResponseItem>>{
                        override fun onResponse(
                            call: Call<List<GetDiagnosisResponseItem>>,
                            response: Response<List<GetDiagnosisResponseItem>>
                        ) {
                            if (response != null){
                                val responseBody = response.body()
                                if(responseBody != null){
                                    setLatestDiagnose(responseBody)
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<GetDiagnosisResponseItem>>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }
        }
    }

    private fun setLatestDiagnose(subList: List<GetDiagnosisResponseItem>) {
        val layoutManager = LinearLayoutManager(context as? MainActivity)
        binding.rvList.setLayoutManager(layoutManager)
        binding.rvList.setHasFixedSize(true)
        val adapter = adapter(context as MainActivity)
        binding.rvList.adapter = adapter
        adapter.submitList(subList)
    }

    private fun showarticle() {
        val client = ApiConfig.getApiService().getarticle()
        client.enqueue(object : Callback<List<ArticleResponseItem>>{
            override fun onResponse(
                call: Call<List<ArticleResponseItem>>,
                response: Response<List<ArticleResponseItem>>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        setArticle(responseBody.subList(0,responseBody.lastIndex+1))
                    }
                }
            }

            override fun onFailure(call: Call<List<ArticleResponseItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setArticle(subList: List<ArticleResponseItem>) {
        val layoutManager = LinearLayoutManager(context as MainActivity,LinearLayoutManager.HORIZONTAL,false)
        binding.rvTips.setLayoutManager(layoutManager)
        binding.rvTips.setHasFixedSize(true)
        val adapter = ArticleAdapter(context as MainActivity)
        binding.rvTips.adapter = adapter
        adapter.submitList(subList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
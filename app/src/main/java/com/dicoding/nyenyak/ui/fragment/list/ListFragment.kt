package com.dicoding.nyenyak.ui.fragment.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nyenyak.adapter.adapter
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem
import com.dicoding.nyenyak.databinding.FragmentListBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.fragment.SecondViewModelFactory
import com.dicoding.nyenyak.ui.input.InputActivity
import com.dicoding.nyenyak.ui.login.LoginActivity
import com.dicoding.nyenyak.ui.main.MainActivity
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var intent : Intent
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
        val pref = SessionPreference.getInstance(requireContext().datastore)
        val viewmodel =
            (context as? MainActivity)?.let {
                ViewModelProvider(it, SecondViewModelFactory(pref)).get(
                    ListFragmentViewModel::class.java
                )
            }

        (context as? MainActivity)?.let {
            viewmodel?.getToken()?.observe(it){
                if (it.token != null){
                        val client = ApiConfig.getApiService(it.token).getalldiagnosis()
                        client.enqueue(object : Callback<List<GetDiagnosisResponseItem>>{
                            override fun onResponse(
                                call: Call<List<GetDiagnosisResponseItem>>,
                                response: Response<List<GetDiagnosisResponseItem>>
                            ) {
                                if(response.isSuccessful){
                                    val responseBody = response.body()
                                    if(responseBody != null){
                                        var listData = responseBody.sortedByDescending { it.date }
                                        setUserDiagnosis(listData)
                                    }else{
                                        Log.e(TAG, "onFailure: ${response.message()}")
                                    }
                                }
                                else{
                                    val errorcode : String = response.code().toString()
                                    when(errorcode){
                                        "401" -> intent = Intent(context as MainActivity, LoginActivity::class.java)
                                    }
                                    context?.startActivity(intent)
                                }
                            }

                            override fun onFailure(call: Call<List<GetDiagnosisResponseItem>>, t: Throwable) {
                                Log.e(TAG, "onFailure: ${t.message}")
                            }

                        })
                    }
            }
        }
    }

    private fun setUserDiagnosis(diagnosisResponse: List<GetDiagnosisResponseItem?>?) {
        val layoutmanager = LinearLayoutManager(context as? MainActivity)
        binding.rvList.setLayoutManager(layoutmanager)
        binding.rvList.setHasFixedSize(true)
        val adapter = (context as? MainActivity)?.let { adapter(it) }
        binding.rvList.adapter = adapter
        adapter?.submitList(diagnosisResponse)
    }

    companion object{
        private const val TAG = "ListFragment"
    }
}
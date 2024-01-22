package com.dicoding.nyenyak.ui.fragment.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.response.GetDetailUserResponse
import com.dicoding.nyenyak.databinding.FragmentUserBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.fragment.FragmentViewModelFactory
import com.dicoding.nyenyak.ui.login.LoginActivity
import com.dicoding.nyenyak.ui.main.MainActivity
import com.dicoding.nyenyak.ui.update.UpdateUserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var intent : Intent
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(inflater,container,false)
        val root: View = binding.root

        setUserData()
        binding.editInfoUser.setOnClickListener{
            val intent = Intent(context,UpdateUserActivity::class.java)
            startActivity(intent)
        }

        binding.logoutInfoUser.setOnClickListener {
            val pref = SessionPreference.getInstance(requireContext().datastore)
            val viewModel =
                (context as? MainActivity)?.let {
                    ViewModelProvider(it, FragmentViewModelFactory(pref)).get(
                        UserFragmentViewModel::class.java
                    )
                }
            viewModel?.destroySession()
            val intent = Intent(context as MainActivity,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        return binding.root
    }

    private fun setUserData() {
        val pref = SessionPreference.getInstance(requireContext().datastore)
        val viewModel =
            (context as? MainActivity)?.let {
                ViewModelProvider(it, FragmentViewModelFactory(pref)).get(
                    UserFragmentViewModel::class.java
                )
            }

        (context as? MainActivity)?.let {
            viewModel?.getToken()?.observe(it){
                if (it.token != null){
                    val apiService = ApiConfig.getApiService(it.token).getUser()
                    apiService.enqueue(object : Callback<GetDetailUserResponse>{
                        override fun onResponse(
                            call: Call<GetDetailUserResponse>,
                            response: Response<GetDetailUserResponse>
                        ) {
                            if (response.isSuccessful){
                                val responseBody = response.body()
                                if (responseBody != null){
                                    binding.namaInfoUser.text = responseBody.user?.name
                                    binding.umurInfoUser.text = responseBody.user?.age.toString()
                                    binding.emailInfoUser.text = responseBody.user?.email
                                    binding.genderInfoUser.text = responseBody.user?.gender
                                    binding.lahirInfoUser.text = responseBody.user?.birthDate
                                }else{
                                    Log.e(TAG, "onFailure: ${response.message()}")
                                }
                            }
                            else{
                                val errorcode : String = response.code().toString()
                                when(errorcode){
                                    "401" -> intent = Intent(context as MainActivity,LoginActivity::class.java)
                                }
                                context?.startActivity(intent)
                            }
                        }

                        override fun onFailure(call: Call<GetDetailUserResponse>, t: Throwable) {
                            Log.e(TAG, "onFailure: ${t.message}")
                        }

                    })
                }
            }
        }
    }

    companion object{
        private const val TAG = "UserFragment"

    }
}
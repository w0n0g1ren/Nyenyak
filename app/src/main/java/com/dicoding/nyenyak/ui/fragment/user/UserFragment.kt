package com.dicoding.nyenyak.ui.fragment.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.databinding.FragmentUserBinding
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.main.MainActivity
import com.dicoding.nyenyak.ui.setelan.SettingsActivity
import com.dicoding.nyenyak.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var intent : Intent

    private val viewModel by viewModels<UserFragmentViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(inflater,container,false)
        val root: View = binding.root

        setUserData()
        binding.editInfoUser.setOnClickListener{
            val intent = Intent(context,SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.logoutInfoUser.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logout()
                val intent = Intent(context as MainActivity,WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
        return binding.root
    }

    private fun setUserData() {
        var gender: String = ""
        viewModel.getDetailUser()
        viewModel.getDetailUserResponse.observe(context as MainActivity){
            if (it.status == "success"){
                if (it.user?.gender == "male"){
                    gender = "Laki Laki"
                }else{
                    gender = "Perempuan"
                }

                binding.emailInfoUser.text = it.user?.email.toString().trim()
                binding.namaInfoUser.text = it.user?.name.toString().trim()
                binding.umurInfoUser.text = it.user?.age.toString().trim()
                binding.lahirInfoUser.text = it.user?.birthDate.toString().trim()
                binding.genderInfoUser.text = gender
            }
            else{

            }
        }
    }

    companion object{
        private const val TAG = "UserFragment"
    }
}
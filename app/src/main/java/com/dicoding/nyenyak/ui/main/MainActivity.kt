package com.dicoding.nyenyak.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.api.ApiConfig
import com.dicoding.nyenyak.data.response.GetDetailUserResponse
import com.dicoding.nyenyak.databinding.ActivityMainBinding
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.session.datastore
import com.dicoding.nyenyak.ui.SecondViewModelFactory
import com.dicoding.nyenyak.ui.fragment.user.UserFragment
import com.dicoding.nyenyak.ui.input.InputActivity
import com.dicoding.nyenyak.ui.input.InputViewModel
import com.dicoding.nyenyak.ui.welcome.WelcomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var intent : Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.navView.background = null
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboardFragment, R.id.listFragment,R.id.userFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val pref = SessionPreference.getInstance(application.datastore)
        val viewmodel = ViewModelProvider(this, SecondViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        viewmodel.gettoken().observe(this){
            if (it.token != null){
                val apiService = ApiConfig.getApiService(it.token).getUser()
                apiService.enqueue(object : Callback<GetDetailUserResponse> {
                    override fun onResponse(
                        call: Call<GetDetailUserResponse>,
                        response: Response<GetDetailUserResponse>
                    ) {
                        if (response.isSuccessful){

                        }
                        else{
                            val errorcode: String = response.code().toString()
                                when(errorcode){
                                    "401" -> {intent = Intent(this@MainActivity,WelcomeActivity::class.java)
                                    }
                                }
                                Toast.makeText(this@MainActivity,"sesi anda telah berakhir",Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<GetDetailUserResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message}")
                    }
                })
            }
        }
        binding.btnToAdd.setOnClickListener {
            startActivity(Intent(this,InputActivity::class.java))
        }
    }
    companion object{
        private const val TAG = "MainActivity"
    }
}
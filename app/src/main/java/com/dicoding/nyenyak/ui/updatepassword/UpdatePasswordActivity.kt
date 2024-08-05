package com.dicoding.nyenyak.ui.updatepassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.nyenyak.R
import com.dicoding.nyenyak.data.response.ForgotResponse
import com.dicoding.nyenyak.databinding.ActivityUpdatePasswordBinding
import com.dicoding.nyenyak.ui.ViewModelFactory
import com.dicoding.nyenyak.ui.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePasswordBinding
    private val viewModel by viewModels<UpdatePasswordViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        showLoading(false)

        binding.updatePwdBtn.setOnClickListener {
            var pwdBaru = binding.edPwdBaru.text.toString()
            var pwdConf = binding.edConfBaru.text.toString()

            if(check(pwdBaru, pwdConf) == false){
                showToast(getString(R.string.cek_pwd_gagal))
            }
            else if(pwdBaru.isEmpty() || pwdConf.isEmpty()){
                showToast(getString(R.string.peringatan))
            }
            else{
                doAction(pwdBaru)
            }
        }
    }

    private fun doAction(pwdBaru: String) {
            showLoading(true)
            lifecycleScope.launch {
                try {
                    val response = viewModel.updatePassword(pwdBaru)
                    showToast(response.message.toString())
                    val intent = Intent(this@UpdatePasswordActivity,
                        MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    showLoading(false)
                    startActivity(intent)
                }catch (e : HttpException){
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ForgotResponse::class.java)
                    showToast(errorResponse.message.toString())
                    showLoading(false)
                }
            }
        }


    private fun showToast(message: String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    private fun check(pwd: String, conf: String): Boolean {
        var decide: Boolean

        if (pwd != conf){
            decide = false
        }else{
            decide = true
        }

        return decide
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressUpdatePwd.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressUpdatePwd.isEnabled = !isLoading
    }
}
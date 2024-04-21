package com.dicoding.nyenyak.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.dicoding.nyenyak.data.api.ApiService
import com.dicoding.nyenyak.data.response.ForgotResponse
import com.dicoding.nyenyak.data.response.LoginResponse
import com.dicoding.nyenyak.data.response.RegisterResponse
import com.dicoding.nyenyak.session.DataModel
import com.dicoding.nyenyak.session.SessionPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository private constructor(
    private val apiService: ApiService,
    private val sessionPreference: SessionPreference
) {
    private var _loginResponse = MutableLiveData<LoginResponse>()
    var loginResponse: LiveData<LoginResponse> = _loginResponse.distinctUntilChanged()

    var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    suspend fun register(email: String, password: String, name: String, gender: String, birthdate: String): RegisterResponse {
        return apiService.register(email, password, name, gender, birthdate)
    }

    suspend fun forgot(email: String): ForgotResponse {
        return apiService.forgot(email)
    }
    
    fun getSession(): Flow<DataModel> {
        return sessionPreference.getToken()
    }

    suspend fun saveSession(user: DataModel) {
        sessionPreference.saveSessionSetting(user)
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) { if (response.isSuccessful) {
                val responseBody = response.body()
                if(responseBody != null){
                        _isLoading.value = false
                        _loginResponse.value = response.body()
                }
            }
            else{
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), LoginResponse::class.java)
                _loginResponse.value = errorResponse
            }

            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    suspend fun logout() {
        sessionPreference.sessiondestroy()
    }

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        private const val TAG = "AppRepository"
        fun getInstance(
            apiService: ApiService,
            sessionPreference: SessionPreference
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService, sessionPreference)
            }.also { instance = it }

        fun clearInstance() {
            instance = null
        }
    }
}
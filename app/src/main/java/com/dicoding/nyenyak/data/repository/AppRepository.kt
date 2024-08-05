package com.dicoding.nyenyak.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.dicoding.nyenyak.data.api.ApiService
import com.dicoding.nyenyak.data.response.ArticleResponseItem
import com.dicoding.nyenyak.data.response.DeleteResponse
import com.dicoding.nyenyak.data.response.ForgotResponse
import com.dicoding.nyenyak.data.response.GetDetailUserResponse
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem
import com.dicoding.nyenyak.data.response.InputResponse
import com.dicoding.nyenyak.data.response.LoginResponse
import com.dicoding.nyenyak.data.response.RegisterResponse
import com.dicoding.nyenyak.data.response.UpdateUserResponse
import com.dicoding.nyenyak.session.DataModel
import com.dicoding.nyenyak.session.SessionPreference
import com.dicoding.nyenyak.utils.SingleLiveEvent
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository private constructor(
    private val apiService: ApiService,
    private val sessionPreference: SessionPreference
) {
    private var _loginResponse = MutableLiveData<LoginResponse?>()
    var loginResponse: LiveData<LoginResponse?>? = _loginResponse?.distinctUntilChanged()

    private var _loginResponse1 = SingleLiveEvent<LoginResponse?>()
    var loginResponse1: SingleLiveEvent<LoginResponse?>? = _loginResponse1

    private var _getDiagnosisResponseItem = MutableLiveData<List<GetDiagnosisResponseItem>>()
    var getDiagnosisResponseItem: LiveData<List<GetDiagnosisResponseItem>> = _getDiagnosisResponseItem

    private var _getDetailUserResponse = MutableLiveData<GetDetailUserResponse>()
    var getDetailUserResponse: LiveData<GetDetailUserResponse> = _getDetailUserResponse.distinctUntilChanged()

    private var _getArticleResponseItem = MutableLiveData<List<ArticleResponseItem>>()
    var getArticleResponseItem: LiveData<List<ArticleResponseItem>> = _getArticleResponseItem.distinctUntilChanged()

    private var _getDetailDiagnosisResponseItem = MutableLiveData<GetDiagnosisResponseItem>()
    var getDetailDiagnosisResponseItem: LiveData<GetDiagnosisResponseItem> = _getDetailDiagnosisResponseItem.distinctUntilChanged()

    var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading.distinctUntilChanged()

    suspend fun register(email: String,
                         password: String,
                         name: String,
                         gender: String,
                         birthdate: String): RegisterResponse {
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
                        _loginResponse1.value = response.body()
                }
            }
            else{
                val errorResponse = Gson().fromJson(response.errorBody()?.string(),
                    LoginResponse::class.java)
                _isLoading.value = false
                _loginResponse1.value = errorResponse
            }

            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    suspend fun logout() {
        sessionPreference.sessionDestroy()
    }

    fun getDiagnosis(){
        val client = apiService.getalldiagnosis()
        client.enqueue(object : Callback<List<GetDiagnosisResponseItem>>{
            override fun onResponse(
                call: Call<List<GetDiagnosisResponseItem>>,
                response: Response<List<GetDiagnosisResponseItem>>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _isLoading.value = false
                        _getDiagnosisResponseItem.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<List<GetDiagnosisResponseItem>>,
                                   t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getDetailUser(){
        val client = apiService.getUser()
        client.enqueue(object : Callback<GetDetailUserResponse>{
            override fun onResponse(
                call: Call<GetDetailUserResponse>,
                response: Response<GetDetailUserResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _isLoading.value = false
                        _getDetailUserResponse.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<GetDetailUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getArticle(){
        val client = apiService.getarticle()
        client.enqueue(object : Callback<List<ArticleResponseItem>>{
            override fun onResponse(
                call: Call<List<ArticleResponseItem>>,
                response: Response<List<ArticleResponseItem>>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _getArticleResponseItem.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<List<ArticleResponseItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getDetailDiagnosis(uid: String){
        val client = apiService.getdetaildiagnosis(uid)
        client.enqueue(object : Callback<GetDiagnosisResponseItem>{
            override fun onResponse(
                call: Call<GetDiagnosisResponseItem>,
                response: Response<GetDiagnosisResponseItem>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                    }
                    _getDetailDiagnosisResponseItem.value = response.body()
                }
                else{
                    _getDetailDiagnosisResponseItem.value = response.body()
                }
            }
            override fun onFailure(call: Call<GetDiagnosisResponseItem>, t: Throwable) {
                Log.e(TAG,"gagal")
            }
        })
    }

    suspend fun deleteDiagnosis(uid: String): DeleteResponse{
        return apiService.deletediagnosis(uid)
    }

    suspend fun inputDiagnosis(
        weight: Int,
        height: Int,
        sleepDuration: Float,
        qualityOfSleep: Int,
        physicalActivityLevel: Int,
        bloodPressure: String,
        stressLevel: Int,
       heartRate: Int,
        dailySteps: Int
    ): InputResponse {
        return apiService.inputDiagnosis(
            weight, height,
            sleepDuration, qualityOfSleep,
            physicalActivityLevel, bloodPressure,
            stressLevel, heartRate, dailySteps)
    }

    suspend fun updatePassword(password: String): ForgotResponse{
        return apiService.updatePassword(password)
    }

    suspend fun updateUser(name : String, birthDate: String, gender: String): UpdateUserResponse{
        return apiService.updateUser(name, birthDate, gender)
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
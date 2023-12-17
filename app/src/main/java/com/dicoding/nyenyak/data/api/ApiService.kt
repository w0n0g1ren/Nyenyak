package com.dicoding.nyenyak.data.api

import com.dicoding.nyenyak.data.response.ArticleResponseItem
import com.dicoding.nyenyak.data.response.DeleteResponse
import com.dicoding.nyenyak.data.response.GetDiagnosisResponseItem
import com.dicoding.nyenyak.data.response.InputResponse
import com.dicoding.nyenyak.data.response.LoginResponse
import com.dicoding.nyenyak.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("gender") gender: String,
        @Field("birthDate") birthDate: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("diagnosis")
    suspend fun inputDiagnosis(
        @Field("weight") weight: Int,
        @Field("height") height: Int,
        @Field("sleepDuration") sleepDuration: Float,
        @Field("qualityOfSleep") qualityOfSleep: Int,
        @Field("physicalActivityLevel") physicalActivityLevel: Int,
        @Field("bloodPressure") bloodPressure: String,
        @Field("stressLevel") stressLevel: Int,
        @Field("heartRate") heartRate: Int,
        @Field("dailySteps") dailySteps: Int
    ): InputResponse

    @GET("diagnosis")
    fun getalldiagnosis() : Call<List<GetDiagnosisResponseItem>>

    @GET("diagnosis/{id]")
    suspend fun getdetaildiagnosis(@Path("id") id: String) : GetDiagnosisResponseItem

    @DELETE("diagnosis/{id}")
    suspend fun deletediagnosis(@Path("id") id: String): DeleteResponse

    @GET("articles")
    fun getarticle() : Call<List<ArticleResponseItem>>
}
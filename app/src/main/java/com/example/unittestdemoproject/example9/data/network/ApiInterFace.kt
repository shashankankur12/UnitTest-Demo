package com.example.unittestdemoproject.example9.data.network

import com.example.unittestdemoproject.example9.data.responses.LoginRequest
import com.example.unittestdemoproject.example9.data.responses.LoginUserData
import com.example.unittestdemoproject.example9.utils.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterFace {

    @POST("login")
    suspend fun doLogin(@Body request: LoginRequest): Response<LoginUserData>

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): ApiInterFace {
            val okHttpClient =
                OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor).build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterFace::class.java)
        }
    }
}
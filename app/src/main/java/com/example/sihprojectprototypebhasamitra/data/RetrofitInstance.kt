package com.example.sihprojectprototypebhasamitra.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    val api: AksharamukhaApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://aksharamukha.appspot.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AksharamukhaApiService::class.java)
    }
}

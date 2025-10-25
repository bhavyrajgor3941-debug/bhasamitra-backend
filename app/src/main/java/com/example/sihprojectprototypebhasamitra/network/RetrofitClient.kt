package com.example.sihprojectprototypebhasamitra.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * A singleton object for handling all network operations.
 * This provides a single, properly configured Retrofit instance for the entire app.
 */
object RetrofitClient {

    // The base URL for your backend server.
    private const val BASE_URL = "https://bhasamitra-backend.onrender.com/"

    // The lazy-initialized OkHttpClient, configured with a logger and timeouts.
    private val client: OkHttpClient by lazy {
        // The logging interceptor helps in debugging by showing request and response details.
        val logging = HttpLoggingInterceptor().apply {
            // BODY level provides the most detail.
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS) // Increased timeout for server spin-up
            .addInterceptor(logging) // Add the logger as an interceptor
            .build()
    }

    // The lazy-initialized Retrofit instance that uses the OkHttpClient.
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

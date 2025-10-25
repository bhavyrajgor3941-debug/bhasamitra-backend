package com.example.sihprojectprototypebhasamitra.data

import com.example.sihprojectprototypebhasamitra.network.RetrofitClient
import com.example.sihprojectprototypebhasamitra.network.TranslitResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AksharamukhaApiClient {

    // You can keep this client if you use it elsewhere, or remove it if Retrofit
    // is handling all your networking now. I'll leave it commented out for now.
    /*
    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    */

    /**
     * Transliterates text using the Retrofit ApiService.
     * This function now calls the GET endpoint with query parameters.
     */
    suspend fun transliterate(
        source: String,
        target: String,
        text: String
    ): TranslitResponse = withContext(Dispatchers.IO) {
        try {
            // =================================================================
            // THIS IS WHERE YOU APPLY THE CHANGE
            // =================================================================

            // This is the new, correct GET way using Retrofit
            val response = RetrofitClient.apiService.transliterateText(
                source = source,
                target = target,
                text = text
            )

            if (response.isSuccessful) {
                // If the call was successful, return the response body.
                // The elvis operator handles the case where the body might be null.
                response.body() ?: TranslitResponse(null, null, "Response body was empty.")
            } else {
                // If the server returned an error (e.g., 404, 500), create a response with the error message.
                TranslitResponse(null, null, "Error: ${response.code()} ${response.message()}")
            }

        } catch (e: Exception) {
            // If there was a network error (e.g., no internet), create a response with the exception message.
            e.printStackTrace()
            TranslitResponse(null, null, "Network error: ${e.message}")
        }
    }
}

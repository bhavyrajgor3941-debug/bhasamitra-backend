package com.example.sihprojectprototypebhasamitra.data

import com.example.sihprojectprototypebhasamitra.network.RetrofitClient
import com.example.sihprojectprototypebhasamitra.network.TranslitResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// In your AksharamukhaApiClient.kt

object AksharamukhaApiClient {
    suspend fun transliterate(        source: String,
                                      target: String,
                                      text: String
    ): TranslitResponse = withContext(Dispatchers.IO) {
        try {
            // FIX: Create the request body object
            val requestBody = TransliterationRequest(source = source, target = target, text = text)

            // FIX: Call the POST method with the request body
            val response = RetrofitClient.apiService.transliterateText(requestBody)

            if (response.isSuccessful) {
                response.body() ?: TranslitResponse(null, null, "Response body was empty.")
            } else {
                TranslitResponse(null, null, "Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            TranslitResponse(null, null, "Network error: ${e.message}")
        }
    }
}


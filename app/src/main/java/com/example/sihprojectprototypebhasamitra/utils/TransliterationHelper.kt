package com.example.sihprojectprototypebhasamitra.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject

object TransliterationHelper {

    // 👇 STEP 1: ADD YOUR LOCAL IP HERE
    // Example: "192.168.1.7" — run `ipconfig` to find it
    private const val SERVER_IP = "https://bhasamitra-backend.onrender.com"

    // 👇 STEP 2: Complete endpoint (must match your FastAPI server)
    private const val ENDPOINT = "https://bhasamitra-backend.onrender.com"

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    suspend fun transliterateText(source: String, target: String, text: String): String =
        withContext(Dispatchers.IO) {
            try {
                // Create the JSON body
                val json = JSONObject().apply {
                    put("source", source)
                    put("target", target)
                    put("text", text)
                }.toString()

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val body = json.toRequestBody(mediaType)

                // 👇 Use your local FastAPI endpoint instead of public API
                val request = Request.Builder()
                    .url("https://bhasamitra-backend.onrender.com/")
                    .post(body)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw Exception("HTTP ${response.code}: ${response.message}")
                    }
                    // Parse backend JSON response
                    val responseBody = response.body?.string() ?: ""
                    val jsonResponse = JSONObject(responseBody)
                    jsonResponse.optString("transliteration", "Error: Missing field")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "Error: ${e.message}"
            }
        }
}

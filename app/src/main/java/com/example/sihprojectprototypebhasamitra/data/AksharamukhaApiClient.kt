package com.example.sihprojectprototypebhasamitra.data

// The problematic import has been removed from here.

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject

object AksharamukhaApiClient {
    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    /**
     * Transliterate text using Aksharamukha public API:
     * POST https://aksharamukha.appspot.com/api/public
     * Body: { "source":"Devanagari", "target":"Latin", "text":"..." }
     *
     * Returns the API response body string.
     */
    suspend fun transliterate(
        source: String,
        target: String,
        text: String
    ): String = withContext(Dispatchers.IO) {
        // Build JSON request body
        val json = JSONObject().apply {
            put("source", source)
            put("target", target)
            put("text", text)
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://aksharamukha.appspot.com/api/public")
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                // include code in the error for easier debugging
                throw IllegalStateException("HTTP ${response.code}: ${response.message}")
            }
            response.body?.string() ?: ""
        }
    }
}

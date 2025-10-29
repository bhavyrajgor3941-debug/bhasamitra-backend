// In your ApiService.kt

package com.example.sihprojectprototypebhasamitra.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// This data class is used to structure the JSON body for the POST request
data class TranslitRequest(
    val source: String,
    val target: String,
    val text: String
)

data class TranslitResponse(
    val detected_text: String?,
    val transliterated_text: String?,
    val error: String? = null
)

interface ApiService {
    @POST("transliterate")
    suspend fun transliterateText(
        @Body request: TranslitRequest
    ): Response<TranslitResponse>
}

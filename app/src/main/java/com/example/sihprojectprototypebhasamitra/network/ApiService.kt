// In your ApiService.kt

package com.example.sihprojectprototypebhasamitra.network

import com.example.sihprojectprototypebhasamitra.data.TransliterationRequest
import retrofit2.Response
// FIX: Add back the POST and Body imports
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
    // FIX: Change @GET back to @POST
    @POST("transliterate")
    suspend fun transliterateText(
        // FIX: Change @Query parameters back to a single @Body object
        @Body request: TransliterationRequest
    ): Response<TranslitResponse>
}

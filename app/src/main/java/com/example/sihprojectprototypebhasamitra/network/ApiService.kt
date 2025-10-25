package com.example.sihprojectprototypebhasamitra.network

// You don't need these okhttp3 imports here anymore
// import okhttp3.MultipartBody
// import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query // <-- FIX 1: Import @Query

data class TranslitResponse(
    val detected_text: String?,
    val transliterated_text: String?,
    val error: String? = null
)

interface ApiService {
    @GET("transliterate") // Endpoint path can be relative
    suspend fun transliterateText(
        // FIX 2: Use @Query for each parameter instead of @Body
        @Query("source") source: String,
        @Query("target") target: String,
        @Query("text") text: String
    ): Response<TranslitResponse>
}

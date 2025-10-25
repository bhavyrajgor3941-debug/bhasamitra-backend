package com.example.sihprojectprototypebhasamitra.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body

data class TranslitResponse(
    val detected_text: String?,
    val transliterated_text: String?,
    val error: String? = null
)

interface ApiService {
    @GET("/transliterate")
    suspend fun transliterateText(
        @Body requestBody: RequestBody
    ): Response<TranslitResponse>

}






package com.example.sihprojectprototypebhasamitra.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body

data class TranslitResponse(
    val detected_text: String?,
    val transliterated_text: String?,
    val error: String? = null
)

interface ApiService {
    @POST("/transliterate")
    suspend fun transliterateText(
        @Body requestBody: RequestBody
    ): Response<TranslitResponse>

}






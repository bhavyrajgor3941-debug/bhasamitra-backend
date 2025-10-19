package com.example.sihprojectprototypebhasamitra.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class TranslitResponse(
    val detected_text: String?,
    val transliterated_text: String?,
    val error: String? = null
)

interface ApiService {
    @Multipart
    @POST("/transliterate")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<TranslitResponse>
}






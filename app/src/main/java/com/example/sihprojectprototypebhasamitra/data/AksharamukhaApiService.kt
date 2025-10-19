package com.example.sihprojectprototypebhasamitra.data

import retrofit2.http.Body
import retrofit2.http.POST

interface AksharamukhaApiService {
    @POST("api/public")
    suspend fun transliterate(@Body request: TransliterationRequest): TransliterationResponse
}

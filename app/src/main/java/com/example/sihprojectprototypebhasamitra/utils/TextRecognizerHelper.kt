package com.example.sihprojectprototypebhasamitra.utils

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import kotlinx.coroutines.tasks.await

object TextRecognizerHelper {

    private val recognizer = TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build())

    suspend fun detectTextFromBitmap(bitmap: Bitmap): String {
        val image = InputImage.fromBitmap(bitmap, 0)
        val result = recognizer.process(image).await()
        return result.text
    }
}



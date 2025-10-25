package com.example.sihprojectprototypebhasamitra.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sihprojectprototypebhasamitra.data.SettingsManager
import com.example.sihprojectprototypebhasamitra.utils.TextRecognizerHelper
import com.example.sihprojectprototypebhasamitra.utils.TransliterationHelper
import kotlinx.coroutines.launch

@Composable
fun CameraScreen(
    settingsManager: SettingsManager, // Added to get language settings
    onDetect: (String, String) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Collect language preferences from SettingsManager
    val fromLang by settingsManager.fromLangFlow.collectAsState(initial = "Hindi")
    val toLang by settingsManager.toLangFlow.collectAsState(initial = "English")

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                try {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    capturedBitmap = bitmap
                    Log.d("CameraScreen", "Bitmap created: ${bitmap.width}x${bitmap.height}")
                } catch (e: Exception) {
                    Log.e("CameraScreen", "Error decoding bitmap", e)
                }
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (capturedBitmap != null) {
            Image(bitmap = capturedBitmap!!.asImageBitmap(), contentDescription = "Selected Image")
        } else {
            Text("No image selected")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text("Select Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    capturedBitmap?.let { bmp ->
                        coroutineScope.launch {
                            isLoading = true
                            val detected = try {
                                TextRecognizerHelper.detectTextFromBitmap(bmp)
                            } catch (e: Exception) {
                                Log.e("CameraScreen", "Error detecting text", e)
                                "Error: " + (e.message ?: "")
                            }

                            // Convert language names (e.g., "Hindi") to script names (e.g., "Devanagari")
                            val sourceScript = langToScript(fromLang)
                            val targetScript = langToScript(toLang)

                            val translit = TransliterationHelper.transliterateText(
                                source = sourceScript,
                                target = targetScript,
                                text = detected
                            )
                            isLoading = false
                            onDetect(detected, translit)
                        }
                    }
                },
                enabled = capturedBitmap != null
            ) {
                Text("Detect and Transliterate")
            }
        }
    }
}

private fun langToScript(lang: String): String {
    return when (lang) {
        "Hindi", "Marathi" -> "Devanagari"
        "Gujarati" -> "Gujarati"
        "Bengali" -> "Bengali"
        "Tamil" -> "Tamil"
        "Telugu" -> "Telugu"
        else -> "Latin" // Default for "English"
    }
}

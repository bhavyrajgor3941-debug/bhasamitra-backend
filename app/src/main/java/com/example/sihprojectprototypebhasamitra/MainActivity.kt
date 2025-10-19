package com.example.sihprojectprototypebhasamitra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sihprojectprototypebhasamitra.ui.screens.CameraScreen
import com.example.sihprojectprototypebhasamitra.ui.screens.HomeScreen
import com.example.sihprojectprototypebhasamitra.ui.screens.ResultScreen
import com.example.sihprojectprototypebhasamitra.ui.screens.SettingsScreen
import com.example.sihprojectprototypebhasamitra.ui.theme.SIHProjectPrototypeBhasaMitraTheme
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.sihprojectprototypebhasamitra.data.SettingsManager
import androidx.compose.ui.platform.LocalContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SIHProjectPrototypeBhasaMitraTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    if (currentRoute != Routes.Home) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                title = { Text(text = "BhashaMitra – Transliteration Assistant") },
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            appNavGraph(navController, settingsManager)
        }
    }
}

object Routes {
    const val Home = "home"
    const val Camera = "camera"
    // Keep this route definition as it is for the NavHost
    const val Result = "result/{originalText}/{transliteratedText}"
    const val Settings = "settings"

    // The function that builds the route for navigation
    fun result(originalText: String, transliteratedText: String): String {
        // FIX: Use the string literal "UTF-8" instead of calling .toString()
        val encodedOriginal = URLEncoder.encode(originalText, "UTF-8")
        val encodedTransliterated = URLEncoder.encode(transliteratedText, "UTF-8")
        return "result/$encodedOriginal/$encodedTransliterated"
    }
}


private fun NavGraphBuilder.appNavGraph(navController: NavController, settingsManager: SettingsManager) {

    composable(Routes.Home) {
        HomeScreen(
            onScanClick = { navController.navigate(Routes.Camera) }
        )
    }
    composable(Routes.Camera) {
        CameraScreen(
            settingsManager = settingsManager,
            onDetect = { original, transliterated ->
                navController.navigate(Routes.result(original, transliterated))
            },
            onClose = { navController.navigateUp() }
        )
    }

    composable(
        route = Routes.Result,
        arguments = listOf(
            navArgument("originalText") { type = NavType.StringType },
            navArgument("transliteratedText") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val originalText = URLDecoder.decode(backStackEntry.arguments?.getString("originalText") ?: "", StandardCharsets.UTF_8.toString())
        val transliteratedText = URLDecoder.decode(backStackEntry.arguments?.getString("transliteratedText") ?: "", StandardCharsets.UTF_8.toString())
        ResultScreen(
            originalText = originalText,
            transliteratedText = transliteratedText,
            onBack = { navController.navigateUp() }
        )
    }
    composable(Routes.Settings) {
        SettingsScreen()
    }
}

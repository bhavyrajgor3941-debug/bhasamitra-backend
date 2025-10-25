plugins {
    id("com.android.application")
    kotlin("android")
    // Add the Compose Compiler plugin
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    signingConfigs {
        create("Python intepreter") {
            storeFile =
                file("C:\\Users\\BHAVY\\AppData\\Local\\Programs\\Python\\Python313\\python.exe")
        }
    }
    namespace = "com.example.SIHProjectPrototypeBhashaMitra"
    compileSdk = 36 // Changed to a stable version, 36 is not available
    // buildToolsVersion is also not needed anymore

    defaultConfig {
        applicationId = "com.example.SIHProjectPrototypeBhashaMitra"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // You should add this
    }

    // --- MOVED HERE ---
    // compileOptions and kotlinOptions belong directly under the 'android' block
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    // --- END OF MOVED BLOCKS ---

    buildTypes {
        release {
            // Add release configurations here, e.g., isMinifyEnabled = true
        }
    }

    buildFeatures {
        compose = true
    }
}

// In your app/build.gradle.kts

dependencies {
    // CORE AND ACTIVITY
    implementation(libs.androidx.core.ktx)
    implementation("androidx.activity:activity-compose:1.9.0")

    // COMPOSE - Using the BOM to manage versions
    // The BOM ensures all the following Compose libraries are compatible.
    // You only need to declare the BOM once.
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3") // Only one Material 3 dependency is needed.
    implementation("androidx.compose.material:material-icons-extended") // Use this for all icons

    // NAVIGATION
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // NETWORKING - Using the OkHttp BOM
    // The BOM manages versions for okhttp and logging-interceptor.
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ML KIT & COROUTINES
    implementation("com.google.mlkit:text-recognition-devanagari:16.0.0-beta6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1") // Keep only the latest version

    // CameraX dependencies
    implementation("androidx.camera:camera-core:1.3.4")
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")

    implementation("androidx.camera:camera-core:1.3.4")
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")
    implementation("androidx.datastore:datastore-preferences:1.0.0")



    // DEBUG
    debugImplementation("androidx.compose.ui:ui-tooling")
}


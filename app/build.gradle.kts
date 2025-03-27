plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.precisepal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.precisepal"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeCompiler {
        enableStrongSkippingMode = true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.volley)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Downloadable Fonts
    implementation(libs.androidx.ui.text.google.fonts)
    // Lifecycle utilities for Compose
    implementation(libs.androidx.lifecycle.runtime.compose)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //WindowSizeClass - for adaptive UI
    implementation(libs.androidx.material3.windowSizeClass)
    //Coil - best version
    implementation("io.coil-kt:coil-compose:2.6.0")
    //navigation
    val nav_version = "2.8.9"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    //kotlin serialization for navigation
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    //Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.0")
    //dagger hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    //firebase auth
    implementation("com.google.firebase:firebase-auth")
    //firebase fireStore
    implementation("com.google.firebase:firebase-firestore")
    //credential manager
    implementation(libs.androidx.credentials)
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0-rc01")
    implementation (libs.googleid)
}
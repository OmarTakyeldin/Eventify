plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {

    // Core Android libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation ("com.google.firebase:firebase-firestore-ktx:24.4.4") // Use the latest version


    // Jetpack Compose libraries
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui:1.5.3") // UI Core
    implementation("androidx.compose.ui:ui-text:1.5.3") // Text utilities (TextAlign)
    implementation("androidx.compose.foundation:foundation:1.5.3") // Foundation (Layout)
    implementation("androidx.compose.material:material:1.5.3") // Material Design 2
    implementation("androidx.compose.material3:material3:1.1.1") // Material Design 3
    implementation("androidx.navigation:navigation-compose:2.7.4")


    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation(libs.firebase.firestore.ktx)

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("com.google.firebase:firebase-auth-ktx:21.0.5") // For Firebase Authentication
    implementation ("com.google.firebase:firebase-database-ktx:20.0.5") // For Firebase Realtime Database
    implementation ("com.google.firebase:firebase-firestore-ktx:24.0.0") // If using Firestore too (optional)
}

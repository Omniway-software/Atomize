plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Hilt
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")

    // GMS
    id("com.google.gms.google-services")
}

android {
    namespace = "com.infinitysoftware.atomize"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.infinitysoftware.atomize"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.0.9"

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
        buildConfig = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Custom Implementations Section.
    // Firebase Related Section.
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    val credentialsManagerVersion = "1.5.0-alpha05"
    implementation("androidx.credentials:credentials:$credentialsManagerVersion")
    implementation("androidx.credentials:credentials-play-services-auth:$credentialsManagerVersion")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Firebase BOM.
    implementation(platform("com.google.firebase:firebase-bom:34.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")

    // Compose BOM.
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Firebase Analytics
    implementation("com.google.firebase:firebase-analytics")
    // End Of Firebase Related Section.

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Lifecycle (ViewModel, LiveData)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")

    // Navigation Component for Compose
    implementation("androidx.navigation:navigation-compose:2.9.5")

    // Material Components
    implementation("com.google.android.material:material:1.13.0")

    // Hilt (Dependency Injection)
    implementation("com.google.dagger:hilt-android:2.57.2")
    kapt("com.google.dagger:hilt-compiler:2.57.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    // Retrofit + JSON
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.google.code.gson:gson:2.13.2")

    // Room Database
    implementation("androidx.room:room-runtime:2.8.1")
    implementation("androidx.room:room-ktx:2.8.1")
    kapt("androidx.room:room-compiler:2.8.1")

    // Lottie.
    implementation("com.airbnb.android:lottie-compose:6.1.0")
}
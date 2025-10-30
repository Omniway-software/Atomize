// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Custom Plugins Section.
    // Dagger & Hilt Section.
    id("com.google.dagger.hilt.android") version "2.48" apply false

    // GMS Section.
    id("com.google.gms.google-services") version "4.4.4" apply false

    // Add the dependency for the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics") version "3.0.6" apply false
}
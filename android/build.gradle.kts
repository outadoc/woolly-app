plugins {
    id("org.jetbrains.compose") version "0.3.1"
    id("com.android.application")
    kotlin("android")
}

group = "fr.outadoc.mastodonk"
version = "0.1-alpha01"

repositories {
    google()
}

dependencies {
    implementation(projects.common)
    implementation(libs.androidx.activity.compose)
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "fr.outadoc.mastodonk.android"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

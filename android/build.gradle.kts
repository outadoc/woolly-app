plugins {
    id("org.jetbrains.compose") version "0.3.2"
    id("com.android.application")
    kotlin("android")
}

repositories {
    google()
}

dependencies {
    implementation(projects.common)
    implementation(libs.androidx.activity.compose)
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "fr.outadoc.woolly.android"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    packagingOptions {
        pickFirst("META-INF/mastodonk-core.kotlin_module")
        pickFirst("META-INF/mastodonk-paging.kotlin_module")
    }
}

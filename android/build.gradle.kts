plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

repositories {
    google()
}

dependencies {
    implementation(projects.common)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.datastore.android)
    implementation(libs.decompose.core)
    implementation(libs.decompose.android)
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

    composeOptions {
        kotlinCompilerVersion = "1.5.10"
        kotlinCompilerExtensionVersion = "1.0.0-rc01"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    packagingOptions {
        exclude("META-INF/**")
    }
}

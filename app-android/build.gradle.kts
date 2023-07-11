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
    implementation(projects.ui)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.datastore.android)
    implementation(libs.androidx.splashscreen)
    implementation(libs.decompose.core)
    implementation(libs.decompose.android)
    implementation(libs.kotlinx.coroutines.android)
}

android {
    compileSdk = 31
    namespace = "fr.outadoc.woolly.android"

    defaultConfig {
        applicationId = "fr.outadoc.woolly.android"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = project.version as String
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/**")
        }
    }
}

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
    implementation(libs.material.components)
}

android {
    compileSdk = 33
    namespace = "fr.outadoc.woolly.android"

    defaultConfig {
        applicationId = "fr.outadoc.woolly.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = project.version as String
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
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
        resources {
            excludes.add("META-INF/**")
        }
    }
}

import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("kotlin-parcelize")
}

kotlin {
    android()

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.runtime)
                implementation(compose.material)
                implementation(compose.ui)

                implementation(libs.androidx.annotation)
                implementation(libs.androidx.paging.core)
                implementation(libs.kotlinx.coroutines)
            }
        }

        val jvmMain by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies {
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.core)
            }
        }

        val desktopMain by getting {
            dependsOn(jvmMain)
        }
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    compileSdk = 31
    namespace = "fr.outadoc.woolly.ports"

    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-rc01"
    }
}

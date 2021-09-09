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
                implementation(projects.common)

                api(compose.foundation)
                api(compose.runtime)
                api(compose.material)
                api(compose.ui)
                api(compose.materialIconsExtended)
                api("androidx.compose.ui:ui-text")

                api(libs.kodein)
                api(libs.ktor.serialization)
                api(libs.kotlinx.coroutines)

                implementation(libs.androidx.paging)
                implementation(libs.mastodonk.core)
                implementation(libs.mastodonk.paging)
                implementation(libs.kamel)
                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.core)
                implementation(libs.androidx.datastore.core)
                implementation(libs.decompose.core)
                implementation(libs.decompose.jb)
            }
        }

        val jvmMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.engine.cio)
                implementation(libs.jsoup)
                implementation(libs.appdirs)
            }
        }

        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies {
                api(libs.androidx.appcompat)
                api(libs.androidx.core)
                api(libs.androidx.preference)
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
    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-rc01"
    }

    dependencies {
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    }
}

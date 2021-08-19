import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    kotlin("plugin.serialization")
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
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
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
        val jvmTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation(libs.junit)
                implementation(kotlin("test-junit"))
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
        val androidTest by getting {
            dependsOn(jvmTest)
        }

        val desktopMain by getting {
            dependsOn(jvmMain)
        }
        val desktopTest by getting {
            dependsOn(jvmTest)
        }
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-rc01"
    }
}

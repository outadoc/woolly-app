import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.3.2"
    id("com.android.library")
}

group = "fr.outadoc.mastodonk"
version = "0.1-alpha01"

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
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)

                implementation(libs.mastodonk.core)
                implementation(libs.mastodonk.paging)
                implementation(libs.androidx.paging.compose)
            }
        }
        val commonTest by getting

        val jvmMain by creating {
            dependsOn(commonMain)
        }
        val jvmTest by creating {
            dependsOn(commonTest)
            dependencies {
                implementation(libs.junit)
            }
        }

        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies {
                api(libs.androidx.appcompat)
                api(libs.androidx.core)
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
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
    }
}

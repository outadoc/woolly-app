import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.3.1"
    id("com.android.library")
}

group = "fr.outadoc.mastodonk"
version = "0.1-alpha01"

repositories {
    google()
}

kotlin {
    android()

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val jvmMain by creating {
            dependencies {
                implementation("fr.outadoc.mastodonk:mastodonk-paging:0.1-alpha10")
            }
        }
        val jvmTest by creating

        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)

                implementation("fr.outadoc.mastodonk:mastodonk-core:0.1-alpha10")
            }
        }
        val commonTest by getting

        val androidMain by getting {
            dependsOn(jvmMain)
            dependencies {
                api("androidx.appcompat:appcompat:1.2.0")
                api("androidx.core:core-ktx:1.3.1")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13")
            }
        }

        val desktopMain by getting {
            dependsOn(jvmMain)
        }
        val desktopTest by getting
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

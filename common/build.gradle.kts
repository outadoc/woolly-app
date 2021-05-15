import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.3.2"
    id("com.android.library")
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
                implementation(projects.libHtmltext)

                api(compose.foundation)
                api(compose.runtime)
                api(compose.material)
                api(compose.ui)
                api(compose.materialIconsExtended)

                implementation(libs.androidx.paging)
                implementation(libs.mastodonk.core.common)
                implementation(libs.mastodonk.paging.common)
                implementation(libs.kamel)
                implementation(libs.kodein)
                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.core)
                implementation(libs.ktor.serialization)
            }
        }
        val commonTest by getting

        val jvmMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.mastodonk.core.jvm)
                implementation(libs.mastodonk.paging.jvm)
                implementation(libs.ktor.engine.cio)
            }
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
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
    }
}

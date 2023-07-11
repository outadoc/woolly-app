plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
                api(libs.kodein)
                api(libs.ktor.contentNegociation)
                api(libs.ktor.logging)
                api(libs.ktor.serialization)
                api(libs.kotlinx.coroutines.core)

                implementation(libs.androidx.paging.core)
                implementation(libs.mastodonk.core)
                implementation(libs.mastodonk.paging)
                implementation(libs.kamel)
                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.core)
                implementation(libs.androidx.datastore.core)
                implementation(libs.decompose.core)
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

        val androidUnitTest by getting {
            dependsOn(jvmTest)
        }

        val desktopMain by getting {
            dependsOn(jvmMain)
            dependencies {
                implementation(libs.systemthemedetector)
            }
        }

        val desktopTest by getting {
            dependsOn(jvmTest)
        }
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    compileSdk = 31
    namespace = "fr.outadoc.woolly.common"

    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-rc01"
    }
}

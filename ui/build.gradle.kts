plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("kotlin-parcelize")
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm("desktop") {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.common)
                implementation(projects.composePorts)

                api(compose.foundation)
                api(compose.runtime)
                api(compose.material)
                api(compose.ui)
                api(compose.materialIconsExtended)

                api(libs.kodein)
                api(libs.ktor.serialization)
                api(libs.kotlinx.coroutines.core)
                api(libs.mokoResources)

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

    compileSdk = 33
    namespace = "fr.outadoc.woolly.ui"

    defaultConfig {
        minSdk = 24
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    dependencies {
        coreLibraryDesugaring(libs.desugar)
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "fr.outadoc.woolly.ui"
}

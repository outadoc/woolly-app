import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.3.2"
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.foundation)
                api(compose.runtime)
            }
        }
        val commonTest by getting

        val jvmMain by getting {
            dependencies {
                implementation("org.jsoup:jsoup:1.13.1")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.junit)
            }
        }
    }
}
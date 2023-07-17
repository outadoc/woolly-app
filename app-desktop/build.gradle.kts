import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.common)
                implementation(projects.ui)
            }
        }

        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.androidx.datastore.core)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.decompose.core)
                implementation(libs.decompose.jb)
            }
        }

        named("jvmTest")
    }
}

compose.desktop {
    application {
        mainClass = "fr.outadoc.woolly.desktop.Woolly"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb,
                TargetFormat.Rpm
            )

            packageName = "Woolly"
            description = "Desktop client for Mastodon"
            packageVersion = project.version as String
            version = project.version as String
            copyright = "© 2021 Baptiste Candellier"
            vendor = "Baptiste Candellier"

            jvmArgs(
                // Auto-switch dark theme on macOS
                "-Dapple.awt.application.appearance=system"
            )

            macOS {
                bundleID = "fr.outadoc.woolly.desktop"
            }

            windows {
                upgradeUuid = "22afac0d-e867-47ed-a874-875e56c58b9e"
                menuGroup = "Woolly"
            }

            linux {
                debMaintainer = "baptiste@candellier.me"
            }

            modules(
                // Include sun/misc/Unsafe in the generated package
                "jdk.unsupported"
            )
        }
    }
}

// Exclude kotlinx-coroutines-android from the build
configurations {
    all {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")
    }
}

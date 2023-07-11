pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}

rootProject.name = "woolly-app"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("app-android")
include("app-desktop")

include("common")
include("ui")

include("compose-ports")

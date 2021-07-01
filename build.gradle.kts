import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    kotlin("android") version "1.5.10" apply false
    kotlin("multiplatform") version "1.5.10" apply false
    kotlin("plugin.serialization") version "1.5.10" apply false
    id("org.jetbrains.compose") version "0.4.0" apply false

    id("com.diffplug.spotless") version "5.14.0"
    id("com.github.ben-manes.versions") version "0.39.0"
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.2.2")
    }
}

allprojects {
    group = "fr.outadoc.woolly"
    version = "0.1-alpha01"

    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven { url = uri("https://nexus.outadoc.fr/repository/public") }
    }
}

configure<SpotlessExtension> {
    format("misc") {
        target(
            "**/*.kt",
            "**/*.kts",
            "**/*.md",
            "**/.gitignore"
        )

        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}

import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    id("com.diffplug.spotless") version "5.12.4"
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
        classpath("com.android.tools.build:gradle:4.0.1")
    }
}

allprojects {
    group = "fr.outadoc.mastodonk"
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

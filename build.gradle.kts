buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")

    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.13.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
        classpath("com.google.gms:google-services:4.4.4")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.6")
    }
}

plugins {
    alias(libs.plugins.arturbosch.detekt)
}

detekt {
    source = files(".")
    buildUponDefaultConfig = false
    config = files("$projectDir/default-detekt-config.yml")
}


tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}


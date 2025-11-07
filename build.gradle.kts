buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }
}

plugins {
    alias(libs.plugins.arturbosch.detekt)
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
    source.setFrom(files("."))
    config.setFrom(files("default-detekt-config.yml"))
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }
}

plugins {
    alias(libs.plugins.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.builtin.kotlin) apply false
    alias(libs.plugins.android.legacy.kapt) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.arturbosch.detekt) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

if (gradle.startParameter.taskNames.any { it.contains("detekt", ignoreCase = true) }) {
    apply(plugin = libs.plugins.arturbosch.detekt.get().pluginId)

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension>("detekt") {
        autoCorrect = true
        buildUponDefaultConfig = true
        source.setFrom(files("."))
        config.setFrom(files("default-detekt-config.yml"))
    }

    dependencies {
        add("detektPlugins", libs.detekt.formatting)
    }
}

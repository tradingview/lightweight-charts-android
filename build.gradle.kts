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
    source = files(".")
    buildUponDefaultConfig = false
    config = files("$projectDir/default-detekt-config.yml")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

tasks.register("detektSource") {
    dependsOn(tasks.findByName("detekt"))
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")

    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.13.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
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


//tasks.register<Delete>("clean") {
//    delete(rootProject.layout.buildDirectory)
//}


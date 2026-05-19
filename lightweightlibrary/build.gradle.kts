import com.android.build.api.dsl.LibraryExtension
import com.tradingview.lightweightcharts.build.NpmBuildTask

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.builtin.kotlin)
    alias(libs.plugins.maven.publish)
}

extensions.configure<LibraryExtension>("android") {
    namespace = "com.tradingview.lightweightcharts"
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        version = libs.versions.lightweightcharts.asProvider().get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        // Publish only the release AAR variant. This matches the Maven artifact consumed by
        // Android clients and avoids exposing debug-only sample configuration.
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}


val npmBuild = tasks.register<NpmBuildTask>("npmBuild") {
    inputs.file(layout.projectDirectory.file("package.json"))
    listOf("package-lock.json", "npm-shrinkwrap.json").forEach { lockfile ->
        val candidate = layout.projectDirectory.file(lockfile)
        if (candidate.asFile.exists()) {
            inputs.file(candidate)
        }
    }
    inputs.file(layout.projectDirectory.file("webpack.config.js"))
    inputs.dir(layout.projectDirectory.dir("lib"))
    outputs.dir(layout.projectDirectory.dir("src/main/assets/com/tradingview/lightweightcharts/scripts"))
}

tasks.configureEach {
    if (name.startsWith("merge") && name.endsWith("Assets")) {
        dependsOn(npmBuild)
    }
}

afterEvaluate {
    publishing {
        repositories {
            maven(url = "${layout.buildDirectory.get()}/repo")
        }

        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.tradingview"
                artifactId = "lightweightcharts"
                version = libs.versions.lightweightcharts.asProvider().get()
                pom {
                    name = "Android Lightweight Charts"
                    description = "The Android Lightweight Charts is an Android wrapper of " +
                        "the TradingView Lightweight Charts library."
                    url = "https://github.com/tradingview/lightweight-charts-android"

                    licenses {
                        license {
                            name = "The Apache License, Version 2.0"
                            url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                        }
                    }

                    developers {
                        developer {
                            name = "Alexandr Nevyantsev"
                            email = "anevyantsev@tradingview.com"
                            organization = "TradingView"
                            organizationUrl = "https://www.tradingview.com/"
                        }
                    }

                    scm {
                        connection = "scm:git:git://github.com/tradingview/lightweight-charts-android.git"
                        developerConnection = "scm:git:ssh://github.com/tradingview/lightweight-charts-android.git"
                        url = "http://github.com/tradingview/lightweight-charts-android/tree/master"
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.webkit)
    implementation(libs.gson)

    testImplementation(libs.junit)
}

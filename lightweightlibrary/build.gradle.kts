import java.io.ByteArrayOutputStream

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {
    namespace = "com.tradingview.lightweightcharts"
    compileSdk = 36

    defaultConfig {
        minSdk = 23

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

    kotlinOptions {
        jvmTarget = "17"
    }
}

fun evaluateShellScript(vararg script: String) {
    val outputStream = ByteArrayOutputStream()
    try {
        project.exec {
            if (System.getProperty("os.name").lowercase().contains("windows")) {
                val command = script.joinToString(" ") + "; if (!\$?) { exit 1 }"
                commandLine("powershell", "-Command", command)
            } else {
                commandLine(*script)
            }
            errorOutput = outputStream
            standardOutput = outputStream
        }
    } catch (e: Exception) {
        e.printStackTrace()
        throw IllegalStateException(outputStream.toString())
    }
}

tasks.register("npmBuild") {
    doLast {
        evaluateShellScript("npm", "install")
        evaluateShellScript("npm", "run", "compile")
        evaluateShellScript("npm", "run", "compile-price-formatter")
        evaluateShellScript("npm", "run", "compile-time-formatter")
        evaluateShellScript("npm", "run", "compile-eval-plugin")
    }
}

afterEvaluate {
    publishing {
        repositories {
            maven(url = "${layout.buildDirectory}/repo")
        }

        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.tradingview"
                artifactId = "lightweightcharts"
                version = android.defaultConfig.versionName
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
    implementation("androidx.webkit:webkit:1.14.0")
    implementation("com.google.code.gson:gson:2.13.2")
}

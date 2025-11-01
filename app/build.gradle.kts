plugins {
    alias(libs.plugins.android)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
//    alias(libs.plugins.kotlin)
//    alias(libs.plugins.kotlin.kapt)
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.tradingview.lightweightcharts.example.app"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.tradingview.lightweightcharts.example"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "consumer-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
//    implementation("com.tradingview:lightweightcharts:3.8.0")
    implementation(project(":lightweightlibrary"))

    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))
    implementation("com.google.firebase:firebase-crashlytics")

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("com.github.permissions-dispatcher:permissionsdispatcher:4.9.2")
    kapt("com.github.permissions-dispatcher:permissionsdispatcher-processor:4.9.2")
}

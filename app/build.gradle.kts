import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.android)
    alias(libs.plugins.android.builtin.kotlin)
    alias(libs.plugins.android.legacy.kapt)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
}

extensions.configure<ApplicationExtension>("android") {
    namespace = "com.tradingview.lightweightcharts.example.app"
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        applicationId = "com.tradingview.lightweightcharts.example"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.compile.get().toInt()
        versionCode = libs.versions.lightweightcharts.code.get().toInt()
        versionName = libs.versions.lightweightcharts.asProvider().get()
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

    packaging {
        jniLibs {
            keepDebugSymbols += "**/libdatastore_shared_counter.so"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
//   implementation(libs.tradingview.lightweightcharts)
    implementation(project(":lightweightlibrary"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.permissionsdispatcher)
    add("kapt", libs.permissionsdispatcher.processor)
}

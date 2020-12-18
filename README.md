# Lightweight-charts-android
Android wrapper for lightweight-charts library

# How to use

In `/build.gradle`

```groovy
allprojects {
    repositories {
        google()
        jcenter()
        //...
        maven {
            url "https://dl.bintray.com/tradingview-org/charts"
        }
    }
}
```

In `/gradle_module/build.gradle`

```groovy
dependencies {
    //...
    implementation 'com.tradingview:lightweightcharts:3.0.0-beta02'
}
```
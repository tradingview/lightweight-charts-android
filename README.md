# Lightweight-charts-android

The Android Lightweight Charts is an Android wrapper of the [TradingView Lightweight Charts](https://github.com/tradingview/lightweight-charts) library.

## Requirements

- minSdkVersion 21
- Installed WebView with support of ES6

## How to use

In `/build.gradle`

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
```

In `/gradle_module/build.gradle`

```groovy
dependencies {
    //...
    implementation 'com.tradingview:lightweightcharts:4.0.0'
}
```

Add view to the layout.

```xml
<androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.tradingview.lightweightcharts.view.ChartsView
            android:id="@+id/charts_view"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

Configure the chart layout.

```kotlin
charts_view.api.applyOptions {
    layout = layoutOptions {
        backgroundColor = Color.LTGRAY.toIntColor()
        textColor = Color.BLACK.toIntColor()
    }
    localization = localizationOptions {
        locale = "ru-RU"
        priceFormatter = PriceFormatter(template = "{price:#2:#3}$")
        timeFormatter = TimeFormatter(
            locale = "ru-RU",
            dateTimeFormat = DateTimeFormat.DATE_TIME
        )
    }
}
```

Add any series to the chart and store a reference to it.

```kotlin
lateinit var histogramSeries: SeriesApi
charts_view.api.addHistogramSeries(
    onSeriesCreated = { series ->
        histogramSeries = series
    }
)
```

Add data to the series.

```kotlin
val data = listOf(
    HistogramData(Time.BusinessDay(2019, 6, 11), 40.01f),
    HistogramData(Time.BusinessDay(2019, 6, 12), 52.38f),
    HistogramData(Time.BusinessDay(2019, 6, 13), 36.30f),
    HistogramData(Time.BusinessDay(2019, 6, 14), 34.48f),
    WhitespaceData(Time.BusinessDay(2019, 6, 15)),
    WhitespaceData(Time.BusinessDay(2019, 6, 16)),
    HistogramData(Time.BusinessDay(2019, 6, 17), 41.50f),
    HistogramData(Time.BusinessDay(2019, 6, 18), 34.82f)
)
histogramSeries.setData(data)
```

## License

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this software except in compliance with the License. You may obtain a copy of the License at LICENSE file. Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

This software incorporates several parts of tslib (https://github.com/Microsoft/tslib, (c) Microsoft Corporation) that are covered by the Apache License, Version 2.0.

This license requires specifying TradingView as the product creator. You shall add the "attribution notice" from the NOTICE file and a link to https://www.tradingview.com/ to the page of your website or mobile application that is available to your users. As thanks for creating this product, we'd be grateful if you add it in a prominent place.

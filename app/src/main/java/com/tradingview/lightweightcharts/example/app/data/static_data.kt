package com.tradingview.lightweightcharts.example.app.data

import com.tradingview.lightweightcharts.api.options.enums.PriceAxisPosition
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerShape
import com.tradingview.lightweightcharts.api.series.models.*

@Suppress("LongMethod")
fun listBarSeriesData(): MutableList<SeriesData> {
    return mutableListOf(
        BarData(
            time = Time.StringTime("2019-07-19"),
            open = 141.77f,
            high = 170.39f,
            low = 120.25f,
            close = 145.72f
        ),
        BarData(
            time = Time.StringTime("2019-07-20"),
            open = 145.72f,
            high = 147.99f,
            low = 100.11f,
            close = 108.19f
        ),
        BarData(
            time = Time.StringTime("2019-07-21"),
            open = 108.19f,
            high = 118.43f,
            low = 74.22f,
            close = 75.16f
        ),
        BarData(
            time = Time.StringTime("2019-07-22"),
            open = 75.16f,
            high = 82.84f,
            low = 36.16f,
            close = 45.72f
        ),
        BarData(
            time = Time.StringTime("2019-07-23"),
            open = 45.12f,
            high = 53.90f,
            low = 45.12f,
            close = 48.09f
        ),
        BarData(
            time = Time.StringTime("2019-07-24"),
            open = 60.71f,
            high = 60.71f,
            low = 53.39f,
            close = 59.29f
        ),
        BarData(
            time = Time.StringTime("2019-07-25"),
            open = 68.26f,
            high = 68.26f,
            low = 59.04f,
            close = 60.50f
        ),
        BarData(
            time = Time.StringTime("2019-07-26"),
            open = 67.71f,
            high = 105.85f,
            low = 66.67f,
            close = 91.04f
        ),
        BarData(
            time = Time.StringTime("2019-07-27"),
            open = 91.04f,
            high = 121.40f,
            low = 82.70f,
            close = 111.40f
        ),
        BarData(
            time = Time.StringTime("2019-07-28"),
            open = 111.51f,
            high = 142.83f,
            low = 103.34f,
            close = 131.25f
        ),
        BarData(
            time = Time.StringTime("2019-07-29"),
            open = 131.33f,
            high = 151.17f,
            low = 77.68f,
            close = 96.43f
        ),
        BarData(
            time = Time.StringTime("2019-07-30"),
            open = 106.33f,
            high = 110.20f,
            low = 90.39f,
            close = 98.10f
        ),
        BarData(
            time = Time.StringTime("2019-07-31"),
            open = 109.87f,
            high = 114.69f,
            low = 85.66f,
            close = 111.26f
        )
    )

}

fun listAreaSeriesData(): MutableList<SeriesData> {
    return mutableListOf(
        LineData(Time.Utc(1560211200), 40.01f),
        LineData(Time.Utc(1560297600), 52.38f),
        LineData(Time.Utc(1560384000), 36.30f),
        LineData(Time.Utc(1560470400), 34.48f),
        LineData(Time.Utc(1560556800), 40.06f),
        LineData(Time.Utc(1560643200), 56.94f),
        LineData(Time.Utc(1560729600), 41.50f),
        LineData(Time.Utc(1560816000), 34.82f)
    )
}

fun listHistogramSeriesData(): MutableList<SeriesData> {
    return mutableListOf(
        HistogramData(Time.BusinessDay(2019, 6, 11), 40.01f),
        HistogramData(Time.BusinessDay(2019, 6, 12), 52.38f),
        HistogramData(Time.BusinessDay(2019, 6, 13), 36.30f),
        HistogramData(Time.BusinessDay(2019, 6, 14), 34.48f),
        WhitespaceData(Time.BusinessDay(2019, 6, 15)),
        WhitespaceData(Time.BusinessDay(2019, 6, 16)),
        HistogramData(Time.BusinessDay(2019, 6, 17), 41.50f),
        HistogramData(Time.BusinessDay(2019, 6, 18), 34.82f)
    )
}

@Suppress("LongMethod")
fun listCandlestickSeriesData(): MutableList<SeriesData> {
    return mutableListOf(
        BarData(
            time = Time.StringTime("2019-12-15"),
            open = 145.72f,
            high = 147.99f,
            low = 100.11f,
            close = 108.19f
        ),
        BarData(
            time = Time.StringTime("2019-12-16"),
            open = 108.19f,
            high = 118.43f,
            low = 74.22f,
            close = 75.16f
        ),
        BarData(
            time = Time.StringTime("2019-12-17"),
            open = 75.16f,
            high = 82.84f,
            low = 36.16f,
            close = 45.72f
        ),
        BarData(
            time = Time.StringTime("2019-12-18"),
            open = 60.71f,
            high = 60.71f,
            low = 53.39f,
            close = 59.29f
        ),
        BarData(
            time = Time.StringTime("2019-12-19"),
            open = 68.26f,
            high = 68.26f,
            low = 59.04f,
            close = 60.50f
        ),
        BarData(
            time = Time.StringTime("2019-12-20"),
            open = 91.04f,
            high = 121.40f,
            low = 82.70f,
            close = 111.40f
        ),
        BarData(
            time = Time.StringTime("2019-12-21"),
            open = 131.33f,
            high = 151.17f,
            low = 77.68f,
            close = 96.43f
        ),
        BarData(
            time = Time.StringTime("2019-12-22"),
            open = 109.87f,
            high = 114.69f,
            low = 85.66f,
            close = 111.26f
        )
    )
}

fun priceScaleOptions() = PriceScaleOptions(autoScale = false, position = PriceAxisPosition.LEFT)
fun timeScaleOptions() = TimeScaleOptions(visible = false)


fun priceLineOptionsWith(price: Float): PriceLineOptions {
    return PriceLineOptions(
        price = price,
        color = 0xFF016309.toInt(),
        lineWidth = LineWidth.TWO,
        lineStyle = LineStyle.Dotted
    )
}

fun priceLineOptions(): PriceLineOptions {
    return PriceLineOptions(
        price = 140f,
        color = 0xFF0F2FFF.toInt(),
        lineWidth = LineWidth.ONE,
        lineStyle = LineStyle.Solid
    )
}

fun singleLineData() = LineData(Time.StringTime("2019-04-11"), 90.01f)

fun listSeriesMarker(): MutableList<SeriesMarker> {
    return mutableListOf(
        SeriesMarker(
            time = Time.StringTime("2019-04-11"),
            position = SeriesMarkerPosition.ABOVE_BAR,
            shape = SeriesMarkerShape.SQUARE,
            color = 0xFFFF1115.toInt(),
            id = "1"
        ),
        SeriesMarker(
            time = Time.StringTime("2019-04-18"),
            position = SeriesMarkerPosition.IN_BAR,
            shape = SeriesMarkerShape.CIRCLE,
            color = 0xFFFF1115.toInt(),
            id = "2"
        ),
        SeriesMarker(
            time = Time.StringTime("2019-04-13"),
            position = SeriesMarkerPosition.BELOW_BAR,
            shape = SeriesMarkerShape.ARROW_DOWN,
            color = 0xFFFF1115.toInt(),
            id = "3"
        )
    )
}

fun listLineSeriesData(): MutableList<SeriesData> {
    return mutableListOf(
        LineData(Time.StringTime("2019-04-11"), 80.01f),
        LineData(Time.StringTime("2019-04-12"), 92.38f),
        LineData(Time.StringTime("2019-04-13"), 76.30f),
        LineData(Time.StringTime("2019-04-14"), 74.48f),
        LineData(Time.StringTime("2019-04-15"), 80.06f),
        LineData(Time.StringTime("2019-04-16"), 96.94f),
        LineData(Time.StringTime("2019-04-17"), 81.50f),
        LineData(Time.StringTime("2019-04-18"), 74.82f)
    )
}


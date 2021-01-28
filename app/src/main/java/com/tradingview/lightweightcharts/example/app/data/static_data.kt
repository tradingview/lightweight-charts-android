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
        BarData(time = Time.StringTime("2018-10-19"), open = 180.34f, high = 180.99f, low = 178.57f, close = 179.85f),
        BarData(time = Time.StringTime("2018-10-22"), open = 180.82f, high = 181.40f, low = 177.56f, close = 178.75f),
        BarData(time = Time.StringTime("2018-10-23"), open = 175.77f, high = 179.49f, low = 175.44f, close = 178.53f),
        BarData(time = Time.StringTime("2018-10-24"), open = 178.58f, high = 182.37f, low = 176.31f, close = 176.97f),
        BarData(time = Time.StringTime("2018-10-25"), open = 177.52f, high = 180.50f, low = 176.83f, close = 179.07f),
        BarData(time = Time.StringTime("2018-10-26"), open = 176.88f, high = 177.34f, low = 170.91f, close = 172.23f),
        BarData(time = Time.StringTime("2018-10-29"), open = 173.74f, high = 175.99f, low = 170.95f, close = 173.20f),
        BarData(time = Time.StringTime("2018-10-30"), open = 173.16f, high = 176.43f, low = 172.64f, close = 176.24f),
        BarData(time = Time.StringTime("2018-10-31"), open = 177.98f, high = 178.85f, low = 175.59f, close = 175.88f),
        BarData(time = Time.StringTime("2018-11-01"), open = 176.84f, high = 180.86f, low = 175.90f, close = 180.46f),
        BarData(time = Time.StringTime("2018-11-02"), open = 182.47f, high = 183.01f, low = 177.39f, close = 179.93f),
        BarData(time = Time.StringTime("2018-11-05"), open = 181.02f, high = 182.41f, low = 179.30f, close = 182.19f),
        BarData(time = Time.StringTime("2018-11-06"), open = 181.93f, high = 182.65f, low = 180.05f, close = 182.01f),
        BarData(time = Time.StringTime("2018-11-07"), open = 183.79f, high = 187.68f, low = 182.06f, close = 187.23f),
        BarData(time = Time.StringTime("2018-11-08"), open = 187.13f, high = 188.69f, low = 185.72f, close = 188.00f),
        BarData(time = Time.StringTime("2018-11-09"), open = 188.32f, high = 188.48f, low = 184.96f, close = 185.99f),
        BarData(time = Time.StringTime("2018-11-12"), open = 185.23f, high = 186.95f, low = 179.02f, close = 179.43f),
        BarData(time = Time.StringTime("2018-11-13"), open = 177.30f, high = 181.62f, low = 172.85f, close = 179.00f),
        BarData(time = Time.StringTime("2018-11-14"), open = 182.61f, high = 182.90f, low = 179.15f, close = 179.90f),
        BarData(time = Time.StringTime("2018-11-15"), open = 179.01f, high = 179.67f, low = 173.61f, close = 177.36f),
        BarData(time = Time.StringTime("2018-11-16"), open = 173.99f, high = 177.60f, low = 173.51f, close = 177.02f),
        BarData(time = Time.StringTime("2018-11-19"), open = 176.71f, high = 178.88f, low = 172.30f, close = 173.59f),
        BarData(time = Time.StringTime("2018-11-20"), open = 169.25f, high = 172.00f, low = 167.00f, close = 169.05f),
        BarData(time = Time.StringTime("2018-11-21"), open = 170.00f, high = 170.93f, low = 169.15f, close = 169.30f),
        BarData(time = Time.StringTime("2018-11-23"), open = 169.39f, high = 170.33f, low = 168.47f, close = 168.85f),
        BarData(time = Time.StringTime("2018-11-26"), open = 170.20f, high = 172.39f, low = 168.87f, close = 169.82f),
        BarData(time = Time.StringTime("2018-11-27"), open = 169.11f, high = 173.38f, low = 168.82f, close = 173.22f),
        BarData(time = Time.StringTime("2018-11-28"), open = 172.91f, high = 177.65f, low = 170.62f, close = 177.43f),
        BarData(time = Time.StringTime("2018-11-29"), open = 176.80f, high = 177.27f, low = 174.92f, close = 175.66f),
        BarData(time = Time.StringTime("2018-11-30"), open = 175.75f, high = 180.37f, low = 175.11f, close = 180.32f),
        BarData(time = Time.StringTime("2018-12-03"), open = 183.29f, high = 183.50f, low = 179.35f, close = 181.74f),
        BarData(time = Time.StringTime("2018-12-04"), open = 181.06f, high = 182.23f, low = 174.55f, close = 175.30f),
        BarData(time = Time.StringTime("2018-12-06"), open = 173.50f, high = 176.04f, low = 170.46f, close = 175.96f),
        BarData(time = Time.StringTime("2018-12-07"), open = 175.35f, high = 178.36f, low = 172.24f, close = 172.79f),
        BarData(time = Time.StringTime("2018-12-10"), open = 173.39f, high = 173.99f, low = 167.73f, close = 171.69f),
        BarData(time = Time.StringTime("2018-12-11"), open = 174.30f, high = 175.60f, low = 171.24f, close = 172.21f),
        BarData(time = Time.StringTime("2018-12-12"), open = 173.75f, high = 176.87f, low = 172.81f, close = 174.21f),
        BarData(time = Time.StringTime("2018-12-13"), open = 174.31f, high = 174.91f, low = 172.07f, close = 173.87f),
        BarData(time = Time.StringTime("2018-12-14"), open = 172.98f, high = 175.14f, low = 171.95f, close = 172.29f),
        BarData(time = Time.StringTime("2018-12-17"), open = 171.51f, high = 171.99f, low = 166.93f, close = 167.97f),
        BarData(time = Time.StringTime("2018-12-18"), open = 168.90f, high = 171.95f, low = 168.50f, close = 170.04f),
        BarData(time = Time.StringTime("2018-12-19"), open = 170.92f, high = 174.95f, low = 166.77f, close = 167.56f),
        BarData(time = Time.StringTime("2018-12-20"), open = 166.28f, high = 167.31f, low = 162.23f, close = 164.16f),
        BarData(time = Time.StringTime("2018-12-21"), open = 162.81f, high = 167.96f, low = 160.17f, close = 160.48f),
        BarData(time = Time.StringTime("2018-12-24"), open = 160.16f, high = 161.40f, low = 158.09f, close = 158.14f),
        BarData(time = Time.StringTime("2018-12-26"), open = 159.46f, high = 168.28f, low = 159.44f, close = 168.28f),
        BarData(time = Time.StringTime("2018-12-27"), open = 166.44f, high = 170.46f, low = 163.36f, close = 170.32f),
        BarData(time = Time.StringTime("2018-12-28"), open = 171.22f, high = 173.12f, low = 168.60f, close = 170.22f),
        BarData(time = Time.StringTime("2018-12-31"), open = 171.47f, high = 173.24f, low = 170.65f, close = 171.82f),
        BarData(time = Time.StringTime("2019-01-02"), open = 169.71f, high = 173.18f, low = 169.05f, close = 172.41f),
        BarData(time = Time.StringTime("2019-01-03"), open = 171.84f, high = 171.84f, low = 168.21f, close = 168.61f),
        BarData(time = Time.StringTime("2019-01-04"), open = 170.18f, high = 174.74f, low = 169.52f, close = 173.62f),
        BarData(time = Time.StringTime("2019-01-07"), open = 173.83f, high = 178.18f, low = 173.83f, close = 177.04f),
        BarData(time = Time.StringTime("2019-01-08"), open = 178.57f, high = 179.59f, low = 175.61f, close = 177.89f),
        BarData(time = Time.StringTime("2019-01-09"), open = 177.87f, high = 181.27f, low = 177.10f, close = 179.73f),
        BarData(time = Time.StringTime("2019-01-10"), open = 178.03f, high = 179.24f, low = 176.34f, close = 179.06f),
        BarData(time = Time.StringTime("2019-01-11"), open = 177.93f, high = 180.26f, low = 177.12f, close = 179.41f),
        BarData(time = Time.StringTime("2019-01-14"), open = 177.59f, high = 179.23f, low = 176.90f, close = 178.81f),
        BarData(time = Time.StringTime("2019-01-15"), open = 176.08f, high = 177.82f, low = 175.20f, close = 176.47f),
        BarData(time = Time.StringTime("2019-01-16"), open = 177.09f, high = 177.93f, low = 175.86f, close = 177.04f),
        BarData(time = Time.StringTime("2019-01-17"), open = 174.01f, high = 175.46f, low = 172.00f, close = 174.87f),
        BarData(time = Time.StringTime("2019-01-18"), open = 176.98f, high = 180.04f, low = 176.18f, close = 179.58f),
        BarData(time = Time.StringTime("2019-01-22"), open = 177.49f, high = 178.60f, low = 175.36f, close = 177.11f),
        BarData(time = Time.StringTime("2019-01-23"), open = 176.59f, high = 178.06f, low = 174.53f, close = 176.89f),
        BarData(time = Time.StringTime("2019-01-24"), open = 177.00f, high = 177.53f, low = 175.30f, close = 177.29f),
        BarData(time = Time.StringTime("2019-01-25"), open = 179.78f, high = 180.87f, low = 178.61f, close = 180.40f),
        BarData(time = Time.StringTime("2019-01-28"), open = 178.97f, high = 179.99f, low = 177.41f, close = 179.83f),
        BarData(time = Time.StringTime("2019-01-29"), open = 178.96f, high = 180.15f, low = 178.09f, close = 179.69f),
        BarData(time = Time.StringTime("2019-01-30"), open = 180.47f, high = 184.20f, low = 179.78f, close = 182.18f),
        BarData(time = Time.StringTime("2019-01-31"), open = 181.50f, high = 184.67f, low = 181.06f, close = 183.53f),
        BarData(time = Time.StringTime("2019-02-01"), open = 184.03f, high = 185.15f, low = 182.83f, close = 184.37f),
        BarData(time = Time.StringTime("2019-02-04"), open = 184.30f, high = 186.43f, low = 183.84f, close = 186.43f),
        BarData(time = Time.StringTime("2019-02-05"), open = 186.89f, high = 186.99f, low = 184.69f, close = 186.39f),
        BarData(time = Time.StringTime("2019-02-06"), open = 186.69f, high = 186.69f, low = 184.06f, close = 184.72f),
        BarData(time = Time.StringTime("2019-02-07"), open = 183.74f, high = 184.92f, low = 182.45f, close = 184.07f),
        BarData(time = Time.StringTime("2019-02-08"), open = 183.05f, high = 184.58f, low = 182.72f, close = 184.54f),
        BarData(time = Time.StringTime("2019-02-11"), open = 185.00f, high = 185.42f, low = 182.75f, close = 182.92f),
        BarData(time = Time.StringTime("2019-02-12"), open = 183.84f, high = 186.40f, low = 183.52f, close = 185.52f),
        BarData(time = Time.StringTime("2019-02-13"), open = 186.30f, high = 188.68f, low = 185.92f, close = 188.41f),
        BarData(time = Time.StringTime("2019-02-14"), open = 187.50f, high = 188.93f, low = 186.00f, close = 187.71f),
        BarData(time = Time.StringTime("2019-02-15"), open = 189.87f, high = 192.62f, low = 189.05f, close = 192.39f),
        BarData(time = Time.StringTime("2019-02-19"), open = 191.71f, high = 193.19f, low = 191.28f, close = 192.33f),
        BarData(time = Time.StringTime("2019-02-20"), open = 192.39f, high = 192.40f, low = 191.11f, close = 191.85f),
        BarData(time = Time.StringTime("2019-02-21"), open = 191.85f, high = 192.37f, low = 190.61f, close = 191.82f),
        BarData(time = Time.StringTime("2019-02-22"), open = 191.69f, high = 192.54f, low = 191.62f, close = 192.39f),
        BarData(time = Time.StringTime("2019-02-25"), open = 192.75f, high = 193.42f, low = 189.96f, close = 189.98f),
        BarData(time = Time.StringTime("2019-02-26"), open = 185.59f, high = 188.47f, low = 182.80f, close = 188.30f),
        BarData(time = Time.StringTime("2019-02-27"), open = 187.90f, high = 188.50f, low = 183.21f, close = 183.67f),
        BarData(time = Time.StringTime("2019-02-28"), open = 183.60f, high = 185.19f, low = 183.11f, close = 185.14f),
        BarData(time = Time.StringTime("2019-03-01"), open = 185.82f, high = 186.56f, low = 182.86f, close = 185.17f),
        BarData(time = Time.StringTime("2019-03-04"), open = 186.20f, high = 186.24f, low = 182.10f, close = 183.81f),
        BarData(time = Time.StringTime("2019-03-05"), open = 184.24f, high = 185.12f, low = 183.25f, close = 184.00f),
        BarData(time = Time.StringTime("2019-03-06"), open = 184.53f, high = 184.97f, low = 183.84f, close = 184.45f),
        BarData(time = Time.StringTime("2019-03-07"), open = 184.39f, high = 184.62f, low = 181.58f, close = 182.51f),
        BarData(time = Time.StringTime("2019-03-08"), open = 181.49f, high = 181.91f, low = 179.52f, close = 181.23f),
        BarData(time = Time.StringTime("2019-03-11"), open = 182.00f, high = 183.20f, low = 181.20f, close = 182.44f),
        BarData(time = Time.StringTime("2019-03-12"), open = 183.43f, high = 184.27f, low = 182.33f, close = 184.00f),
        BarData(time = Time.StringTime("2019-03-13"), open = 183.24f, high = 183.78f, low = 181.08f, close = 181.14f),
        BarData(time = Time.StringTime("2019-03-14"), open = 181.28f, high = 181.74f, low = 180.50f, close = 181.61f),
        BarData(time = Time.StringTime("2019-03-15"), open = 182.30f, high = 182.49f, low = 179.57f, close = 182.23f),
        BarData(time = Time.StringTime("2019-03-18"), open = 182.53f, high = 183.48f, low = 182.33f, close = 183.42f),
        BarData(time = Time.StringTime("2019-03-19"), open = 184.19f, high = 185.82f, low = 183.48f, close = 184.13f),
        BarData(time = Time.StringTime("2019-03-20"), open = 184.30f, high = 187.12f, low = 183.43f, close = 186.10f),
        BarData(time = Time.StringTime("2019-03-21"), open = 185.50f, high = 190.00f, low = 185.50f, close = 189.97f),
        BarData(time = Time.StringTime("2019-03-22"), open = 189.31f, high = 192.05f, low = 188.67f, close = 188.75f),
        BarData(time = Time.StringTime("2019-03-25"), open = 188.75f, high = 191.71f, low = 188.51f, close = 189.68f),
        BarData(time = Time.StringTime("2019-03-26"), open = 190.69f, high = 192.19f, low = 188.74f, close = 189.34f),
        BarData(time = Time.StringTime("2019-03-27"), open = 189.65f, high = 191.61f, low = 188.39f, close = 189.25f),
        BarData(time = Time.StringTime("2019-03-28"), open = 189.91f, high = 191.40f, low = 189.16f, close = 190.06f),
        BarData(time = Time.StringTime("2019-03-29"), open = 190.85f, high = 192.04f, low = 190.14f, close = 191.89f),
        BarData(time = Time.StringTime("2019-04-01"), open = 192.99f, high = 195.90f, low = 192.85f, close = 195.64f),
        BarData(time = Time.StringTime("2019-04-02"), open = 195.50f, high = 195.50f, low = 194.01f, close = 194.31f),
        BarData(time = Time.StringTime("2019-04-03"), open = 194.98f, high = 198.78f, low = 194.11f, close = 198.61f),
        BarData(time = Time.StringTime("2019-04-04"), open = 199.00f, high = 200.49f, low = 198.02f, close = 200.45f),
        BarData(time = Time.StringTime("2019-04-05"), open = 200.86f, high = 203.13f, low = 200.61f, close = 202.06f),
        BarData(time = Time.StringTime("2019-04-08"), open = 201.37f, high = 203.79f, low = 201.24f, close = 203.55f),
        BarData(time = Time.StringTime("2019-04-09"), open = 202.26f, high = 202.71f, low = 200.46f, close = 200.90f),
        BarData(time = Time.StringTime("2019-04-10"), open = 201.26f, high = 201.60f, low = 198.02f, close = 199.43f),
        BarData(time = Time.StringTime("2019-04-11"), open = 199.90f, high = 201.50f, low = 199.03f, close = 201.48f),
        BarData(time = Time.StringTime("2019-04-12"), open = 202.13f, high = 204.26f, low = 202.13f, close = 203.85f),
        BarData(time = Time.StringTime("2019-04-15"), open = 204.16f, high = 205.14f, low = 203.40f, close = 204.86f),
        BarData(time = Time.StringTime("2019-04-16"), open = 205.25f, high = 205.99f, low = 204.29f, close = 204.47f),
        BarData(time = Time.StringTime("2019-04-17"), open = 205.34f, high = 206.84f, low = 205.32f, close = 206.55f),
        BarData(time = Time.StringTime("2019-04-18"), open = 206.02f, high = 207.78f, low = 205.10f, close = 205.66f),
        BarData(time = Time.StringTime("2019-04-22"), open = 204.11f, high = 206.25f, low = 204.00f, close = 204.78f),
        BarData(time = Time.StringTime("2019-04-23"), open = 205.14f, high = 207.33f, low = 203.43f, close = 206.05f),
        BarData(time = Time.StringTime("2019-04-24"), open = 206.16f, high = 208.29f, low = 205.54f, close = 206.72f),
        BarData(time = Time.StringTime("2019-04-25"), open = 206.01f, high = 207.72f, low = 205.06f, close = 206.50f),
        BarData(time = Time.StringTime("2019-04-26"), open = 205.88f, high = 206.14f, low = 203.34f, close = 203.61f),
        BarData(time = Time.StringTime("2019-04-29"), open = 203.31f, high = 203.80f, low = 200.34f, close = 202.16f),
        BarData(time = Time.StringTime("2019-04-30"), open = 201.55f, high = 203.75f, low = 200.79f, close = 203.70f),
        BarData(time = Time.StringTime("2019-05-01"), open = 203.20f, high = 203.52f, low = 198.66f, close = 198.80f),
        BarData(time = Time.StringTime("2019-05-02"), open = 199.30f, high = 201.06f, low = 198.80f, close = 201.01f),
        BarData(time = Time.StringTime("2019-05-03"), open = 202.00f, high = 202.31f, low = 200.32f, close = 200.56f),
        BarData(time = Time.StringTime("2019-05-06"), open = 198.74f, high = 199.93f, low = 198.31f, close = 199.63f),
        BarData(time = Time.StringTime("2019-05-07"), open = 196.75f, high = 197.65f, low = 192.96f, close = 194.77f),
        BarData(time = Time.StringTime("2019-05-08"), open = 194.49f, high = 196.61f, low = 193.68f, close = 195.17f),
        BarData(time = Time.StringTime("2019-05-09"), open = 193.31f, high = 195.08f, low = 191.59f, close = 194.58f),
        BarData(time = Time.StringTime("2019-05-10"), open = 193.21f, high = 195.49f, low = 190.01f, close = 194.58f),
        BarData(time = Time.StringTime("2019-05-13"), open = 191.00f, high = 191.66f, low = 189.14f, close = 190.34f),
        BarData(time = Time.StringTime("2019-05-14"), open = 190.50f, high = 192.76f, low = 190.01f, close = 191.62f),
        BarData(time = Time.StringTime("2019-05-15"), open = 190.81f, high = 192.81f, low = 190.27f, close = 191.76f),
        BarData(time = Time.StringTime("2019-05-16"), open = 192.47f, high = 194.96f, low = 192.20f, close = 192.38f),
        BarData(time = Time.StringTime("2019-05-17"), open = 190.86f, high = 194.50f, low = 190.75f, close = 192.58f),
        BarData(time = Time.StringTime("2019-05-20"), open = 191.13f, high = 192.86f, low = 190.61f, close = 190.95f),
        BarData(time = Time.StringTime("2019-05-21"), open = 187.13f, high = 192.52f, low = 186.34f, close = 191.45f),
        BarData(time = Time.StringTime("2019-05-22"), open = 190.49f, high = 192.22f, low = 188.05f, close = 188.91f),
        BarData(time = Time.StringTime("2019-05-23"), open = 188.45f, high = 192.54f, low = 186.27f, close = 192.00f),
        BarData(time = Time.StringTime("2019-05-24"), open = 192.54f, high = 193.86f, low = 190.41f, close = 193.59f),
        BarData(time = Time.StringTime("2019-05-28"), open = 194.38f, high = 196.47f, low = 193.75f, close = 194.08f)
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
        lineStyle = LineStyle.DOTTED
    )
}

fun priceLineOptions(): PriceLineOptions {
    return PriceLineOptions(
        price = 140f,
        color = 0xFF0F2FFF.toInt(),
        lineWidth = LineWidth.ONE,
        lineStyle = LineStyle.SOLID
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


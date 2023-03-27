package com.tradingview.lightweightcharts.example.app.repository

import android.app.Application
import com.tradingview.lightweightcharts.api.series.models.AreaData
import com.tradingview.lightweightcharts.api.series.models.BarData
import com.tradingview.lightweightcharts.api.series.models.Time
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Date
import java.util.concurrent.TimeUnit

class HistoricalDataRepository(private val app: Application) {

    fun getDailyData(): Sequence<AreaData> = sequence {
        val isr = InputStreamReader(app.assets.open("EURUSD.csv"))
        val read = BufferedReader(isr)
        try {
            for (line in read.readLinesSequence()) {
                if (!line.startsWith("<")) {
                    val note = HistNote(line.split(";"))
                    yield(note.toAreaData())
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun getWeeklyData(): Sequence<AreaData> {
        var last: Date? = null
        return getDailyData().filter {
            when {
                last == null -> {
                    last = it.time.date
                    true
                }
                TimeUnit.MILLISECONDS.toDays(it.time.date.time - last!!.time) >= 7 -> {
                    last = it.time.date
                    true
                }
                else -> false
            }
        }
    }


    fun getMonthlyData(): Sequence<AreaData> {
        var last: Date? = null
        return getDailyData().filter {
            when {
                last == null -> {
                    last = it.time.date
                    true
                }
                TimeUnit.MILLISECONDS.toDays(it.time.date.time - last!!.time) >= 31 -> {
                    last = it.time.date
                    true
                }
                else -> false
            }
        }
    }

    fun getYearlyData(): Sequence<AreaData> {
        var last: Date? = null
        return getDailyData().filter {
            when {
                last == null -> {
                    last = it.time.date
                    true
                }
                TimeUnit.MILLISECONDS.toDays(it.time.date.time - last!!.time) >= 6 * 31 -> {
                    last = it.time.date
                    true
                }
                else -> false
            }
        }
    }

}


data class HistNote(
    val splittedLine: List<String>,
) {
    val year: Int get() = splittedLine[2].substring(0, 4).toInt()
    val month: Int get() = splittedLine[2].substring(4, 6).toInt()
    val day: Int get() = splittedLine[2].substring(6, 8).toInt()

    val open: Float get() = splittedLine[4].toFloat()
    val high: Float get() = splittedLine[5].toFloat()
    val low: Float get() = splittedLine[6].toFloat()
    val close: Float get() = splittedLine[7].toFloat()
    val vol: Float get() = splittedLine[8].toFloat()
}

fun HistNote.toBarData() =
    BarData(
        time = Time.BusinessDay(year, month, day),
        open = open,
        high = high,
        low = low,
        close = close
    )


fun HistNote.toAreaData() =
    AreaData(
        time = Time.BusinessDay(year, month, day),
        value = close,
    )

fun BufferedReader.readLinesSequence(): Sequence<String> {
    val reader = this
    return sequence {
        var line = reader.readLine()
        while (line != null) {
            yield(line)
            line = readLine()
        }
    }
}
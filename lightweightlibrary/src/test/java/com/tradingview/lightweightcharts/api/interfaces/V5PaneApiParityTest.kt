package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.serializer.gson.GsonProvider
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class V5PaneApiParityTest {
    @Test
    fun paneApiExposesNonPluginV5Methods() {
        val exposedMethods = PaneApi::class.java.methods.map { method -> method.name }.toSet()

        assertTrue(
            exposedMethods.containsAll(
                listOf(
                    "addSeries",
                    "priceScale",
                    "setPreserveEmptyPane",
                    "preserveEmptyPane",
                    "getStretchFactor",
                    "setStretchFactor",
                    "getHeight",
                    "setHeight",
                    "moveTo",
                    "paneIndex",
                    "getSeries",
                )
            )
        )
    }

    @Test
    fun markerPositionEnumCoversEveryV5Placement() {
        val gson = GsonProvider.newInstance()
        val serializedPositions = SeriesMarkerPosition.values().map { position ->
            gson.toJson(position).trim('"')
        }

        assertEquals(
            listOf(
                "aboveBar",
                "belowBar",
                "inBar",
                "atPriceTop",
                "atPriceBottom",
                "atPriceMiddle",
            ),
            serializedPositions,
        )
    }
}

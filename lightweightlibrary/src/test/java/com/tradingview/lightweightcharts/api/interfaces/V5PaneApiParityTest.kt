package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.serializer.gson.GsonProvider
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.options.models.SeriesOptionsCommon
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    @Test
    fun paneSnapshotCombinesPaneReadCallbacks() {
        var snapshot: PaneSnapshot? = null

        fakePane().snapshot {
            snapshot = it
        }

        assertNotNull(snapshot)
        assertEquals(2, snapshot!!.index)
        assertEquals(120, snapshot!!.height)
        assertEquals(emptyList<SeriesApi>(), snapshot!!.series)
        assertEquals(true, snapshot!!.preserveEmptyPane)
        assertEquals(0.5f, snapshot!!.stretchFactor)
    }

    @Test
    fun paneApiExtensionsExposeBatchPaneSizeHelper() {
        val source = paneApiExtensionsSource()

        assertTrue(source.contains("fun ChartApi.paneSizes("))
        assertTrue(source.contains("fun PaneApi.snapshot("))
    }

    private fun fakePane() = object : PaneApi {
        override val uuid: String = "pane"

        override fun addSeries(
            type: SeriesType,
            options: SeriesOptionsCommon?,
            onSeriesCreated: (api: SeriesApi) -> Unit,
        ) = Unit

        override fun priceScale(id: PriceScaleId): PriceScaleApi {
            throw UnsupportedOperationException()
        }

        override fun getHeight(onHeightReceived: (Int) -> Unit) {
            onHeightReceived(120)
        }

        override fun setHeight(height: Int) = Unit

        override fun moveTo(paneIndex: Int) = Unit

        override fun paneIndex(onPaneIndexReceived: (Int) -> Unit) {
            onPaneIndexReceived(2)
        }

        override fun getSeries(onSeriesReceived: (List<SeriesApi>) -> Unit) {
            onSeriesReceived(emptyList())
        }

        override fun setPreserveEmptyPane(preserve: Boolean) = Unit

        override fun preserveEmptyPane(onPreserveReceived: (Boolean) -> Unit) {
            onPreserveReceived(true)
        }

        override fun getStretchFactor(onStretchFactorReceived: (Float) -> Unit) {
            onStretchFactorReceived(0.5f)
        }

        override fun setStretchFactor(stretchFactor: Float) = Unit
    }

    private fun paneApiExtensionsSource(): String {
        val candidates = listOf(
            File("src/main/java/com/tradingview/lightweightcharts/api/interfaces/PaneApiExtensions.kt"),
            File("lightweightlibrary/src/main/java/com/tradingview/lightweightcharts/api/interfaces/PaneApiExtensions.kt"),
        )
        val file = candidates.firstOrNull { it.isFile }
        assertNotNull("PaneApiExtensions.kt should be readable", file)
        return file!!.readText()
    }
}

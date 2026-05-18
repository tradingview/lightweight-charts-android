package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.options.enums.ConflationPriority
import com.tradingview.lightweightcharts.api.options.models.ChartOptions
import com.tradingview.lightweightcharts.api.options.models.ImageWatermarkOptions
import com.tradingview.lightweightcharts.api.options.models.LayoutOptions
import com.tradingview.lightweightcharts.api.options.models.LayoutPanesOptions
import com.tradingview.lightweightcharts.api.options.models.PriceScaleOptions
import com.tradingview.lightweightcharts.api.options.models.SeriesMarkersOptions
import com.tradingview.lightweightcharts.api.options.models.TextWatermarkLineOptions
import com.tradingview.lightweightcharts.api.options.models.TextWatermarkOptions
import com.tradingview.lightweightcharts.api.options.models.TimeScaleOptions
import com.tradingview.lightweightcharts.api.serializer.gson.GsonProvider
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerShape
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerZOrder
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.api.series.models.Time
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class V5SerializationTest {
    private val gson = GsonProvider.newInstance()

    @Test
    fun chartOptionsSerializePluralOverlayPriceScales() {
        val json = gson.toJsonTree(
            ChartOptions(overlayPriceScale = PriceScaleOptions())
        ).asJsonObject

        assertTrue(json.has("overlayPriceScales"))
        assertFalse(json.has("overlayPriceScale"))
    }

    @Test
    fun layoutOptionsSerializePanesOptions() {
        val json = gson.toJsonTree(
            LayoutOptions(
                panes = LayoutPanesOptions(
                    enableResize = true,
                    separatorColor = "#111111",
                    separatorHoverColor = "#222222",
                )
            )
        ).asJsonObject

        assertTrue(json.has("panes"))
        assertTrue(json["panes"].asJsonObject["enableResize"].asBoolean)
        assertEquals("#111111", json["panes"].asJsonObject["separatorColor"].asString)
        assertEquals("#222222", json["panes"].asJsonObject["separatorHoverColor"].asString)
    }

    @Test
    fun seriesTypeSerializesToV5DefinitionName() {
        assertEquals("\"Line\"", gson.toJson(SeriesType.LINE))
        assertEquals("\"Candlestick\"", gson.toJson(SeriesType.CANDLESTICK))
        assertEquals("\"Baseline\"", gson.toJson(SeriesType.BASELINE))
    }

    @Test
    fun markerSupportsPricePositionAndPriceField() {
        val json = gson.toJsonTree(
            SeriesMarker(
                position = SeriesMarkerPosition.AT_PRICE_TOP,
                shape = SeriesMarkerShape.CIRCLE,
                price = 42.5f,
            )
        ).asJsonObject

        assertEquals("atPriceTop", json["position"].asString)
        assertEquals(42.5f, json["price"].asFloat)
    }

    @Test
    fun markerPrimitiveOptionsUseV5Names() {
        val json = gson.toJsonTree(
            SeriesMarkersOptions(
                zOrder = SeriesMarkerZOrder.ABOVE_SERIES,
                autoScale = true,
            )
        ).asJsonObject

        assertEquals("aboveSeries", json["zOrder"].asString)
        assertTrue(json["autoScale"].asBoolean)
    }

    @Test
    fun textWatermarkOptionsUseV5Fields() {
        val json = gson.toJsonTree(
            TextWatermarkOptions(
                visible = false,
                lines = listOf(TextWatermarkLineOptions(lineHeight = 24))
            )
        ).asJsonObject

        assertFalse(json["visible"].asBoolean)
        assertEquals(24, json["lines"].asJsonArray[0].asJsonObject["lineHeight"].asInt)
    }

    @Test
    fun imageWatermarkOptionsDoNotSerializeTextAlignmentFields() {
        val json = gson.toJsonTree(
            ImageWatermarkOptions(alpha = 0.5f, maxWidth = 120, maxHeight = 80, padding = 12)
        ).asJsonObject

        assertEquals(0.5f, json["alpha"].asFloat)
        assertEquals(120, json["maxWidth"].asInt)
        assertEquals(80, json["maxHeight"].asInt)
        assertEquals(12, json["padding"].asInt)
        assertFalse(json.has("horzAlign"))
        assertFalse(json.has("vertAlign"))
    }

    @Test
    fun timeScaleSerializesConflationOptions() {
        val json = gson.toJsonTree(
            TimeScaleOptions(
                enableConflation = true,
                conflationThresholdFactor = 0.75f,
                precomputeConflationOnInit = true,
                precomputeConflationPriority = ConflationPriority.USER_VISIBLE,
            )
        ).asJsonObject

        assertTrue(json["enableConflation"].asBoolean)
        assertEquals(0.75f, json["conflationThresholdFactor"].asFloat)
        assertTrue(json["precomputeConflationOnInit"].asBoolean)
        assertEquals("user-visible", json["precomputeConflationPriority"].asString)
    }

    @Test
    fun timeRangeDeserializerTreatsNumericStringsAsUtcTimestamps() {
        val json = gson.fromJson(
            """
            {
              "from": "1779277902",
              "to": "1779364302"
            }
            """.trimIndent(),
            com.google.gson.JsonElement::class.java,
        )

        val range = TimeRangeDeserializer().deserialize(json)

        assertEquals(Time.Utc(1_779_277_902L), range?.from)
        assertEquals(Time.Utc(1_779_364_302L), range?.to)
    }

    @Test
    fun timeRangeDeserializerTreatsCompactNumericStringsAsUtc() {
        val json = gson.fromJson(
            """
            {
              "from": "20240115",
              "to": "20240116"
            }
            """.trimIndent(),
            com.google.gson.JsonElement::class.java,
        )

        val range = TimeRangeDeserializer().deserialize(json)

        assertEquals(Time.Utc(20_240_115L), range?.from)
        assertEquals(Time.Utc(20_240_116L), range?.to)
    }

    @Test
    fun stringTimeKeepsLegacyDateTimePrefixParsing() {
        val time = Time.StringTime("2024-01-15T00:00:00")

        assertEquals("2024-01-15T00:00:00", time.source)
        assertEquals(Time.StringTime("2024-01-15").date, time.date)
    }

    @Test
    fun businessDayDateUsesOneBasedMonth() {
        val calendar = Calendar.getInstance().apply {
            time = Time.BusinessDay(2024, 6, 11).date
        }

        assertEquals(2024, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH))
        assertEquals(11, calendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun businessDaySerializationKeepsOneBasedMonth() {
        val json = gson.toJsonTree(Time.BusinessDay(2024, 6, 11)).asJsonObject

        assertEquals(2024, json["year"].asInt)
        assertEquals(6, json["month"].asInt)
        assertEquals(11, json["day"].asInt)
    }

    @Test
    fun timeRangeDeserializerKeepsNonCompactNumericStringsAsUtc() {
        val json = gson.fromJson(
            """
            {
              "from": "999999999",
              "to": "1000000000"
            }
            """.trimIndent(),
            com.google.gson.JsonElement::class.java,
        )

        val range = TimeRangeDeserializer().deserialize(json)

        assertEquals(Time.Utc(999_999_999L), range?.from)
        assertEquals(Time.Utc(1_000_000_000L), range?.to)
    }

    @Test
    fun mouseEventDeserializerKeepsLogicalCompatibilityAndFractionalIndex() {
        val event = gson.fromJson(
            """
            {
              "logical": 5.25,
              "seriesData": []
            }
            """.trimIndent(),
            com.google.gson.JsonElement::class.java,
        )

        val params = MouseEventParamsDeserializer().deserialize(event)

        assertEquals(5, params?.logical)
        assertEquals(5.25f, params?.logicalFloat ?: -1f, 0.0001f)
    }
}

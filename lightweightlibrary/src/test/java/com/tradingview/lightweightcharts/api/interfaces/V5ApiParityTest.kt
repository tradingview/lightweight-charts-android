package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.delegates.WatermarkApiDelegate
import com.tradingview.lightweightcharts.api.options.models.ImageWatermarkOptions
import com.tradingview.lightweightcharts.api.options.models.TextWatermarkOptions
import com.tradingview.lightweightcharts.api.serializer.PrimitiveSerializer
import com.tradingview.lightweightcharts.api.serializer.gson.GsonProvider
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.runtime.messaging.BridgeFunction
import com.tradingview.lightweightcharts.runtime.messaging.BridgeFunctionResult
import com.tradingview.lightweightcharts.runtime.messaging.BridgeMessage
import com.tradingview.lightweightcharts.runtime.messaging.Data
import com.tradingview.lightweightcharts.runtime.messaging.MessageType
import com.google.gson.JsonPrimitive
import com.tradingview.lightweightcharts.Logger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class V5ApiParityTest {

    @Test
    fun chartApiExposesAutoSizeActive() {
        val methodNames = ChartApi::class.java.methods.map { it.name }.toSet()
        assertTrue(methodNames.contains("autoSizeActive"))
    }

    @Test
    fun priceScaleApiExposesExpectedMethods() {
        val methodNames = PriceScaleApi::class.java.methods.map { it.name }.toSet()
        assertTrue(
            methodNames.containsAll(
                listOf(
                    "options",
                    "applyOptions",
                    "width",
                    "setVisibleRange",
                    "getVisibleRange",
                    "setAutoScale",
                )
            )
        )
    }

    @Test
    fun seriesApiExposesDataChangeMethods() {
        val methodNames = SeriesApi::class.java.methods.map { it.name }.toSet()
        assertTrue(
            methodNames.containsAll(
                listOf(
                    "data",
                    "subscribeDataChanged",
                    "unsubscribeDataChanged",
                )
            )
        )
    }

    @Test
    fun addBaselineSeriesUsesBaselineSeriesOptionsAndKeepsCompatibilityOverload() {
        val parameterTypes = ChartApi::class.java.methods
            .filter { it.name == "addBaselineSeries" }
            .map { it.parameterTypes.first().simpleName }
            .toSet()

        assertTrue(parameterTypes.contains("BaselineSeriesOptions"))
        assertTrue(parameterTypes.contains("BaselineStyleOptions"))
    }

    @Test
    fun addPaneUsesExplicitPreserveEmptyPaneParamName() {
        assertTrue(PaneApi.Params.PRESERVE_EMPTY_PANE == "preserveEmptyPane")
    }

    @Test(expected = IllegalArgumentException::class)
    fun textWatermarkRejectsImageOptions() {
        WatermarkApiDelegate("watermark", WebMessageController(), WatermarkApiDelegate.Kind.TEXT)
            .applyImageOptions(ImageWatermarkOptions())
    }

    @Test(expected = IllegalArgumentException::class)
    fun imageWatermarkRejectsTextOptions() {
        WatermarkApiDelegate("watermark", WebMessageController(), WatermarkApiDelegate.Kind.IMAGE)
            .applyTextOptions(TextWatermarkOptions())
    }

    @Test
    fun timeScaleVoidBridgeMethodsResolveCallbacks() {
        val source = timeScaleBridgeSource()

        listOf(
            "scrollToPosition" to "timeScale.scrollToPosition(params.position, params.animated);",
            "timeScaleApplyOptions" to "timeScale.applyOptions(paramsWithPlugin.options);",
            "scrollToRealTime" to "timeScale.scrollToRealTime();",
            "setVisibleRange" to "timeScale.setVisibleRange(params.range);",
            "setVisibleLogicalRange" to "timeScale.setVisibleLogicalRange(params.range);",
            "resetTimeScale" to "timeScale.resetTimeScale();",
            "fitContent" to "timeScale.fitContent();",
        ).forEach { (bridgeName, operation) ->
            val methodStart = source.indexOf("super(\"$bridgeName\"")
            assertTrue("$bridgeName bridge method should exist", methodStart >= 0)
            val nextMethodStart = source.indexOf("class ", methodStart + 1).let { index ->
                if (index == -1) source.length else index
            }
            val methodSource = source.substring(methodStart, nextMethodStart)
            val operationIndex = methodSource.indexOf(operation)
            assertTrue("$bridgeName operation should exist", operationIndex >= 0)
            val resolveIndex = methodSource.indexOf("resolve();", operationIndex)
            assertTrue("$bridgeName should resolve after successful operation", resolveIndex > operationIndex)
        }
    }

    @Test
    fun timeScaleSubscriptionsUseRegisteredUnsubscribeHandler() {
        val source = timeScaleBridgeSource()

        assertTrue(source.contains("(subscriptionRef) =>"))
        assertTrue(source.contains("subscription.unsubscribe(this._timeScale(), subscriptionRef);"))
    }

    @Test
    fun controllerCanClearSubscriptionsWithoutDroppingFunctionCallbacks() {
        val controller = WebMessageController()

        controller.callFunction("remove")
        controller.callSubscribe(
            "subscribeCrosshairMove",
            callback = { _: String -> },
            deserializer = PrimitiveSerializer.StringDeserializer,
        )

        controller.clearSubscriptions()

        assertEquals(0, callbackBufferSize(controller))
    }

    @Test
    fun chartRemoveClearsNativeSubscriptionsBeforeSendingRemove() {
        val controller = WebMessageController()
        val chartApi = com.tradingview.lightweightcharts.api.delegates.ChartApiDelegate(controller)

        chartApi.subscribeClick { }
        chartApi.subscribeCrosshairMove { }
        chartApi.remove()

        assertEquals(0, callbackBufferSize(controller))
    }

    @Test
    fun callbackFunctionResultRemovesCallbackBufferEntry() {
        val controller = WebMessageController()
        var received: String? = null

        val uuid = controller.callFunction(
            "chartOptions",
            callback = { value: String -> received = value },
            deserializer = PrimitiveSerializer.StringDeserializer,
        )

        assertEquals(1, callbackBufferSize(controller))

        controller.onMessage(
            BridgeFunctionResult(
                BridgeMessage(
                    MessageType.FUNCTION_RESULT,
                    Data(uuid = uuid, result = JsonPrimitive("ok")),
                )
            )
        )

        assertEquals("ok", received)
        assertEquals(0, callbackBufferSize(controller))
    }

    @Test
    fun noCallbackFunctionResultIsIgnoredWhenNoCallbackIsBuffered() {
        val controller = WebMessageController()
        val originalLogLevel = Logger.level

        try {
            Logger.level = com.tradingview.lightweightcharts.runtime.messaging.LogLevel.NONE
            val uuid = controller.callFunction("remove")
            controller.onMessage(
                BridgeFunctionResult(
                    BridgeMessage(
                        MessageType.FUNCTION_RESULT,
                        Data(uuid = uuid, result = JsonPrimitive("ignored")),
                    )
                )
            )

            assertEquals(0, callbackBufferSize(controller))
        } finally {
            Logger.level = originalLogLevel
        }
    }

    @Test
    fun bridgeFunctionSerializesResultExpectation() {
        val gson = GsonProvider.newInstance()
        val notification = gson.toJsonTree(
            BridgeFunction("remove", expectsResult = false)
        ).asJsonObject
        val request = gson.toJsonTree(
            BridgeFunction("chartOptions")
        ).asJsonObject

        assertFalse(notification["data"].asJsonObject["expectsResult"].asBoolean)
        assertTrue(request["data"].asJsonObject["expectsResult"].asBoolean)
    }

    @Test
    fun priceScaleSeriesMissingSeriesUsesSingleFatalErrorPath() {
        val source = priceScaleBridgeSource()

        assertTrue(source.contains("Series with uuid:${'$'}{input.params.seriesId} is not found"))
        assertFalse(source.contains("this.functionManager.throwFatalError"))
    }

    private fun timeScaleBridgeSource(): String {
        val candidates = listOf(
            File("lib/app/time-scale/time-scale-instance.js"),
            File("lightweightlibrary/lib/app/time-scale/time-scale-instance.js"),
        )
        val file = candidates.firstOrNull { it.isFile }
        assertNotNull("time-scale-instance.js should be readable", file)
        return file!!.readText()
    }

    private fun priceScaleBridgeSource(): String {
        val candidates = listOf(
            File("lib/app/price-scale/price-scale-creation.js"),
            File("lightweightlibrary/lib/app/price-scale/price-scale-creation.js"),
        )
        val file = candidates.firstOrNull { it.isFile }
        assertNotNull("price-scale-creation.js should be readable", file)
        return file!!.readText()
    }

    private fun callbackBufferSize(controller: WebMessageController): Int {
        val field = WebMessageController::class.java.getDeclaredField("callbackBuffer")
        field.isAccessible = true
        val buffer = field.get(controller) as Map<*, *>
        return buffer.size
    }
}

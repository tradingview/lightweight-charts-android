package com.tradingview.lightweightcharts.runtime.controller

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.tradingview.lightweightcharts.Logger
import com.tradingview.lightweightcharts.api.exception.ChartBridgeException
import com.tradingview.lightweightcharts.api.serializer.Deserializer
import com.tradingview.lightweightcharts.api.serializer.PrimitiveSerializer
import com.tradingview.lightweightcharts.api.serializer.gson.GsonProvider
import com.tradingview.lightweightcharts.runtime.messaging.BridgeFatalError
import com.tradingview.lightweightcharts.runtime.messaging.BridgeFunctionResult
import com.tradingview.lightweightcharts.runtime.messaging.BridgeMessage
import com.tradingview.lightweightcharts.runtime.messaging.BridgeSubscribeResult
import com.tradingview.lightweightcharts.runtime.messaging.Data
import com.tradingview.lightweightcharts.runtime.messaging.LogLevel
import com.tradingview.lightweightcharts.runtime.messaging.MessageType
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WebMessageControllerErrorTest {

    private val controller = WebMessageController()
    private val errors = mutableListOf<ChartBridgeException>()
    private var originalLogLevel = Logger.level

    @Before
    fun setUp() {
        originalLogLevel = Logger.level
        Logger.level = LogLevel.NONE
    }

    @After
    fun tearDown() {
        Logger.level = originalLogLevel
    }

    @Test
    fun fatalErrorOfCallWithoutCallbackReachesListenerWithCallSite() {
        controller.errorListener = { errors.add(it) }
        val uuid = updateRemovedSeries()

        controller.onMessage(fatalError(uuid, "update"))

        val error = errors.single() as ChartBridgeException.JsFatalError
        assertEquals("update", error.functionName)
        assertEquals("Bridge call 'update' failed: $JS_MESSAGE", error.message)
        assertEquals("jsCode", error.stackTrace.first().className)
        assertEquals("SeriesInstanceService._findSeries", error.stackTrace.first().methodName)
        assertEquals(1175, error.stackTrace.first().lineNumber)
        assertCallSiteIncludes(error.cause!!.stackTrace, "updateRemovedSeries")
    }

    @Test
    fun fatalErrorIsThrownWhenNoListenerIsSet() {
        val uuid = updateRemovedSeries()

        // still an IllegalStateException, the type thrown by earlier versions
        val error = assertThrows(IllegalStateException::class.java) {
            controller.onMessage(fatalError(uuid, "update"))
        }

        assertTrue(error is ChartBridgeException.JsFatalError)
        assertCallSiteIncludes(error.cause!!.stackTrace, "updateRemovedSeries")
    }

    @Test
    fun fatalErrorOfCallWithCallbackDropsCallbackAndKeepsCallSite() {
        controller.errorListener = { errors.add(it) }
        var callbackInvoked = false
        val uuid = requestOptions { callbackInvoked = true }

        controller.onMessage(fatalError(uuid, "seriesOptions"))
        controller.onMessage(functionResult(uuid, JsonPrimitive("late")))

        assertFalse(callbackInvoked)
        assertCallSiteIncludes(errors.single().cause!!.stackTrace, "requestOptions")
    }

    @Test
    fun fatalErrorWithoutUuidOrMessageIsReported() {
        controller.errorListener = { errors.add(it) }

        controller.onMessage(parse("""{"messageType":"Message::FatalError","data":{}}"""))

        val error = errors.single() as ChartBridgeException.JsFatalError
        assertNull(error.functionName)
        assertNull(error.cause)
        assertEquals("Unknown JavaScript error", error.message)
    }

    @Test
    fun onlyRecentCallSitesOfCallsWithoutCallbackAreKept() {
        controller.errorListener = { errors.add(it) }
        val uuids = List(1_000) { updateRemovedSeries() }

        controller.onMessage(fatalError(uuids.first(), "update"))
        controller.onMessage(fatalError(uuids.last(), "update"))

        assertEquals(127, notificationCallSitesSize())
        // evicted call site: the error still names the failed call
        assertEquals("update", errors[0].functionName)
        assertNull(errors[0].cause)
        assertNotNull(errors[1].cause)
    }

    @Test
    fun deserializationFailureReachesListenerAndSkipsCallback() {
        controller.errorListener = { errors.add(it) }
        val failure = IllegalStateException("unexpected shape")
        var callbackInvoked = false
        val uuid = controller.callFunction(
            "seriesOptions",
            callback = { _: String -> callbackInvoked = true },
            deserializer = throwingDeserializer(failure),
        )

        controller.onMessage(functionResult(uuid, JsonPrimitive("value")))

        val error = errors.single() as ChartBridgeException.ResultDeserializationError
        assertFalse(callbackInvoked)
        assertEquals("seriesOptions", error.functionName)
        assertSame(failure, error.cause)
        assertCallSiteIncludes(error.stackTrace, "deserializationFailureReachesListenerAndSkipsCallback")
    }

    @Test
    fun subscriptionDeserializationFailureReachesListener() {
        controller.errorListener = { errors.add(it) }
        val failure = IllegalStateException("unexpected shape")
        controller.callSubscribe(
            "subscribeCrosshairMove",
            callback = { _: String -> },
            deserializer = throwingDeserializer(failure),
        )
        val uuid = callbackBufferKeys().single()

        controller.onMessage(
            BridgeSubscribeResult(
                BridgeMessage(
                    MessageType.SUBSCRIBE_RESULT,
                    Data(uuid = uuid, fn = "subscribeCrosshairMove", result = JsonPrimitive("value")),
                )
            )
        )

        assertSame(failure, errors.single().cause)
    }

    @Test
    fun deserializationFailureIsThrownWhenNoListenerIsSet() {
        val uuid = controller.callFunction(
            "seriesOptions",
            callback = { _: String -> },
            deserializer = throwingDeserializer(IllegalStateException("unexpected shape")),
        )

        assertThrows(ChartBridgeException.ResultDeserializationError::class.java) {
            controller.onMessage(functionResult(uuid, JsonPrimitive("value")))
        }
    }

    @Test
    fun callbackExceptionPropagatesWithoutReachingListener() {
        controller.errorListener = { errors.add(it) }
        val failure = IllegalArgumentException("caller bug")
        val uuid = requestOptions { throw failure }

        val thrown = assertThrows(IllegalArgumentException::class.java) {
            controller.onMessage(functionResult(uuid, JsonPrimitive("value")))
        }

        assertSame(failure, thrown)
        assertTrue(errors.isEmpty())
    }

    @Test
    fun channelErrorsReachListener() {
        controller.errorListener = { errors.add(it) }
        val error = ChartBridgeException.MalformedMessageError(IllegalStateException("bad json"))

        controller.onError(error)

        assertSame(error, errors.single())
    }

    private fun updateRemovedSeries(): String {
        return controller.callFunction("update", mapOf("seriesId" to "removed"))
    }

    private fun requestOptions(callback: (String) -> Unit): String {
        return controller.callFunction(
            "seriesOptions",
            callback = callback,
            deserializer = PrimitiveSerializer.StringDeserializer,
        )
    }

    private fun assertCallSiteIncludes(stackTrace: Array<StackTraceElement>, methodName: String) {
        assertTrue(
            "call site should include $methodName",
            stackTrace.any { it.className == javaClass.name && it.methodName == methodName }
        )
        val controllerClass = WebMessageController::class.java.name
        assertFalse(
            "call site should not include controller frames",
            stackTrace.any { it.className == controllerClass || it.className.startsWith("$controllerClass$") }
        )
    }

    private fun throwingDeserializer(failure: Exception) = object : Deserializer<String>() {
        override fun deserialize(json: JsonElement): String = throw failure
    }

    private fun fatalError(uuid: String, fn: String): BridgeFatalError {
        return BridgeFatalError(
            BridgeMessage(MessageType.FATAL_ERROR, Data(uuid = uuid, fn = fn, message = JS_STACK))
        )
    }

    private fun functionResult(uuid: String, result: JsonElement): BridgeFunctionResult {
        return BridgeFunctionResult(
            BridgeMessage(MessageType.FUNCTION_RESULT, Data(uuid = uuid, result = result))
        )
    }

    private fun parse(json: String): BridgeFatalError {
        return BridgeFatalError(GsonProvider.newInstance().fromJson(json, BridgeMessage::class.java))
    }

    private fun notificationCallSitesSize(): Int = privateMap("notificationCallSites").size

    private fun callbackBufferKeys(): Set<String> {
        @Suppress("UNCHECKED_CAST")
        return privateMap("callbackBuffer").keys as Set<String>
    }

    private fun privateMap(name: String): Map<*, *> {
        val field = WebMessageController::class.java.getDeclaredField(name)
        field.isAccessible = true
        return field.get(controller) as Map<*, *>
    }

    private companion object {
        const val JS_MESSAGE = "Error: Series with uuid:removed is not found"
        const val MAIN_JS = "file:///android_asset/com/tradingview/lightweightcharts/scripts/app/main.js"
        val JS_STACK = """
            $JS_MESSAGE
                at SeriesInstanceService._findSeries ($MAIN_JS:1175:19)
                at Object.functionRef ($MAIN_JS:1076:22)
                at FunctionManager.call ($MAIN_JS:585:16)
        """.trimIndent()
    }
}

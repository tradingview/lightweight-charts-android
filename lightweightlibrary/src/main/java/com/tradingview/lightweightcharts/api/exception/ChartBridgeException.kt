package com.tradingview.lightweightcharts.api.exception

/**
 * An error the chart reports asynchronously, after the API call that caused it has already returned.
 *
 * It is passed to the listener set with `ChartsView.setOnErrorListener`. When no listener is set,
 * it is thrown on the main thread. It extends [IllegalStateException], the type thrown in earlier versions.
 */
sealed class ChartBridgeException(
    message: String,
    cause: Throwable?
) : IllegalStateException(message, cause) {

    /**
     * Name of the bridge function that failed, or `null` if the error is not tied to a call.
     */
    abstract val functionName: String?

    /**
     * The JavaScript side failed to execute [functionName].
     *
     * The stack trace holds the JavaScript frames, and [cause] holds the native frames of the API call
     * that sent it, if they are still known.
     */
    class JsFatalError internal constructor(
        override val functionName: String?,
        jsMessage: String,
        jsStackTrace: Array<StackTraceElement>,
        callSite: Throwable?
    ) : ChartBridgeException(messageOf(functionName, jsMessage), callSite) {
        init {
            stackTrace = jsStackTrace
        }
    }

    /**
     * The result of [functionName] could not be deserialized, so its callback was not invoked.
     *
     * The stack trace holds the native frames of the API call that sent it, and [cause] holds the
     * deserialization error.
     */
    class ResultDeserializationError internal constructor(
        override val functionName: String?,
        callStackTrace: Array<StackTraceElement>,
        cause: Throwable
    ) : ChartBridgeException(messageOf(functionName, "result could not be deserialized"), cause) {
        init {
            stackTrace = callStackTrace
        }
    }

    /**
     * A message received from the JavaScript side could not be parsed. [cause] holds the parse error.
     */
    class MalformedMessageError internal constructor(
        cause: Throwable
    ) : ChartBridgeException("Malformed bridge message: ${cause.message}", cause) {
        override val functionName: String? = null
    }

    private companion object {
        fun messageOf(functionName: String?, reason: String): String {
            return if (functionName != null) "Bridge call '$functionName' failed: $reason" else reason
        }
    }
}

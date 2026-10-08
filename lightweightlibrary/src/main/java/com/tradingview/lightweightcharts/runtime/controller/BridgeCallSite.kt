package com.tradingview.lightweightcharts.runtime.controller

/**
 * Records where a bridge call was made, so an error reported for it later can point at the calling code.
 *
 * The stack is captured on construction but only decoded into [StackTraceElement]s when it is read,
 * which keeps recording cheap for calls that never fail.
 */
internal class BridgeCallSite(
    val functionName: String
) : Throwable("Bridge call '$functionName' was made here") {

    /**
     * Drops the controller's own frames, which say nothing about the caller, before this call site is reported.
     */
    fun trimToCaller(): BridgeCallSite = apply {
        stackTrace = stackTrace.filterNot { isControllerFrame(it.className) }.toTypedArray()
    }

    private fun isControllerFrame(className: String): Boolean {
        return className == CONTROLLER_CLASS_NAME || className.startsWith("$CONTROLLER_CLASS_NAME$")
    }

    private companion object {
        val CONTROLLER_CLASS_NAME: String = WebMessageController::class.java.name
    }
}

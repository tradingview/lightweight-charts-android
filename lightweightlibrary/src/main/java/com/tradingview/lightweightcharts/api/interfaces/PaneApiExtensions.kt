package com.tradingview.lightweightcharts.api.interfaces

import android.util.SizeF

data class PaneSnapshot(
    val index: Int,
    val height: Int,
    val series: List<SeriesApi>,
    val preserveEmptyPane: Boolean,
    val stretchFactor: Float,
)

fun ChartApi.paneSizes(
    vararg paneIndices: Int,
    onPaneSizesReceived: (Map<Int, SizeF>) -> Unit,
) {
    paneSizes(paneIndices.toList(), onPaneSizesReceived)
}

fun ChartApi.paneSizes(
    paneIndices: Iterable<Int>,
    onPaneSizesReceived: (Map<Int, SizeF>) -> Unit,
) {
    val indices = paneIndices.toList()
    if (indices.isEmpty()) {
        onPaneSizesReceived(emptyMap())
        return
    }

    val sizes = linkedMapOf<Int, SizeF>()
    val lock = Any()
    var remaining = indices.size

    indices.forEach { paneIndex ->
        paneSize(paneIndex) { size ->
            val result = synchronized(lock) {
                sizes[paneIndex] = size
                remaining -= 1
                if (remaining == 0) {
                    indices.associateWith { sizes.getValue(it) }
                } else {
                    null
                }
            }
            result?.let(onPaneSizesReceived)
        }
    }
}

fun PaneApi.snapshot(onSnapshotReceived: (PaneSnapshot) -> Unit) {
    val lock = Any()
    var remaining = SNAPSHOT_FIELD_COUNT
    var index: Int? = null
    var height: Int? = null
    var series: List<SeriesApi>? = null
    var preserveEmptyPane: Boolean? = null
    var stretchFactor: Float? = null

    fun complete(update: () -> Unit): PaneSnapshot? = synchronized(lock) {
        update()
        remaining -= 1
        if (remaining == 0) {
            PaneSnapshot(
                index = checkNotNull(index),
                height = checkNotNull(height),
                series = checkNotNull(series),
                preserveEmptyPane = checkNotNull(preserveEmptyPane),
                stretchFactor = checkNotNull(stretchFactor),
            )
        } else {
            null
        }
    }

    paneIndex { value ->
        complete { index = value }?.let(onSnapshotReceived)
    }
    getHeight { value ->
        complete { height = value }?.let(onSnapshotReceived)
    }
    getSeries { value ->
        complete { series = value }?.let(onSnapshotReceived)
    }
    preserveEmptyPane { value ->
        complete { preserveEmptyPane = value }?.let(onSnapshotReceived)
    }
    getStretchFactor { value ->
        complete { stretchFactor = value }?.let(onSnapshotReceived)
    }
}

private const val SNAPSHOT_FIELD_COUNT = 5

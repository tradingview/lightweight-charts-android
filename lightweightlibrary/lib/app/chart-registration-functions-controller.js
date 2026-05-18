import { createImageWatermark, createTextWatermark } from "lightweight-charts";
import PaneCache from "./pane/pane-cache";
import PriceScaleCache from "./price-scale/price-scale-cache";
import SeriesFunctionManager from "./series/series-function-manager.js";
import SeriesCache from "./series/series-cache";
import LineCache from "./series/line-cache";
import MarkersCache from "./series/markers-cache";
import SubscriptionsFunctionManager from "./subscriptions-function-manager";
import TimeScaleFunctionManager from "./time-scale/time-scale-function-manager";
import { logger } from './logger.js';
import { Locator } from "./service-locator/locator.js";
import PriceScaleFunctionManager from "./price-scale/price-scale-function-manager";
import WatermarkCache from "./watermark/watermark-cache";

export default class ChartRegistrationFunctionsController {

    constructor(chart, functionManager, pluginManager) {
        this.chart = chart
        this.functionManager = functionManager
        this.pluginManager = pluginManager
        this.cache = new Map()
    }

    registerFunctions() {
        const seriesFunctionManager = Locator.resolve(SeriesFunctionManager.name)
        seriesFunctionManager.register()

        const subscriptions = new SubscriptionsFunctionManager(
            this.chart,
            this.functionManager,
            seriesFunctionManager
        )
        subscriptions.register()

        const timeScale = Locator.resolve(TimeScaleFunctionManager.name)
        timeScale.register()
        const priceScale = Locator.resolve(PriceScaleFunctionManager.name)
        priceScale.register()
        this.paneCache = Locator.resolve(PaneCache.name)
        this.priceScaleCache = Locator.resolve(PriceScaleCache.name)
        this.seriesCache = Locator.resolve(SeriesCache.name)
        this.lineCache = Locator.resolve(LineCache.name)
        this.markersCache = Locator.resolve(MarkersCache.name)
        this.watermarkCache = Locator.resolve(WatermarkCache.name)

        this.functionManager.registerFunction("remove", (params, resolve) => {
            this.functionManager.removeSubscriptions(() => true)
            this.cache.clear()
            this.paneCache.clear()
            this.priceScaleCache.clear()
            this.seriesCache.clear()
            this.lineCache.clear()
            this.markersCache.clear()
            this.watermarkCache.clear()
            this.chart.remove()
            resolve()
        })

        this.functionManager.registerFunction("chartOptions", (params, resolve) => {
            let options = this.chart.options()

            this._restorePluginOption(options.localization, "priceFormatter")
            this._restorePluginOption(options.localization, "timeFormatter")
            this._restorePluginOption(options.localization, "tickmarksPriceFormatter")
            this._restorePluginOption(options.localization, "tickmarksPercentageFormatter")
            this._restorePluginOption(options.timeScale, "tickMarkFormatter")

            resolve(options)
        })
        this.functionManager.registerFunction("chartApplyOptions", (input, resolve) => {
            const options = input.params.options || {}
            this._applyLegacyWatermark(options)
            Promise.all([
                this._registerPluginOption(options.localization, "priceFormatter"),
                this._registerPluginOption(options.localization, "timeFormatter"),
                this._registerPluginOption(options.localization, "tickmarksPriceFormatter"),
                this._registerPluginOption(options.localization, "tickmarksPercentageFormatter"),
                this._registerPluginOption(options.timeScale, "tickMarkFormatter"),
            ]).then(() => {
                this.chart.applyOptions(options)
                logger.d('apply options')
                resolve()
            })
        })

        this.functionManager.registerFunction("takeScreenshot", (input, resolve) => {
            const mimeType = input.params.mimeType
            let chartScreenshot = this.chart.takeScreenshot(
                input.params.addTopLayer || false,
                input.params.includeCrosshair || false
            )
            resolve(chartScreenshot.toDataURL(mimeType, 1.0))
        })

        this.functionManager.registerFunction("resize", (input, resolve) => {
            this.chart.resize(input.params.width, input.params.height, input.params.forceRepaint || false)
            resolve()
        })

        this.functionManager.registerFunction("setCrosshairPosition", (input, resolve) => {
            const series = this.seriesCache.get(input.params.seriesId)
            if (!series) {
                this.functionManager.throwFatalError(new Error(`Series with uuid:${input.params.seriesId} is not found`), input)
                return
            }
            this.chart.setCrosshairPosition(input.params.price, input.params.horizontalPosition, series)
            resolve()
        })

        this.functionManager.registerFunction("clearCrosshairPosition", (input, resolve) => {
            this.chart.clearCrosshairPosition()
            resolve()
        })

        this.functionManager.registerFunction("paneSize", (input, resolve) => {
            resolve(this.chart.paneSize(input.params.paneIndex || 0))
        });

        this.functionManager.registerFunction("autoSizeActive", (input, resolve) => {
            resolve(this.chart.autoSizeActive())
        });

        this._registerPaneFunctions()
        this._registerWatermarkFunctions()
    }

    _registerPaneFunctions() {
        this.functionManager.registerFunction("getPanes", (input, resolve) => {
            resolve(this.chart.panes().map((pane) => this._cachePane(pane)))
        })

        this.functionManager.registerFunction("addPane", (input, resolve) => {
            const pane = this.chart.addPane(input.params.preserveEmptyPane || false)
            this.paneCache.set(input.uuid, pane)
            resolve(input.uuid)
        })

        this.functionManager.registerFunction("removePane", (input, resolve) => {
            this.chart.removePane(input.params.paneIndex)
            this._syncPanes()
            resolve()
        })

        this.functionManager.registerFunction("swapPanes", (input, resolve) => {
            this.chart.swapPanes(input.params.first, input.params.second)
            this._syncPanes()
            resolve()
        })

        this.functionManager.registerFunction("panePriceScale", (input, resolve) => {
            const pane = this._paneById(input.params.paneId, input)
            if (!pane) {
                return
            }
            const scale = pane.priceScale(input.params.priceScaleId)
            this.priceScaleCache.set(input.uuid, scale)
            resolve(input.uuid)
        })

        this.functionManager.registerFunction("paneGetHeight", (input, resolve) => {
            const pane = this._paneById(input.params.paneId, input)
            if (pane) {
                resolve(pane.getHeight())
            }
        })

        this.functionManager.registerFunction("paneSetHeight", (input, resolve) => {
            const pane = this._paneById(input.params.paneId, input)
            if (pane) {
                pane.setHeight(input.params.height)
                resolve()
            }
        })

        this.functionManager.registerFunction("paneMoveTo", (input, resolve) => {
            const pane = this._paneById(input.params.paneId, input)
            if (pane) {
                pane.moveTo(input.params.paneIndex)
                this._syncPanes()
                resolve()
            }
        })

        this.functionManager.registerFunction("paneIndex", (input, resolve) => {
            const pane = this._paneById(input.params.paneId, input)
            if (pane) {
                resolve(pane.paneIndex())
            }
        })

        this.functionManager.registerFunction("paneGetSeries", (input, resolve) => {
            const pane = this._paneById(input.params.paneId, input)
            if (pane) {
                resolve(pane.getSeries().map((series) => ({
                    uuid: this.seriesCache.getKeyOfSeries(series),
                    seriesType: series.seriesType(),
                })))
            }
        })

        this.functionManager.registerFunction("paneSetPreserveEmpty", (input, resolve) => {
            const pane = this._paneById(input.params.paneId, input)
            if (pane) {
                pane.setPreserveEmptyPane(input.params.preserve)
                resolve()
            }
        })

        this.functionManager.registerFunction("panePreserveEmpty", (input, resolve) => {
            const pane = this._paneById(input.params.paneId, input)
            if (pane) {
                resolve(pane.preserveEmptyPane())
            }
        })

        this.functionManager.registerFunction("paneGetStretchFactor", (input, resolve) => {
            const pane = this._paneById(input.params.paneId, input)
            if (pane) {
                resolve(pane.getStretchFactor())
            }
        })

        this.functionManager.registerFunction("paneSetStretchFactor", (input, resolve) => {
            const pane = this._paneById(input.params.paneId, input)
            if (pane) {
                pane.setStretchFactor(input.params.stretchFactor)
                resolve()
            }
        })
    }

    _registerWatermarkFunctions() {
        this.functionManager.registerFunction("createTextWatermark", (input, resolve) => {
            const pane = this._paneByIndex(input.params.paneIndex || 0)
            const watermark = createTextWatermark(pane, input.params.options || {})
            this.watermarkCache.set(input.uuid, watermark)
            resolve(input.uuid)
        })

        this.functionManager.registerFunction("createImageWatermark", (input, resolve) => {
            const pane = this._paneByIndex(input.params.paneIndex || 0)
            const watermark = createImageWatermark(pane, input.params.imageUrl, input.params.options || {})
            this.watermarkCache.set(input.uuid, watermark)
            resolve(input.uuid)
        })

        this.functionManager.registerFunction("watermarkApplyOptions", (input, resolve) => {
            const watermark = this.watermarkCache.get(input.params.watermarkId)
            if (!watermark) {
                this.functionManager.throwFatalError(new Error(`Watermark with uuid:${input.params.watermarkId} is not found`), input)
                return
            }
            watermark.applyOptions(input.params.options || {})
            resolve()
        })

        this.functionManager.registerFunction("watermarkDetach", (input, resolve) => {
            const watermark = this.watermarkCache.get(input.params.watermarkId)
            if (!watermark) {
                this.functionManager.throwFatalError(new Error(`Watermark with uuid:${input.params.watermarkId} is not found`), input)
                return
            }
            watermark.detach()
            this.watermarkCache.delete(input.params.watermarkId)
            resolve()
        })
    }

    _registerPluginOption(options, key) {
        return new Promise((resolve) => {
            if (!options || !options[key]) {
                resolve()
                return
            }

            const plugin = options[key]
            this.pluginManager.register(plugin, (fun) => {
                options[key] = fun
                logger.d(`plugin ${key} registered`)
                resolve()
            })
        })
    }

    _restorePluginOption(options, key) {
        if (options && options[key]) {
            options[key] = this.pluginManager.getPlugin(options[key])
        }
    }

    _applyLegacyWatermark(options) {
        const legacy = options.watermark
        if (!legacy) {
            return
        }

        delete options.watermark
        const id = "__legacy_watermark__"
        const existing = this.watermarkCache.get(id)
        if (legacy.visible === false) {
            if (existing) {
                existing.detach()
                this.watermarkCache.delete(id)
            }
            return
        }

        const textOptions = {
            horzAlign: legacy.horzAlign,
            vertAlign: legacy.vertAlign,
            lines: [
                {
                    text: legacy.text || "",
                    color: legacy.color,
                    fontSize: legacy.fontSize,
                    fontStyle: legacy.fontStyle,
                    fontFamily: legacy.fontFamily,
                }
            ]
        }

        if (existing) {
            existing.applyOptions(textOptions)
        } else {
            this.watermarkCache.set(id, createTextWatermark(this._paneByIndex(0), textOptions))
        }
    }

    _syncPanes() {
        const panes = this.chart.panes()
        for (let [id, pane] of this.paneCache.entries()) {
            if (!panes.some((currentPane) => Object.is(currentPane, pane))) {
                this.paneCache.delete(id)
            }
        }
        panes.forEach((pane) => this._cachePane(pane))
    }

    _cachePane(pane) {
        try {
            return this.paneCache.getKeyOfPane(pane)
        } catch (e) {
            const id = `pane:${Date.now()}:${this.paneCache.size}`
            this.paneCache.set(id, pane)
            return id
        }
    }

    _paneByIndex(index) {
        const pane = this.chart.panes()[index]
        if (!pane) {
            throw new Error(`Pane index ${index} is not found`)
        }
        this._cachePane(pane)
        return pane
    }

    _paneById(paneId, input) {
        const pane = this.paneCache.get(paneId)
        if (!pane) {
            this.functionManager.throwFatalError(new Error(`Pane with uuid:${paneId} is not found`), input)
            return undefined
        }
        return pane
    }
}

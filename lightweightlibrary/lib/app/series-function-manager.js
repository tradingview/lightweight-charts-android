export default class SeriesFunctionManager {

    constructor(chart, functionManager, pluginManager) {
        this.chart = chart
        this.functionManager = functionManager
        this.pluginManager = pluginManager
        this.cache = new Map()
    }

    _registerAutoscaleInfoProvider(params, onSuccess) {
        new Promise((resolve) => {
            if (!params.options.autoscaleInfoProvider) {
                resolve()
                return
            }

            const plugin = params.options.autoscaleInfoProvider
            this.pluginManager.register(plugin, (fun) => {
                params.options.autoscaleInfoProvider = fun
                resolve()
            })
        }).then(() => {
            onSuccess(params)
        })
    }

    _registerPriceFormatter(params, onSuccess) {
        new Promise((resolve) => {
            if (!params.options.priceFormat || !params.options.priceFormat.formatter) {
                resolve()
                return
            }

            const plugin = params.options.priceFormat.formatter
            this.pluginManager.register(plugin, (fun) => {
                params.options.priceFormat.formatter = fun
                resolve()
            })
        }).then(() => {
            onSuccess(params)
        })
    }

    _registerPlugins(rawParams, onSuccess) {
        this._registerAutoscaleInfoProvider(rawParams, (autoscaleParams) => {
            this._registerPriceFormatter(autoscaleParams, (params) => {
                onSuccess(params)
            })
        })
    }

    _registerSeriesFunctions() {
        this.functionManager.registerFunction("addAreaSeries", (input, resolve) => {
            this._registerPlugins(input.params, (params) => {
                this.addSeries(input.uuid, this.chart.addAreaSeries(params.options))
                resolve(input.uuid)
            })
        })
        
        this.functionManager.registerFunction("addLineSeries", (input, resolve) => {
            this._registerPlugins(input.params, (params) => {
                this.addSeries(input.uuid, this.chart.addLineSeries(params.options))
                resolve(input.uuid)
            })
        })
        
        this.functionManager.registerFunction("addBarSeries", (input, resolve) => {
            this._registerPlugins(input.params, (params) => {
                this.addSeries(input.uuid, this.chart.addBarSeries(params.options))
                resolve(input.uuid)
            })
        })
        
        this.functionManager.registerFunction("addCandlestickSeries", (input, resolve) => {
            this._registerPlugins(input.params, (params) => {
                this.addSeries(input.uuid, this.chart.addCandlestickSeries(params.options))
                resolve(input.uuid)
            })
        })
        
        this.functionManager.registerFunction("addHistogramSeries", (input, resolve) => {
            this._registerPlugins(input.params, (params) => {
                this.addSeries(input.uuid, this.chart.addHistogramSeries(params.options))
                resolve(input.uuid)
            })
        })
    }

    register() {
        this._registerSeriesFunctions()
        
        this.functionManager.registerFunction("setSeries", (input) => {
            this.findSeries(input, (series) => {
                series.setData(input.params.data)
            })
        })
        this.functionManager.registerFunction("removeSeries", (input, resolve) => {
            this.findSeries(input, (series) => {
                this.cache.delete(input.params.seriesId)
                this.chart.removeSeries(series)
                resolve()
            })
        })
        this.functionManager.registerFunction("priceToCoordinate", (input, resolve) => {
            this.findSeries(input, (series) => {
                resolve(series.priceToCoordinate(input.params.price))
            })
        })
        this.functionManager.registerFunction("coordinateToPrice", (input) => {
            this.findSeries(input, (series) => {
                series.coordinateToPrice(input.params.price)
            })
        })
        this.functionManager.registerFunction("options", (input, resolve) => {
            this.findSeries(input, (series) => {
                let options = series.options()
                if (options.priceFormat.formatter !== undefined) {
                    const fun = options.priceFormat.formatter
                    options.priceFormat.formatter = this.pluginManager.getPlugin(fun)
                }
                if (options.autoscaleInfoProvider !== undefined) {
                    const fun = options.autoscaleInfoProvider
                    options.autoscaleInfoProvider = this.pluginManager.getPlugin(fun)
                }
                resolve(options)
            })
        })
        this.functionManager.registerFunction("applyOptions", (input, resolve) => {
            this.findSeries(input, (series) => {
                this._registerPriceFormatter(input.params, (params) => {
                    series.applyOptions(params.options)
                    resolve()
                })
            })
        })
        this.functionManager.registerFunction("setMarkers", (input, resolve) => {
            this.findSeries(input, (series) => {
                series.setMarkers(input.params.data)
            })
        })
        this.functionManager.registerFunction("createPriceLine", (input, resolve) => {
            this.findSeries(input, (series) => {
                this.series = series
                this.line = this.series.createPriceLine(input.params.options)
                this.cache.set(input.uuid, this.line)
            })
        })
        this.functionManager.registerFunction("removePriceLine", (input, resolve) => {
            this.findSeries(input, (series) => {
                this.findLine(input, (line) => {
                    this.cache.delete(input.params.lineId)
                    series.removePriceLine(line)
                })
            })
        })
        this.functionManager.registerFunction("update", (input, resolve) => {
            this.findSeries(input, (series) => {
                series.update(input.params.bar)
            })
        })
        this.functionManager.registerFunction("priceLineOptions", (input, resolve) => {
            this.findLine(input, (line) => {
                resolve(line.options())
            })
        })
        this.functionManager.registerFunction("priceLineApplyOptions", (input, resolve) => {
            this.findLine(input, (line) => {
                line.applyOptions(input.params.options)
            })
        })
        this.functionManager.registerFunction("formatPrice", (input, resolve) => {
            let formatter = this.cache.get(input.params.formatterId)
            if (formatter === undefined) {
                this.functionManager.throwFatalError(`Formatter with uuid:${input.uuid} is not found`, input)
            } else {
                resolve(formatter.format(input.params.price))
            }
        })
    }

    findSeries(input, callback) {
        let series = this.cache.get(input.params.seriesId)
        if (series === undefined) {
            this.functionManager.throwFatalError(`${seriesName} with uuid:${input.uuid} is not found`, input)
        } else {
            callback(series)
        }
    }

    getSeriesId(seriesObject, input) {
        for (let [key, value] of this.cache.entries()) {
            if (Object.is(value, seriesObject)) {
                return key
            }
        }

        this.functionManager.throwFatalError(`Series id is not found`, input)

        return undefined
    }

    findLine(input, callback) {
        let line = this.cache.get(input.params.lineId)
        if (line === undefined) {
            this.functionManager.throwFatalError(`PriceLine with uuid:${input.uuid} is not found`, input)
        } else {
            callback(line)
        }
    }

    addSeries(uuid, series) {
        this.cache.set(uuid, series)
    }
}

export default class SeriesFunctionManager {

    constructor(chart, functionManager, pluginManager) {
        this.chart = chart
        this.functionManager = functionManager
        this.pluginManager = pluginManager
        this.cache = new Map()
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

    _registerSeriesFunctions() {
        this.functionManager.registerFunction("addAreaSeries", (rawParams, resolve) => {
            this._registerPriceFormatter(rawParams, (params) => {
                this.addSeries(params.uuid, this.chart.addAreaSeries(params.options))
                resolve(params.uuid)
            })
        })
        
        this.functionManager.registerFunction("addLineSeries", (rawParams, resolve) => {
            this._registerPriceFormatter(rawParams, (params) => {
                this.addSeries(params.uuid, this.chart.addLineSeries(params.options))
                resolve(params.uuid)
            })
        })
        
        this.functionManager.registerFunction("addBarSeries", (rawParams, resolve) => {
            this._registerPriceFormatter(rawParams, (params) => {
                this.addSeries(params.uuid, this.chart.addBarSeries(params.options))
                resolve(params.uuid)
            })
        })
        
        this.functionManager.registerFunction("addCandlestickSeries", (rawParams, resolve) => {
            this._registerPriceFormatter(rawParams, (params) => {
                this.addSeries(params.uuid, this.chart.addCandlestickSeries(params.options))
                resolve(params.uuid)
            })
        })
        
        this.functionManager.registerFunction("addHistogramSeries", (rawParams, resolve) => {
            this._registerPriceFormatter(rawParams, (params) => {
                this.addSeries(params.uuid, this.chart.addHistogramSeries(params.options))
                resolve(params.uuid)
            })
        })
    }

    register() {
        this._registerSeriesFunctions()
        
        this.functionManager.registerFunction("setSeries", (params) => {
            this.findSeries(params, (series) => {
                series.setData(params.data)
            })
        })
        this.functionManager.registerFunction("removeSeries", (params, resolve) => {
            console.log('removeSeries')
            this.findSeries(params, (series) => {
                this.cache.delete(params.seriesId)
                this.chart.removeSeries(series)
                resolve()
            })
        })
        this.functionManager.registerFunction("priceToCoordinate", (params, resolve) => {
            this.findSeries(params, (series) => {
                resolve(series.priceToCoordinate(params.price))
            })
        })
        this.functionManager.registerFunction("coordinateToPrice", (params) => {
            this.findSeries(params, (series) => {
                series.coordinateToPrice(params.price)
            })
        })
        this.functionManager.registerFunction("options", (params, resolve) => {
            this.findSeries(params, (series) => {
                let options = series.options()
                if (options.priceFormat.formatter !== undefined) {
                    const fun = options.priceFormat.formatter
                    options.priceFormat.formatter = this.pluginManager.getPlugin(fun)
                }
                resolve(options)
            })
        })
        this.functionManager.registerFunction("applyOptions", (rawParams, resolve) => {
            this.findSeries(rawParams, (series) => {
                this._registerPriceFormatter(rawParams, (params) => {
                    series.applyOptions(params.options)
                    resolve()
                })
            })
        })
        this.functionManager.registerFunction("setMarkers", (params, resolve) => {
            this.findSeries(params, (series) => {
                series.setMarkers(params.data)
            })
        })
        this.functionManager.registerFunction("createPriceLine", (params, resolve) => {
            this.findSeries(params, (series) => {
                this.series = series
                this.line = this.series.createPriceLine(params.options)
                this.cache.set(params.uuid, this.line)
            })
        })
        this.functionManager.registerFunction("removePriceLine", (params, resolve) => {
            this.findSeries(params, (series) => {
                this.findLine(params, (line) => {
                    this.cache.delete(params.lineId)
                    series.removePriceLine(line)
                })
            })
        })
        this.functionManager.registerFunction("update", (params, resolve) => {
            this.findSeries(params, (series) => {
                series.update(params.bar)
            })
        })
        this.functionManager.registerFunction("priceLineOptions", (params, resolve) => {
            this.findLine(params, (line) => {
                resolve(line.options())
            })
        })
        this.functionManager.registerFunction("priceLineApplyOptions", (params, resolve) => {
            this.findLine(params, (line) => {
                line.applyOptions(params.options)
            })
        })
        this.functionManager.registerFunction("formatPrice", (params, resolve) => {
            let formatter = this.cache.get(params.formatterId)
            if (formatter === undefined) {
                this.functionManager.throwFatalError(`Formatter with uuid:${params.uuid} is not found`, params)
            } else {
                resolve(formatter.format(params.price))
            }
        })

        //TODO: delete
        this.functionManager.registerFunction("priceFormatter", (params, resolve) => {
            this.findSeries(params, (series) => {
                this.cache.set(params.uuid, series.priceFormatter())
            })
        })
    }

    findSeries(params, callback) {
        let series = this.cache.get(params.seriesId)
        if (series === undefined) {
            this.functionManager.throwFatalError(`${seriesName} with uuid:${params.uuid} is not found`, params)
        } else {
            callback(series)
        }
    }

    getSeriesId(seriesObject, params) {
        console.debug('getSeriesId(seriesObject)', seriesObject)

        for (let [key, value] of this.cache.entries()) {
            if (Object.is(value, seriesObject)) {
                return key
            }
        }

        this.functionManager.throwFatalError(`Series id is not found`, params)

        return undefined
    }

    findLine(params, callback) {
        let line = this.cache.get(params.lineId)
        if (line === undefined) {
            this.functionManager.throwFatalError(`PriceLine with uuid:${params.uuid} is not found`, params)
        } else {
            callback(line)
        }
    }

    addSeries(uuid, series) {
        this.cache.set(uuid, series)
    }
}

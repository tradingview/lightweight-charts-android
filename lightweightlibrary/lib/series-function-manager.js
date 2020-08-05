export default class SeriesFunctionManager {

    constructor(chart, functionManager) {
        this.chart = chart
        this.functionManager = functionManager
        this.cache = new Map()
    }

    register() {
        this.functionManager.registerFunction("addLineSeries", (params, resolve) => {
            this.addSeries(params.uuid, this.chart.addLineSeries(params.options))
        })
        this.functionManager.registerFunction("addAreaSeries", (params, resolve) => {
            this.addSeries(params.uuid, this.chart.addAreaSeries(params.options))
        })
        this.functionManager.registerFunction("addBarSeries", (params, resolve) => {
            this.addSeries(params.uuid, this.chart.addBarSeries(params.options))
        })
        this.functionManager.registerFunction("addCandlestickSeries", (params, resolve) => {
            this.addSeries(params.uuid, this.chart.addCandlestickSeries(params.options))
        })
        this.functionManager.registerFunction("addHistogramSeries", (params, resolve) => {
            this.addSeries(params.uuid, this.chart.addHistogramSeries(params.options))
        })
        this.functionManager.registerFunction("setSeries", (params, resolve) => {
            this.findSeries(params, (series) => {
                series.setData(params.data)
            })
        })
        this.functionManager.registerFunction("removeSeries", (params, resolve) => {
            this.findSeries(params, (series) => {
                this.cache.delete(params.seriesId)
                this.chart.removeSeries(series)
            })
        })
        this.functionManager.registerFunction("priceToCoordinate", (params, resolve) => {
            this.findSeries(params, (series) => {
                resolve(series.priceToCoordinate(params.price))
            })
        })
        this.functionManager.registerFunction("coordinateToPrice", (params, resolve) => {
            this.findSeries(params, (series) => {
                series.coordinateToPrice(params.price)
            })
        })
        this.functionManager.registerFunction("options", (params, resolve) => {
            this.findSeries(params, (series) => {
                let options = series.options()
                if (options.priceFormat.formatter !== undefined) {
                    options.priceFormat.formatter = options.priceFormat.formatter.toString()
                }
                resolve(options)
            })
        })
        this.functionManager.registerFunction("applyOptions", (params, resolve) => {
            this.findSeries(params, (series) => {
                if (params.options.priceFormat.formatter !== undefined) {
                    let fn = new Function("return " + params.options.priceFormat.formatter)()
                    params.options.priceFormat.formatter = fn
                }
                series.applyOptions(params.options)
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

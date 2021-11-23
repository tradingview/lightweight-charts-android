import { logger } from '../logger.js'
import SeriesCreationService from './series-creation.js'

export default class SeriesFunctionManager {

    constructor(chart, functionManager, pluginManager) {
        this.chart = chart;
        this.functionManager = functionManager;
        this.pluginManager = pluginManager;
        this.cache = new Map();
        this.seriesCreationService = new SeriesCreationService(chart, this.cache, functionManager, pluginManager);
    }

    register() {
        this.seriesCreationService.register();
        
        this.functionManager.registerFunction("setSeries", (input) => {
            this._findSeries(input, (series) => {
                series.setData(input.params.data)
            })
        })
        this.functionManager.registerFunction("removeSeries", (input, resolve) => {
            this._findSeries(input, (series) => {
                this.cache.delete(input.params.seriesId)
                this.chart.removeSeries(series)
                resolve()
            })
        })
        this.functionManager.registerFunction("priceToCoordinate", (input, resolve) => {
            this._findSeries(input, (series) => {
                resolve(series.priceToCoordinate(input.params.price))
            })
        })
        this.functionManager.registerFunction("coordinateToPrice", (input, resolve) => {
            this._findSeries(input, (series) => {
                resolve(series.coordinateToPrice(input.params.coordinate))
            })
        })
        this.functionManager.registerFunction("options", (input, resolve) => {
            this._findSeries(input, (series) => {
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
        this.functionManager.registerFunction("seriesType", (input, resolve) => {
            this._findSeries(input, (series) => {
                resolve(series.seriesType())
            })
        })
        this.functionManager.registerFunction("applyOptions", (input, resolve) => {
            this._findSeries(input, (series) => {
                this._registerPriceFormatter(input.params, (params) => {
                    series.applyOptions(params.options)
                    resolve()
                })
            })
        })
        this.functionManager.registerFunction("setMarkers", (input, resolve) => {
            this._findSeries(input, (series) => {
                series.setMarkers(input.params.data)
            })
        })
        this.functionManager.registerFunction("createPriceLine", (input, resolve) => {
            this._findSeries(input, (series) => {
                this.series = series
                this.line = this.series.createPriceLine(input.params.options)
                this.cache.set(input.uuid, this.line)
            })
        })
        this.functionManager.registerFunction("removePriceLine", (input, resolve) => {
            this._findSeries(input, (series) => {
                this._findLine(input, (line) => {
                    this.cache.delete(input.params.lineId)
                    series.removePriceLine(line)
                })
            })
        })
        this.functionManager.registerFunction("update", (input, resolve) => {
            this._findSeries(input, (series) => {
                series.update(input.params.bar)
            })
        })
        this.functionManager.registerFunction("priceLineOptions", (input, resolve) => {
            this._findLine(input, (line) => {
                resolve(line.options())
            })
        })
        this.functionManager.registerFunction("priceLineApplyOptions", (input, resolve) => {
            this._findLine(input, (line) => {
                line.applyOptions(input.params.options)
            })
        })
        this.functionManager.registerFunction("formatPrice", (input, resolve) => {
            let formatter = this.cache.get(input.params.formatterId)
            if (formatter === undefined) {
                this.functionManager.throwFatalError(new Error(`Formatter with uuid:${input.uuid} is not found`), input)
            } else {
                resolve(formatter.format(input.params.price))
            }
        })
    }

    _findSeries(input, callback) {
        let series = this.cache.get(input.params.seriesId)
        if (series === undefined) {
            this.functionManager.throwFatalError(new Error(`${seriesName} with uuid:${input.uuid} is not found`), input)
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

        this.functionManager.throwFatalError(new Error(`Series id is not found`), input)

        return undefined
    }

    _findLine(input, callback) {
        let line = this.cache.get(input.params.lineId)
        if (line === undefined) {
            this.functionManager.throwFatalError(new Error(`PriceLine with uuid:${input.uuid} is not found`), input)
        } else {
            callback(line)
        }
    }
}

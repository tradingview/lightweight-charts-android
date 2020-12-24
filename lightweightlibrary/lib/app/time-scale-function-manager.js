export default class TimeScaleFunctionManager {

    constructor(chart, functionManager, pluginManager) {
        this.chart = chart
        this.functionManager = functionManager
        this.pluginManager = pluginManager
    }

    _timeScale() {
        return this.chart.timeScale()
    }
    
    _registerTickMarkFormatter(params, onSuccess) {
        new Promise((resolve) => {
            if (!params.options.tickMarkFormatter) {
                resolve()
                return
            }

            const plugin = params.options.tickMarkFormatter
            this.pluginManager.register(plugin, (fun) => {
                params.options.tickMarkFormatter = fun
                resolve()
            })
        }).then(() => {
            onSuccess(params)
        })
    }

    register() {
        this.functionManager.registerFunction("scrollPosition", (input, resolve) => {
            resolve(this._timeScale().scrollPosition())
        })
        this.functionManager.registerFunction("scrollToPosition", (input, resolve) => {
            this._timeScale().scrollToPosition(input.params.position, input.params.animated)
        })
        this.functionManager.registerFunction("timeScaleOptions", (input, resolve) => {
            const options = this._timeScale().options()
            if (options.tickMarkFormatter) {
                const fun = options.tickMarkFormatter
                options.tickMarkFormatter = this.pluginManager.getPlugin(fun)
            }
            resolve(options)
        })
        this.functionManager.registerFunction("timeScaleApplyOptions", (input, resolve) => {
            this._registerTickMarkFormatter(input.params, (params) => {
                this._timeScale().applyOptions(params.options)
                resolve()
            })
        })
        this.functionManager.registerFunction("scrollToRealTime", (input, resolve) => {
            this._timeScale().scrollToRealTime()
        })
        this.functionManager.registerFunction("getVisibleRange", (input, resolve) => {
            resolve(this._timeScale().getVisibleRange())
        })
        this.functionManager.registerFunction("setVisibleRange", (input, resolve) => {
            this._timeScale().setVisibleRange(input.params.range)
        })
        this.functionManager.registerFunction("resetTimeScale", (input, resolve) => {
            this._timeScale().resetTimeScale()
        })
        this.functionManager.registerFunction("fitContent", (input, resolve) => {
            this._timeScale().fitContent()
        })
        this.functionManager.registerFunction("timeToCoordinate", (input, resolve) => {
            resolve(this._timeScale().timeToCoordinate(input.params.time))
        })
        this.functionManager.registerFunction("coordinateToTime", (input, resolve) => {
            console.log(this._timeScale().coordinateToTime(input.params.x))
            resolve(this._timeScale().coordinateToTime(input.params.x))
        })
        this.functionManager.registerFunction("logicalToCoordinate", (input, resolve) => {
            resolve(this._timeScale().logicalToCoordinate(input.params.logical))
        })
        this.functionManager.registerFunction("coordinateToLogical", (input, resolve) => {
            resolve(this._timeScale().coordinateToLogical(input.params.x))
        })
        this.functionManager.registerSubscription(
            "subscribeVisibleTimeRangeChange",
            (input, callback) => {
                try {
                    this._timeScale().subscribeVisibleTimeRangeChange(callback)
                    console.debug("subscribeVisibleTimeRangeChange successful")
                } catch (error) {
                    console.error(error)
                    console.warn('subscribeVisibleTimeRangeChange has been failed')
                }
            },
            (callback) => {
                try {
                    this._timeScale().unsubscribeVisibleTimeRangeChange(callback)
                    console.debug("unsubscribeVisibleTimeRangeChange successful")
                } catch (error) {
                    console.error(error)
                    console.warn('unsubscribeVisibleTimeRangeChange has been failed')
                }
            }
        )
    }

    fetchTimeScale(input, callback) {
        let scale = this.cache.get(input.params.timeScaleId)
        if (scale === undefined) {
            this.functionManager.throwFatalError(`TimeScale with uuid:${input.uuid} is not found`, input)
        } else {
            callback(scale)
        }
    }
}

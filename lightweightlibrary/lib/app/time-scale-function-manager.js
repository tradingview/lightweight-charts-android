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
        this.functionManager.registerFunction("scrollPosition", (params, resolve) => {
            resolve(this._timeScale().scrollPosition())
        })
        this.functionManager.registerFunction("scrollToPosition", (params, resolve) => {
            this._timeScale().scrollToPosition(params.position, params.animated)
        })
        this.functionManager.registerFunction("timeScaleOptions", (params, resolve) => {
            const options = this._timeScale().options()
            if (options.tickMarkFormatter) {
                const fun = options.tickMarkFormatter
                options.tickMarkFormatter = this.pluginManager.getPlugin(fun)
            }
            resolve(options)
        })
        this.functionManager.registerFunction("timeScaleApplyOptions", (rawParams, resolve) => {
            this._registerTickMarkFormatter(rawParams, (params) => {
                this._timeScale().applyOptions(params.options)
                resolve()
            })
        })
        this.functionManager.registerFunction("scrollToRealTime", (params, resolve) => {
            this._timeScale().scrollToRealTime()
        })
        this.functionManager.registerFunction("getVisibleRange", (params, resolve) => {
            resolve(this._timeScale().getVisibleRange())
        })
        this.functionManager.registerFunction("setVisibleRange", (params, resolve) => {
            this._timeScale().setVisibleRange(params.range)
        })
        this.functionManager.registerFunction("resetTimeScale", (params, resolve) => {
            this._timeScale().resetTimeScale()
        })
        this.functionManager.registerFunction("fitContent", (params, resolve) => {
            this._timeScale().fitContent()
        })
        this.functionManager.registerSubscription(
            "subscribeVisibleTimeRangeChange",
            (params, callback) => {
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

    fetchTimeScale(params, callback) {
        let scale = this.cache.get(params.timeScaleId)
        if (scale === undefined) {
            this.functionManager.throwFatalError(`TimeScale with uuid:${params.uuid} is not found`, params)
        } else {
            callback(scale)
        }
    }
}

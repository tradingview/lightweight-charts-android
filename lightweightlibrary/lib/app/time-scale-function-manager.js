export default class TimeScaleFunctionManager {

    constructor(chart, functionManager) {
        this.chart = chart
        this.functionManager = functionManager
    }

    _timeScale() {
        return this.chart.timeScale()
    }

    register() {
        this.functionManager.registerFunction("scrollPosition", (params, resolve) => {
            resolve(this._timeScale().scrollPosition())
        })
        this.functionManager.registerFunction("scrollToPosition", (params, resolve) => {
            this._timeScale().scrollToPosition(params.position, params.animated)
        })
        this.functionManager.registerFunction("timeScaleOptions", (params, resolve) => {
            resolve(this._timeScale().options())
        })
        this.functionManager.registerFunction("timeScaleApplyOptions", (params, resolve) => {
            this._timeScale().applyOptions(params.options)
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

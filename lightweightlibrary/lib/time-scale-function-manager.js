export default class TimeScaleFunctionManager {

    constructor(chart, functionManager) {
        this.chart = chart
        this.functionManager = functionManager
        this.cache = new Map()
    }

    register() {
        this.functionManager.registerFunction("timeScale", (params, resolve) => {
            this.cache.set(params.uuid, this.chart.timeScale())
        })
        this.functionManager.registerFunction("scrollPosition", (params, resolve) => {
            this.fetchTimeScale(params, (scale) => {
                resolve(scale.scrollPosition())
            })
        })
        this.functionManager.registerFunction("scrollToPosition", (params, resolve) => {
            this.fetchTimeScale(params, (scale) => {
                scale.scrollToPosition(params.position, params.animated)
            })
        })
        this.functionManager.registerFunction("timeScaleOptions", (params, resolve) => {
            this.fetchTimeScale(params, (scale) => {
                resolve(scale.options())
            })
        })
        this.functionManager.registerFunction("timeScaleApplyOptions", (params, resolve) => {
            this.fetchTimeScale(params, (scale) => {
                scale.applyOptions(params.options)
            })
        })
        this.functionManager.registerFunction("scrollToRealTime", (params, resolve) => {
            this.fetchTimeScale(params, (scale) => {
                scale.scrollToRealTime()
            })
        })
        this.functionManager.registerFunction("getVisibleRange", (params, resolve) => {
            this.fetchTimeScale(params, (scale) => {
                resolve(scale.getVisibleRange())
            })
        })
        this.functionManager.registerFunction("setVisibleRange", (params, resolve) => {
            this.fetchTimeScale(params, (scale) => {
                scale.setVisibleRange(params.range)
            })
        })
        this.functionManager.registerFunction("resetTimeScale", (params, resolve) => {
            this.fetchTimeScale(params, (scale) => {
                scale.resetTimeScale()
            })
        })
        this.functionManager.registerFunction("fitContent", (params, resolve) => {
            this.fetchTimeScale(params, (scale) => {
                scale.fitContent()
            })
        })
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

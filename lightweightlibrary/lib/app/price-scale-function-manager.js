export default class PriceScaleFunctionManager {

    constructor(chart, functionManager) {
        this.chart = chart
        this.functionManager = functionManager
        this.cache = new Map()
    }

    register() {
        this.functionManager.registerFunction("priceScale", (params, resolve) => {
            this.cache.set(params.uuid, this.chart.priceScale(params.priceScaleId))
        })
        this.functionManager.registerFunction("priceScaleOptions", (params, resolve) => {
            const scale = this.cache.get(params.priceScaleId)
            if (scale === undefined) {
                this.functionManager.throwFatalError(`PriceScale with uuid:${params.uuid} is not found`, params)
            } else {
                resolve(scale.options())
            }
        })
        this.functionManager.registerFunction("priceScaleApplyOptions", (params, resolve) => {
            const scale = this.cache.get(params.priceScaleId)
            if (scale === undefined) {
                this.functionManager.throwFatalError(`PriceScale with uuid:${params.uuid} is not found`, params)
            } else {
                scale.applyOptions(params.options)
            }
        })
        this.functionManager.registerFunction("priceScaleWidth", (params, resolve) => {
            const scale = this.cache.get(params.priceScaleId)
            if (scale === undefined) {
                this.functionManager.throwFatalError(`PriceScale with uuid:${params.uuid} is not found`, params)
            } else {
                resolve(scale.width())
            }
        })
    }
}

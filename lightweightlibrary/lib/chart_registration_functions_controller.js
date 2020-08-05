import SeriesFunctionManager from "./series-function-manager.js";
import SubscriptionsFunctionManager from "./subscriptions-function-manager";
import PriceScaleFunctionManager from "./price-scale-function-manager";
import TimeScaleFunctionManager from "./time-scale-function-manager";

export default class ChartRegistrationFunctionsController {

  constructor(chart, functionManager) {
        this.chart = chart
        this.functionManager = functionManager
        this.cache = new Map()
    }

    registerFunctions() {

        const series = new SeriesFunctionManager(this.chart, this.functionManager)
        series.register()

        const subscriptions = new SubscriptionsFunctionManager(
            this.chart,
            this.functionManager,
            (seriesObject, params) => { return series.getSeriesId(seriesObject, params) }
        )
        subscriptions.register()

        const timeScale = new TimeScaleFunctionManager(this.chart, this.functionManager)
        timeScale.register()

        const priceScale = new PriceScaleFunctionManager(this.chart, this.functionManager)
        priceScale.register()

        this.functionManager.registerFunction("print", (params, resolve) => {
            console.log(params.text)
        })

        this.functionManager.registerFunction("resize", (params, resolve) => {
            this.chart.resize(params.width, params.height, params.forceRepaint)
        })

        this.functionManager.registerFunction("remove", (params, resolve) => {
            this.cache.clear()
            this.chart.remove()
        })
        this.functionManager.registerFunction("chartOptions", (params, resolve) => {
            let options = this.chart.options()

            if (options.localization && options.localization.priceFormatter) {
                options.localization.priceFormatter = options.localization.priceFormatter.toString()
            }

            if (options.localization && options.localization.timeFormatter) {
                options.localization.timeFormatter = options.localization.timeFormatter.toString()
            }

            resolve(options)
        })
        this.functionManager.registerFunction("chartApplyOptions", (params, resolve) => {
            if (params.options.localization && params.options.localization.priceFormatter) {
                let priceFunction = "return " + params.options.localization.priceFormatter
                params.options.localization.priceFormatter = new Function(priceFunction)()
            }

            if (params.options.localization && params.options.localization.timeFormatter) {
                let timeFunction = "return " + params.options.localization.timeFormatter
                params.options.localization.timeFormatter = new Function(timeFunction)()
            }

            this.chart.applyOptions(params.options)
        })

    }

}

import SeriesFunctionManager from "./series-function-manager.js";
import SubscriptionsFunctionManager from "./subscriptions-function-manager";
import PriceScaleFunctionManager from "./price-scale-function-manager";
import TimeScaleFunctionManager from "./time-scale-function-manager";

export default class ChartRegistrationFunctionsController {

  constructor(chart, functionManager, pluginManager) {
        this.chart = chart
        this.functionManager = functionManager
        this.pluginManager = pluginManager
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
                const fun = options.localization.priceFormatter
                options.localization.priceFormatter = this.pluginManager.getPlugin(fun)
            }

            if (options.localization && options.localization.timeFormatter) {
                const fun = options.localization.timeFormatter
                options.localization.timeFormatter = this.pluginManager.getPlugin(fun)
            }

            resolve(options)
        })
        this.functionManager.registerFunction("chartApplyOptions", (params, resolve) => {
            console.log(params)
            new Promise((resolve) => {
                if (!params.options.localization || !params.options.localization.priceFormatter) {
                    resolve()
                    return
                }

                const plugin = params.options.localization.priceFormatter
                this.pluginManager.register(plugin, (fun) => {
                    params.options.localization.priceFormatter = fun
                    console.log('plugin priceFormatter registered')
                    resolve()
                })
            }).then(() => new Promise((resolve) => {
                if (!params.options.localization || !params.options.localization.timeFormatter) {
                    resolve()
                    return
                }

                const plugin = params.options.localization.timeFormatter
                this.pluginManager.register(plugin, (fun) => {
                    params.options.localization.timeFormatter = fun
                    console.log('plugin timeFormatter registered')
                    resolve()
                })
            })).then(() => {
                this.chart.applyOptions(params.options)
                console.log('apply options')
            })
        })

    }

}

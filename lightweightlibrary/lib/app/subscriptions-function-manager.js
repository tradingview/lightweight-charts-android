export default class SubscriptionsFunctionManager {

    constructor(chart, functionManager, getSeriesId) {
        this.chart = chart
        this.functionManager = functionManager
        this.getSeriesId = getSeriesId
    }

    register() {
        this.functionManager.registerSubscription(
            "subscribeOnClick",
            (callParams, callback) => {
                try {
                    chart.subscribeClick((params) => {
                        let customSeries = []
                        params.seriesPrices.forEach((value, key, map) => {
                            customSeries.push({ id: this.getSeriesId(key, callParams), prices: value })
                        })
                        params.seriesPrices = customSeries
                        callback(params)
                    })
                    console.debug("subscribeOnChartClicked successful")
                } catch (error) {
                    console.error(error)
                    console.warn('subscribeOnClick has been failed')
                }
            },
            (callback) => {
                try {
                    chart.unsubscribeClick(callback)
                    console.debug("unsubscribeOnChartClicked successful")
                } catch (error) {
                    console.error(error)
                    console.warn('unsubscribeOnClick has been failed')
                }
            }
        )

        this.functionManager.registerSubscription(
            "subscribeCrosshairMove",
            (callParams, callback) => {
                try {
                    chart.subscribeCrosshairMove((params) => {
                        let customSeries = []
                        console.log("series prices", params.seriesPrices)
                        params.seriesPrices.forEach((value, key, map) => {
                            console.log("series prices forEach", value, key)
                            customSeries.push({ id: this.getSeriesId(key, callParams), prices: value })
                        })
                        params.seriesPrices = customSeries
                        callback(params)
                    })
                    console.debug("subscribeCrosshairMove successful")
                } catch (error) {
                    console.error(error)
                    console.warn('subscribeCrosshairMove has been failed')
                }
            },
            (callback) => {
                try {
                    chart.unsubscribeCrosshairMove(callback)
                    console.debug("unsubscribeCrosshairMove successful")
                } catch (error) {
                    console.error(error)
                    console.warn('unsubscribeCrosshairMove has been failed')
                }
            }
        )
    }

}

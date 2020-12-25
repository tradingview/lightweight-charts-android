export default class SubscriptionsFunctionManager {

    constructor(chart, functionManager, getSeriesId) {
        this.chart = chart
        this.functionManager = functionManager
        this.getSeriesId = getSeriesId
    }

    register() {
        this.functionManager.registerSubscription(
            "subscribeOnClick",
            (input, callback) => {
                try {
                    const subscription = (params) => {
                        let customSeries = []
                        params.seriesPrices.forEach((value, key, map) => {
                            customSeries.push({ id: this.getSeriesId(key, input), prices: value })
                        })
                        params.seriesPrices = customSeries
                        callback(params)
                    }
                    chart.subscribeClick()
                    console.debug("subscribeOnChartClicked successful")
                    return subscription
                } catch (error) {
                    console.error(error)
                    console.warn('subscribeOnClick has been failed')
                    return null
                }
            },
            (subscription) => {
                try {
                    chart.unsubscribeClick(subscription)
                    console.debug("unsubscribeOnChartClicked successful")
                } catch (error) {
                    console.error(error)
                    console.warn('unsubscribeOnClick has been failed')
                }
            }
        )

        this.functionManager.registerSubscription(
            "subscribeCrosshairMove",
            (input, callback) => {
                try {
                    const subscription = (params) => {
                        let customSeries = []
                        console.log("series prices", params.seriesPrices)
                        params.seriesPrices.forEach((value, key, map) => {
                            console.log("series prices forEach", value, key)
                            customSeries.push({ id: this.getSeriesId(key, input), prices: value })
                        })
                        params.seriesPrices = customSeries
                        callback(params)
                    }
                    console.log(subscription)
                    chart.subscribeCrosshairMove(subscription)
                    console.debug("subscribeCrosshairMove successful")
                    return subscription
                } catch (error) {
                    console.error(error)
                    console.warn('subscribeCrosshairMove has been failed')
                    return null
                }
            },
            (subscription) => {
                try {
                    console.log(subscription)
                    chart.unsubscribeCrosshairMove(subscription)
                    console.debug("unsubscribeCrosshairMove successful")
                } catch (error) {
                    console.error(error)
                    console.warn('unsubscribeCrosshairMove has been failed')
                }
            }
        )
    }

}

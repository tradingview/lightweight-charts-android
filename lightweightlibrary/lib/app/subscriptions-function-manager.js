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
                    chart.subscribeClick(subscription)
                    logger.debug("subscribeOnChartClicked successful")
                    return subscription
                } catch (error) {
                    logger.error('subscribeOnClick has been failed', error)
                    return null
                }
            },
            (subscription) => {
                try {
                    chart.unsubscribeClick(subscription)
                    logger.debug("unsubscribeOnChartClicked successful")
                } catch (error) {
                    logger.error('unsubscribeOnClick has been failed', error)
                }
            }
        )

        this.functionManager.registerSubscription(
            "subscribeCrosshairMove",
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
                    chart.subscribeCrosshairMove(subscription)
                    logger.debug("subscribeCrosshairMove successful")
                    return subscription
                } catch (error) {
                    logger.error('subscribeCrosshairMove has been failed', error)
                    return null
                }
            },
            (subscription) => {
                try {
                    chart.unsubscribeCrosshairMove(subscription)
                    logger.debug("unsubscribeCrosshairMove successful")
                } catch (error) {
                    logger.error('unsubscribeCrosshairMove has been failed', error)
                }
            }
        )
    }

}

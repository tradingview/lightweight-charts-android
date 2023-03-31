import {logger} from './logger.js'

export default class SubscriptionsFunctionManager {

    constructor(chart, functionManager, seriesFunctionManager) {
        this.chart = chart
        this.functionManager = functionManager
        this.seriesFunctionManager = seriesFunctionManager
    }

    register() {
        this.functionManager.registerSubscription(
            "subscribeOnClick",
            (input, callback) => {
                try {
                    const subscription = (params) => {
                        let customSeries = []
                        params.seriesPrices.forEach((value, key, map) => {
                            customSeries.push({id: this.seriesFunctionManager.getSeriesId(key, input), prices: value})
                        })
                        params.seriesPrices = customSeries
                        callback(params)
                    }
                    chart.subscribeClick(subscription)
                    logger.d("subscribeOnChartClicked successful")
                    return subscription
                } catch (error) {
                    logger.e('subscribeOnClick has been failed', error)
                    return null
                }
            },
            (subscription) => {
                try {
                    chart.unsubscribeClick(subscription)
                    logger.d("unsubscribeOnChartClicked successful")
                } catch (error) {
                    logger.e('unsubscribeOnClick has been failed', error)
                }
            }
        )

        this.functionManager.registerSubscription(
            "subscribeCrosshairMove",
            (input, callback) => {
                try {
                    console.error(this.seriesFunctionManager)
                    const subscription = (params) => {
                        params.sourceEvent = this.selectProps(
                            "clientX", "clientY", "pageX", "pageY", "screenX", "screenY",
                            "localX", "localY", "ctrlKey", "altKey", "shiftKey", "metaKey"
                        )(params.sourceEvent)

                        let customSeries = []
                        params.seriesData.forEach((value, key, map) => {
                            customSeries.push({id: this.seriesFunctionManager.getSeriesId(key, input), prices: value})
                        })
                        params.seriesData = customSeries

                        callback(params)
                    }
                    chart.subscribeCrosshairMove(subscription)
                    logger.d("subscribeCrosshairMove successful")
                    return subscription
                } catch (error) {
                    logger.e('subscribeCrosshairMove has been failed', error)
                    return null
                }
            },
            (subscription) => {
                try {
                    chart.unsubscribeCrosshairMove(subscription)
                    logger.d("unsubscribeCrosshairMove successful")
                } catch (error) {
                    logger.e('unsubscribeCrosshairMove has been failed', error)
                }
            }
        )
    }


    selectProps(...props) {
        return function (obj) {
            const newObj = {};
            props.forEach(name => {
                newObj[name] = obj[name];
            });

            return newObj;
        }
    }


}

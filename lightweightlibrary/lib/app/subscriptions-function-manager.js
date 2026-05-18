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
                        let mouseEvent = this.mouseEvent(params, input)
                        callback(mouseEvent)
                    }
                    this.chart.subscribeClick(subscription)
                    logger.d("subscribeOnChartClicked successful")
                    return subscription
                } catch (error) {
                    logger.e('subscribeOnClick has been failed', error)
                    return null
                }
            },
            (subscription) => {
                try {
                    this.chart.unsubscribeClick(subscription)
                    logger.d("unsubscribeOnChartClicked successful")
                } catch (error) {
                    logger.e('unsubscribeOnClick has been failed', error)
                }
            }
        )

        this.functionManager.registerSubscription("subscribeCrosshairMove",
            (input, callback) => {
                try {
                    const subscription = (params) => {
                        let mouseEvent = this.mouseEvent(params, input)
                        callback(mouseEvent)
                    }
                    this.chart.subscribeCrosshairMove(subscription)
                    logger.d("subscribeCrosshairMove successful")
                    return subscription
                } catch (error) {
                    logger.e('subscribeCrosshairMove has been failed', error)
                    return null
                }
            },
            (subscription) => {
                try {
                    this.chart.unsubscribeCrosshairMove(subscription)
                    logger.d("unsubscribeCrosshairMove successful")
                } catch (error) {
                    logger.e('unsubscribeCrosshairMove has been failed', error)
                }
            }
        )

        this.functionManager.registerSubscription(
            "subscribeDblClick",
            (input, callback) => {
                try {
                    const subscription = (params) => {
                        let mouseEvent = this.mouseEvent(params, input)
                        callback(mouseEvent)
                    }
                    this.chart.subscribeDblClick(subscription)
                    logger.d("subscribeDblClick successful")
                    return subscription
                } catch (error) {
                    logger.e('subscribeDblClick has been failed', error)
                    return null
                }
            },
            (subscription) => {
                try {
                    this.chart.unsubscribeDblClick(subscription)
                    logger.d("unsubscribeDblClick successful")
                } catch (error) {
                    logger.e('unsubscribeDblClick has been failed', error)
                }
            }
        )
    }

    mouseEvent(params, input) {
        let result = this.selectProps("time", "logical", "point", "paneIndex", "hoveredObjectId")(params)
        if (typeof params.logical === "number") {
            result.logicalFloat = params.logical
            result.logical = Math.trunc(params.logical)
        }
        result.sourceEvent = this.selectProps(
            "clientX", "clientY", "pageX", "pageY", "screenX", "screenY",
            "localX", "localY", "ctrlKey", "altKey", "shiftKey", "metaKey"
        )(params.sourceEvent)

        result.seriesData = []
        params.seriesData.forEach((value, key) => {
            result.seriesData.push({
                id: this.seriesFunctionManager.getSeriesId(key, input),
                prices: value
            })
        })


        if (params.hoveredSeries) {
            result.hoveredSeries = this.seriesFunctionManager.getSeriesId(params.hoveredSeries, input)
        }
        if (params.hoveredInfo) {
            result.hoveredInfo = {...params.hoveredInfo}
            if (params.hoveredInfo.series) {
                result.hoveredInfo.series = this.seriesFunctionManager.getSeriesId(params.hoveredInfo.series, input)
            }
        }
        return result
    }

    selectProps(...props) {
        return function (obj) {
            const newObj = {};
            props.forEach(name => {
                if (obj && obj.hasOwnProperty(name)) {
                    newObj[name] = obj[name];
                }
            });

            return newObj;
        }
    }


}

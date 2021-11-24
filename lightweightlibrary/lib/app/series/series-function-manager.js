import { logger } from '../logger.js'
import SeriesCreationService from './series-creation.js'
import SeriesInstanceService from './series-instance.js';

export default class SeriesFunctionManager {

    constructor(chart, functionManager, pluginManager) {
        this.chart = chart;
        this.functionManager = functionManager;
        this.pluginManager = pluginManager;
        this.seriesCache = new Map();
        this.seriesCreationService = new SeriesCreationService(chart, this.seriesCache, functionManager, pluginManager);
        this.seriesInstanceService = new SeriesInstanceService(chart, this.seriesCache, functionManager, pluginManager);
    }

    register() {
        this.seriesCreationService.register();
        this.seriesInstanceService.register();


        // this.functionManager.registerFunction("formatPrice", (input, resolve) => {
        //     let formatter = this.cache.get(input.params.formatterId)
        //     if (formatter === undefined) {
        //         this.functionManager.throwFatalError(new Error(`Formatter with uuid:${input.uuid} is not found`), input)
        //     } else {
        //         resolve(formatter.format(input.params.price))
        //     }
        // })
    }

    getSeriesId(seriesObject, input) {
        for (let [key, value] of this.seriesCache.entries()) {
            if (Object.is(value, seriesObject)) {
                return key
            }
        }

        this.functionManager.throwFatalError(new Error(`Series id is not found`), input)

        return undefined
    }
}

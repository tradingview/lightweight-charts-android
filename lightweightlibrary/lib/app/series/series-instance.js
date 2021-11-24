import PriceFormatterService from "./price-formatter";

export default class SeriesInstanceService {

    constructor(chart, seriesCache, functionManager, pluginManager) {
        this.chart = chart;
        this.seriesCache = seriesCache;
        this.functionManager = functionManager;
        this.pluginManager = pluginManager;
        this.lineCache = new Map();
        this.priceFormatterService = new PriceFormatterService(pluginManager);
        this.lineService = new LineService(functionManager, this.lineCache);
    }

    register() {
        this._seriesInstanceMethods().forEach((method) => {
            this.functionManager.registerFunction(method.name, (input, resolve) => {
                this._findSeries(input, (series) => {
                    method.invoke.bind({input: input})(series, input.params, resolve);
                });
            });
        });

        this._priceLineMethods().forEach((method) => {
            this.functionManager.registerFunction(method.name, (input, resolve) => {
                this.lineService.getLine(input, (line) => {
                    method.invoke.bind({input: input})(line, input.params, resolve);
                });
            });
        });
    }

    _seriesInstanceMethods() {
        return [
            new SetData(),
            new RemoveSeries(this.chart, this.seriesCache),
            new PriceToCoordinate(),
            new CoordinateToPrice(),
            new Options(),
            new SeriesType(),
            new ApplyOptions(this.priceFormatterService),
            new SetMarkers(),
            new CreatePriceLine(this.lineCache),
            new RemovePriceLine(this.lineService, this.lineCache),
            new Update()
        ];
    }

    _priceLineMethods() {
        return [
            new PriceLineOptions(),
            new PriceLineApplyOptions()
        ];
    }

    _findSeries(input, callback) {
        let series = this.seriesCache.get(input.params.seriesId)
        if (series === undefined) {
            this.functionManager.throwFatalError(new Error(`${seriesName} with uuid:${input.uuid} is not found`), input)
        } else {
            callback(series)
        }
    }
}


class LineService {
    constructor(functionManager, lineCache) {
        this.functionManager = functionManager;
        this.lineCache = lineCache;
    }

    getLine(input, callback) {
        let line = this.lineCache.get(input.params.lineId);
        if (line === undefined) {
            const error = new Error(`PriceLine with uuid:${input.uuid} is not found`);
            this.functionManager.throwFatalError(error, input);
        } else {
            callback(line);
        }
    }
}

/**
 * ==============================================================
 * Methods of series instance
 * ==============================================================
 */
class SeriesInstanceMethod {
    constructor(name, invoke) {
        this.name = name;
        this.invoke = invoke;
    }
}

class SetData extends SeriesInstanceMethod {
    constructor() {
        super("setSeries", function(series, params, resolve) {
            series.setData(params.data);
        });
    }
}

class RemoveSeries extends SeriesInstanceMethod {
    constructor(chart, seriesCache) {
        super("removeSeries", function(series, params, resolve) {
            seriesCache.delete(params.seriesId);
            chart.removeSeries(series);
            resolve();
        });
    }
}

class PriceToCoordinate extends SeriesInstanceMethod {
    constructor() {
        super("priceToCoordinate", function(series, params, resolve) {
            resolve(series.priceToCoordinate(params.price));
        });
    }
}

class CoordinateToPrice extends SeriesInstanceMethod {
    constructor() {
        super("coordinateToPrice", function(series, params, resolve) {
            resolve(series.coordinateToPrice(input.params.coordinate));
        });
    }
}

class Options extends SeriesInstanceMethod {
    constructor(pluginManager) {
        super("options", function(series, params, resolve) {
            let options = series.options();

            if (options.priceFormat.formatter !== undefined) {
                const formatter = options.priceFormat.formatter;
                options.priceFormat.formatter = pluginManager.getPlugin(formatter);
            }

            if (options.autoscaleInfoProvider !== undefined) {
                const autoscaleInfoProvider = options.autoscaleInfoProvider;
                options.autoscaleInfoProvider = pluginManager.getPlugin(autoscaleInfoProvider);
            }

            resolve(options);
        });
    }
}

class SeriesType extends SeriesInstanceMethod {
    constructor() {
        super("seriesType", function(series, params, resolve) {
            resolve(series.seriesType());
        });
    }
}

class ApplyOptions extends SeriesInstanceMethod {
    constructor(priceFormatterService) {
        super("applyOptions", function(series, params, resolve) {
            priceFormatterService.register(params, (paramsWithFormatter) => {
                series.applyOptions(paramsWithFormatter.options);
                resolve();
            });
        });
    }
}

class SetMarkers extends SeriesInstanceMethod {
    constructor() {
        super("setMarkers", function(series, params, resolve) {
            series.setMarkers(params.data);
        });
    }
}

class CreatePriceLine extends SeriesInstanceMethod {
    constructor(lineCache) {
        /**
         * {this} - raw input of method
         */
        super("createPriceLine", function(series, params, resolve) {
            let priceLine = series.createPriceLine(params.options);
            lineCache.set(this.input.uuid, priceLine);
        });
    }
}

class RemovePriceLine extends SeriesInstanceMethod {
    constructor(lineService, lineCache) {
        /**
         * {this} - raw input of method
         */
        super("removePriceLine", function(series, params, resolve) {
            lineService.getLine(this.input, (line) => {
                lineCache.delete(params.lineId);
                series.removePriceLine(line);
            });
        });
    }
}

class Update extends SeriesInstanceMethod {
    constructor() {
        super("update", function(series, params, resolve) {
            series.update(params.bar);
        });
    }
}

/**
 * ==============================================================
 * Methods of price line instance
 * ==============================================================
 */
class PriceLineInstanceMethod {
    constructor(name, invoke) {
        this.name = name;
        this.invoke = invoke;
    }
}

class PriceLineOptions extends PriceLineInstanceMethod {
    constructor() {
        super("priceLineOptions", function(line, params, resolve) {
            resolve(line.options());
        });
    }
}

class PriceLineApplyOptions extends PriceLineInstanceMethod {
    constructor() {
        super("priceLineApplyOptions", function(line, params, resolve) {
            line.applyOptions(params.options);
        });
    }
}
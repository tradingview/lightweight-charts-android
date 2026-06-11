import { createSeriesMarkers } from "lightweight-charts";
import FunctionManager from "../function-manager";
import PaneCache from "../pane/pane-cache";
import PluginManager from "../plugin-manager";
import LineCache from "./line-cache";
import LineService from "./line-service";
import MarkersCache from "./markers-cache";
import PriceFormatterService from "./price-formatter";
import SeriesCache from "./series-cache";

export default class SeriesInstanceService {

    constructor(locator) {
        this.chart = locator.resolve("chart");
        this.seriesCache = locator.resolve(SeriesCache.name);
        this.functionManager = locator.resolve(FunctionManager.name);
        this.pluginManager = locator.resolve(PluginManager.name);
        this.lineCache = locator.resolve(LineCache.name);
        this.markersCache = locator.resolve(MarkersCache.name);
        this.paneCache = locator.resolve(PaneCache.name);
        this.priceFormatterService = locator.resolve(PriceFormatterService.name);
        this.lineService = locator.resolve(LineService.name);
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

        this._markerMethods().forEach((method) => {
            this.functionManager.registerFunction(method.name, (input, resolve) => {
                const markers = this.markersCache.get(input.params.markersId);
                if (markers === undefined) {
                    this.functionManager.throwFatalError(new Error(`Series markers with uuid:${input.params.markersId} are not found`), input);
                } else {
                    method.invoke(markers.api, input.params, resolve);
                }
            });
        });

        this.functionManager.registerSubscription(
            "subscribeDataChanged",
            (input, callback) => {
                try {
                    return this._findSeries(input, (series) => {
                        const subscription = () => callback(null)
                        series.subscribeDataChanged(subscription)
                        return { series, seriesId: input.params.seriesId, subscription }
                    })
                } catch (error) {
                    this.functionManager.throwFatalError(error, input)
                    return null
                }
            },
            (subscription) => {
                try {
                    if (subscription) {
                        subscription.series.unsubscribeDataChanged(subscription.subscription)
                    }
                } catch (error) {
                    this.functionManager.throwFatalError(error, subscription)
                }
            }
        );
    }

    _seriesInstanceMethods() {
        return [
            new SetData(),
            new Data(),
            new RemoveSeries(this.chart, this.seriesCache, this.markersCache, this.lineCache, this.functionManager),
            new PriceToCoordinate(),
            new CoordinateToPrice(),
            new BarsInLogicalRange(),
            new PriceFormatterFormat(),
            new Options(this.pluginManager),
            new DataByIndex(),
            new SeriesType(),
            new ApplyOptions(this.priceFormatterService),
            new SetMarkers(this.markersCache),
            new GetMarkers(this.markersCache),
            new CreateSeriesMarkers(this.markersCache),
            new CreatePriceLine(this.lineCache),
            new RemovePriceLine(this.lineService, this.lineCache),
            new PriceLines(this.lineCache),
            new Update(),
            new Pop(),
            new SeriesOrder(),
            new SetSeriesOrder(),
            new MoveToPane(),
            new GetPane(this.paneCache),
            new LastValueData()
        ];
    }

    _priceLineMethods() {
        return [
            new PriceLineOptions(),
            new PriceLineApplyOptions()
        ];
    }

    _markerMethods() {
        return [
            new SeriesMarkersSet(),
            new SeriesMarkersGet(),
            new SeriesMarkersApplyOptions(),
            new SeriesMarkersDetach(this.markersCache),
        ];
    }

    _findSeries(input, callback) {
        let series = this.seriesCache.get(input.params.seriesId)
        if (series === undefined) {
            this.functionManager.throwFatalError(new Error(`Series with uuid:${input.params.seriesId} is not found`), input)
        } else {
            return callback(series)
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
        super("setSeries", function (series, params, resolve) {
            series.setData(params.data);
            resolve();
        });
    }
}

class Data extends SeriesInstanceMethod {
    constructor() {
        super("seriesData", function (series, params, resolve) {
            resolve(series.data());
        });
    }
}

class RemoveSeries extends SeriesInstanceMethod {
    constructor(chart, seriesCache, markersCache, lineCache, functionManager) {
        super("removeSeries", function (series, params, resolve) {
            functionManager.removeSubscriptions((subscription, name) => {
                return name === "subscribeDataChanged" && subscription?.seriesId === params.seriesId;
            });
            for (let [markersId, markers] of markersCache.entries()) {
                if (markers.seriesId === params.seriesId) {
                    markers.api.detach();
                    markersCache.delete(markersId);
                }
            }
            const seriesPriceLines = series.priceLines();
            for (let [lineId, line] of lineCache.entries()) {
                if (seriesPriceLines.some((seriesLine) => Object.is(seriesLine, line))) {
                    lineCache.delete(lineId);
                }
            }
            seriesCache.delete(params.seriesId);
            chart.removeSeries(series);
            resolve();
        });
    }
}

class PriceToCoordinate extends SeriesInstanceMethod {
    constructor() {
        super("priceToCoordinate", function (series, params, resolve) {
            resolve(series.priceToCoordinate(params.price));
        });
    }
}

class CoordinateToPrice extends SeriesInstanceMethod {
    constructor() {
        super("coordinateToPrice", function (series, params, resolve) {
            resolve(series.coordinateToPrice(params.coordinate));
        });
    }
}

class BarsInLogicalRange extends SeriesInstanceMethod {
    constructor() {
        super("barsInLogicalRange", function (series, params, resolve) {
            resolve(series.barsInLogicalRange(params.range));
        });
    }
}

class PriceFormatterFormat extends SeriesInstanceMethod {
    constructor() {
        super("priceFormatterFormat", function (series, params, resolve) {
            resolve(series.priceFormatter().format(params.price));
        });
    }
}

class Options extends SeriesInstanceMethod {
    constructor(pluginManager) {
        super("options", function (series, params, resolve) {
            let options = series.options();

            if (options.priceFormat && options.priceFormat.formatter !== undefined) {
                const formatter = options.priceFormat.formatter;
                options.priceFormat.formatter = pluginManager.getPlugin(formatter);
            }

            if (options.priceFormat && options.priceFormat.tickmarksFormatter !== undefined) {
                const tickmarksFormatter = options.priceFormat.tickmarksFormatter;
                options.priceFormat.tickmarksFormatter = pluginManager.getPlugin(tickmarksFormatter);
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
        super("seriesType", function (series, params, resolve) {
            resolve(series.seriesType());
        });
    }
}

class ApplyOptions extends SeriesInstanceMethod {
    constructor(priceFormatterService) {
        super("applyOptionsSeries", function (series, params, resolve) {
            priceFormatterService.register(params, (paramsWithFormatter) => {
                series.applyOptions(paramsWithFormatter.options);
                resolve();
            });
        });
    }
}

class DataByIndex extends SeriesInstanceMethod {
    constructor() {
        super("dataByIndexSeries", function (series, params, resolve) {
            const d = series.dataByIndex(params.logicalIndex, params.mismatchDirection)
            resolve(d)
        });
    }
}

class SetMarkers extends SeriesInstanceMethod {
    constructor(markersCache) {
        super("setMarkers", function (series, params, resolve) {
            const markers = compatMarkers(markersCache, params.seriesId, series);
            markers.api.setMarkers(params.data || []);
            resolve();
        });
        this.markersCache = markersCache;
    }
}

class GetMarkers extends SeriesInstanceMethod {
    constructor(markersCache) {
        super("getMarkersSeries", function (series, params, resolve) {
            const markers = compatMarkers(markersCache, params.seriesId, series);
            resolve(markers.api.markers());
        });
        this.markersCache = markersCache;
    }
}

class CreateSeriesMarkers extends SeriesInstanceMethod {
    constructor(markersCache) {
        super("createSeriesMarkersCompat", function (series, params, resolve) {
            const api = createSeriesMarkers(series, params.data || [], params.options || {});
            markersCache.set(this.input.uuid, {
                api: api,
                seriesId: params.seriesId
            });
            resolve(this.input.uuid);
        });
        this.markersCache = markersCache;
    }
}

class CreatePriceLine extends SeriesInstanceMethod {
    constructor(lineCache) {
        /**
         * {this} - raw input of method
         */
        super("createPriceLine", function (series, params, resolve) {
            let priceLine = series.createPriceLine(params.options);
            lineCache.set(this.input.uuid, priceLine);
            resolve(this.input.uuid);
        });
    }
}

class RemovePriceLine extends SeriesInstanceMethod {
    constructor(lineService, lineCache) {
        /**
         * {this} - raw input of method
         */
        super("removePriceLine", function (series, params, resolve) {
            lineService.getLine(this.input, (line) => {
                lineCache.delete(params.lineId);
                series.removePriceLine(line);
                resolve();
            });
        });
    }
}

class PriceLines extends SeriesInstanceMethod {
    constructor(lineCache) {
        super("priceLines", function (series, params, resolve) {
            const ids = series.priceLines().map((line, index) => {
                const existing = findCacheKey(lineCache, line);
                if (existing) {
                    return existing;
                }
                const id = `${params.seriesId}:price-line:${Date.now()}:${index}`;
                lineCache.set(id, line);
                return id;
            });
            resolve(ids);
        });
    }
}

class Update extends SeriesInstanceMethod {
    constructor() {
        super("update", function (series, params, resolve) {
            series.update(params.bar, params.historicalUpdate || false);
            resolve();
        });
    }
}

class Pop extends SeriesInstanceMethod {
    constructor() {
        super("pop", function (series, params, resolve) {
            resolve(series.pop(params.count));
        });
    }
}

class SeriesOrder extends SeriesInstanceMethod {
    constructor() {
        super("seriesOrder", function (series, params, resolve) {
            resolve(series.seriesOrder());
        });
    }
}

class SetSeriesOrder extends SeriesInstanceMethod {
    constructor() {
        super("setSeriesOrder", function (series, params, resolve) {
            series.setSeriesOrder(params.order);
            resolve();
        });
    }
}

class MoveToPane extends SeriesInstanceMethod {
    constructor() {
        super("moveToPane", function (series, params, resolve) {
            series.moveToPane(params.paneIndex);
            resolve();
        });
    }
}

class GetPane extends SeriesInstanceMethod {
    constructor(paneCache) {
        super("getSeriesPane", function (series, params, resolve) {
            const pane = series.getPane();
            resolve(cachePane(paneCache, pane));
        });
    }
}

class LastValueData extends SeriesInstanceMethod {
    constructor() {
        super("lastValueData", function (series, params, resolve) {
            resolve(series.lastValueData(params.globalLast || false));
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
        super("priceLineOptions", function (line, params, resolve) {
            resolve(line.options());
        });
    }
}

class PriceLineApplyOptions extends PriceLineInstanceMethod {
    constructor() {
        super("priceLineApplyOptions", function (line, params, resolve) {
            line.applyOptions(params.options);
            resolve();
        });
    }
}

/**
 * ==============================================================
 * Methods of series marker primitive instances
 * ==============================================================
 */
class SeriesMarkersInstanceMethod {
    constructor(name, invoke) {
        this.name = name;
        this.invoke = invoke;
    }
}

class SeriesMarkersSet extends SeriesMarkersInstanceMethod {
    constructor() {
        super("seriesMarkersSet", function (markers, params, resolve) {
            markers.setMarkers(params.data || []);
            resolve();
        });
    }
}

class SeriesMarkersGet extends SeriesMarkersInstanceMethod {
    constructor() {
        super("seriesMarkersGet", function (markers, params, resolve) {
            resolve(markers.markers());
        });
    }
}

class SeriesMarkersApplyOptions extends SeriesMarkersInstanceMethod {
    constructor() {
        super("seriesMarkersApplyOptions", function (markers, params, resolve) {
            markers.applyOptions(params.options || {});
            resolve();
        });
    }
}

class SeriesMarkersDetach extends SeriesMarkersInstanceMethod {
    constructor(markersCache) {
        super("seriesMarkersDetach", function (markers, params, resolve) {
            markers.detach();
            markersCache.delete(params.markersId);
            resolve();
        });
    }
}

function compatMarkers(markersCache, seriesId, series) {
    const id = `${seriesId}:compat-markers`;
    let markers = markersCache.get(id);
    if (!markers) {
        markers = {
            api: createSeriesMarkers(series, []),
            seriesId: seriesId
        };
        markersCache.set(id, markers);
    }
    return markers;
}

function findCacheKey(cache, value) {
    for (let [key, cached] of cache.entries()) {
        if (Object.is(cached, value)) {
            return key;
        }
    }
    return undefined;
}

function cachePane(paneCache, pane) {
    try {
        return paneCache.getKeyOfPane(pane);
    } catch (e) {
        const id = `pane:${Date.now()}:${paneCache.size}`;
        paneCache.set(id, pane);
        return id;
    }
}

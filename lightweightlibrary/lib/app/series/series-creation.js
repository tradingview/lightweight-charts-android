import * as LightweightCharts from "lightweight-charts";
import FunctionManager from "../function-manager";
import PaneCache from "../pane/pane-cache";
import PluginManager from "../plugin-manager";
import SeriesCache from "./series-cache";

const SERIES_DEFINITIONS = {
    Line: LightweightCharts.LineSeries,
    LINE: LightweightCharts.LineSeries,
    Area: LightweightCharts.AreaSeries,
    AREA: LightweightCharts.AreaSeries,
    Bar: LightweightCharts.BarSeries,
    BAR: LightweightCharts.BarSeries,
    Candlestick: LightweightCharts.CandlestickSeries,
    CANDLESTICK: LightweightCharts.CandlestickSeries,
    Histogram: LightweightCharts.HistogramSeries,
    HISTOGRAM: LightweightCharts.HistogramSeries,
    Baseline: LightweightCharts.BaselineSeries,
    BASELINE: LightweightCharts.BaselineSeries,
}

export default class SeriesCreationService {

    /**
     * 
     * @param {ServiceLocator} locator 
     */
    constructor(locator) {
        this.chart = locator.resolve("chart");
        this.seriesCache = locator.resolve(SeriesCache.name);
        this.paneCache = locator.resolve(PaneCache.name);
        this.functionManager = locator.resolve(FunctionManager.name);
        this.pluginManager = locator.resolve(PluginManager.name);
    }

    register() {
        this._seriesFunctions().forEach((method) => {
            this.functionManager.registerFunction(method.name, (input, resolve) => {
                this._registerPlugins(input.params, (params) => {
                    this._addSeries(input.uuid, method.invoke(params));
                    resolve(input.uuid);
                });
            });
        });

        this.functionManager.registerFunction("addSeries", (input, resolve) => {
            this._registerPlugins(input.params, (params) => {
                this._addSeries(input.uuid, this.chart.addSeries(
                    this._seriesDefinition(params.seriesType),
                    params.options || {},
                    params.paneIndex || 0
                ));
                resolve(input.uuid);
            });
        });

        this.functionManager.registerFunction("paneAddSeries", (input, resolve) => {
            this._registerPlugins(input.params, (params) => {
                const pane = this.paneCache.get(params.paneId);
                if (!pane) {
                    this.functionManager.throwFatalError(new Error(`Pane with uuid:${params.paneId} is not found`), input);
                    return;
                }
                this._addSeries(input.uuid, pane.addSeries(
                    this._seriesDefinition(params.seriesType),
                    params.options || {}
                ));
                resolve(input.uuid);
            });
        });
    }

    _registerAutoscaleInfoProvider(params, callback) {
        params.options = params.options || {};
        if (!params.options.autoscaleInfoProvider) {
            callback(params);
            return;
        }

        const plugin = params.options.autoscaleInfoProvider;
        this.pluginManager.register(plugin, (fun) => {
            params.options.autoscaleInfoProvider = fun;
            callback(params);
        });
    }

    _registerPriceFormatter(params, callback) {
        params.options = params.options || {};
        if (!params.options.priceFormat || !params.options.priceFormat.formatter) {
            callback(params);
            return;
        }

        const plugin = params.options.priceFormat.formatter;
        this.pluginManager.register(plugin, (fun) => {
            params.options.priceFormat.formatter = fun;
            callback(params);
        });
    }

    _registerTickmarksFormatter(params, callback) {
        params.options = params.options || {};
        if (!params.options.priceFormat || !params.options.priceFormat.tickmarksFormatter) {
            callback(params);
            return;
        }

        const plugin = params.options.priceFormat.tickmarksFormatter;
        this.pluginManager.register(plugin, (fun) => {
            params.options.priceFormat.tickmarksFormatter = fun;
            callback(params);
        });
    }

    _registerPlugins(rawParams, callback) {
        this._registerAutoscaleInfoProvider(rawParams, (autoscaleParams) => {
            this._registerPriceFormatter(autoscaleParams, (params) => {
                this._registerTickmarksFormatter(params, callback);
            });
        });
    }

    _addSeries(uuid, series) {
        this.seriesCache.set(uuid, series);
    }

    _seriesDefinition(seriesType) {
        const definition = SERIES_DEFINITIONS[seriesType];
        if (!definition) {
            throw new Error(`Series type ${seriesType} is not supported`);
        }
        return definition;
    }

    _seriesFunctions() {
        return [
            new AddAreaSeries(this.chart),
            new AddLineSeries(this.chart),
            new AddBarSeries(this.chart),
            new AddCandlestickSeries(this.chart),
            new AddHistogramSeries(this.chart),
            new AddBaselineSeries(this.chart)
        ];
    }
}

class SeriesCreationMethod {
    constructor(name, invoke) {
        this.name = name;
        this.invoke = invoke;
    }
}

class AddAreaSeries extends SeriesCreationMethod {
    constructor(chart) {
        super("addAreaSeries", function(params) {
            return chart.addSeries(LightweightCharts.AreaSeries, params.options || {});
        })
    }
}

class AddLineSeries extends SeriesCreationMethod {
    constructor(chart) {
        super("addLineSeries", function(params) {
            return chart.addSeries(LightweightCharts.LineSeries, params.options || {});
        })
    }
}

class AddBarSeries extends SeriesCreationMethod {
    constructor(chart) {
        super("addBarSeries", function(params) {
            return chart.addSeries(LightweightCharts.BarSeries, params.options || {});
        })
    }
}

class AddCandlestickSeries extends SeriesCreationMethod {
    constructor(chart) {
        super("addCandlestickSeries", function(params) {
            return chart.addSeries(LightweightCharts.CandlestickSeries, params.options || {});
        })
    }
}

class AddHistogramSeries extends SeriesCreationMethod {
    constructor(chart) {
        super("addHistogramSeries", function(params) {
            return chart.addSeries(LightweightCharts.HistogramSeries, params.options || {});
        })
    }
}

class AddBaselineSeries extends SeriesCreationMethod {
    constructor(chart) {
        super("addBaselineSeries", function(params) {
            return chart.addSeries(LightweightCharts.BaselineSeries, params.options || {});
        })
    }
}

import FunctionManager from "../function-manager";
import PluginManager from "../plugin-manager";
import ServiceLocator from "../service-locator/locator";
import TickMarkFormatterService from "./tick-mark-formatter";

export default class TimeScaleInstanceService {

    /**
     * 
     * @param {ServiceLocator} locator 
     */
    constructor(locator) {
        this.chart = locator.resolve("chart");

        /** @type {PluginManager} */
        this.pluginManager = locator.resolve(PluginManager.name);

        /** @type {FunctionManager} */
        this.functionManager = locator.resolve(FunctionManager.name);

        /** @type {TickMarkFormatterService} */
        this.tickMarkFormatter = locator.resolve(TickMarkFormatterService.name);
    }

    _timeScale() {
        return this.chart.timeScale();
    }

    _timeScaleInstanceMethods() {
        return [
            new ScrollPosition(),
            new ScrollToPosition(),
            new TimeScaleOptions(this.pluginManager),
            new TimeScaleApplyOptions(this.tickMarkFormatter),
            new ScrollToRealTime(),
            new GetVisibleRange(),
            new SetVisibleRange(),
            new ResetTimeScale(),
            new FitContent(),
            new TimeToCoordinate(),
            new CoordinateToTime(),
            new LogicalToCoordinate(),
            new CoordinateToLogical(),
            new Width(),
            new Height()
        ];
    }

    register() {
        this._timeScaleInstanceMethods().forEach((method) => {
            this.functionManager.registerFunction(method.name, (input, resolve) => {
                method.invoke(this._timeScale(), input.params, resolve);
            });
        });
        
        this.functionManager.registerSubscription(
            "subscribeVisibleTimeRangeChange",
            (input, callback) => {
                this._timeScale().subscribeVisibleTimeRangeChange(callback);
                return callback;
            },
            (subscription) => {
                this._timeScale().unsubscribeVisibleTimeRangeChange(subscription);
            }
        );

        this.functionManager.registerSubscription(
            "subscribeTimeScaleSizeChange",
            (input, callback) => {
                const subcription = (width, height) => {
                    callback({width: width, height: height});
                };
                this._timeScale().subscribeSizeChange(subcription);
                return subcription;
            },
            (subscription) => {
                this._timeScale().unsubscribeSizeChange(subscription);
            }
        );
    }
}

class TimeScaleMethod {
    constructor(name, invoke) {
        this.name = name;
        this.invoke = invoke;
    }
}

class TimeScaleMethodWithReturn extends TimeScaleMethod {
    constructor(name, invoke) {
        super(name, (timeScale, params, resolve) => {
            resolve(invoke(timeScale, params));
        });
    }
}

class ScrollPosition extends TimeScaleMethodWithReturn {
    constructor() {
        super("scrollPosition", (timeScale, params) => {
            return timeScale.scrollPosition();
        });
    }
}

class ScrollToPosition extends TimeScaleMethod {
    constructor() {
        super("scrollToPosition", (timeScale, params) => {
            timeScale.scrollToPosition(params.position, params.animated);
        });
    }
}

class TimeScaleOptions extends TimeScaleMethodWithReturn {
    constructor(pluginManager) {
        super("timeScaleOptions", (timeScale, params) => {
            const options = timeScale.options();
            if (options.tickMarkFormatter) {
                const fun = options.tickMarkFormatter;
                options.tickMarkFormatter = pluginManager.getPlugin(fun);
            }
            return options;
        });
    }
}

class TimeScaleApplyOptions extends TimeScaleMethod {
    /**
     * 
     * @param {TickMarkFormatterService} tickMarkFormatter 
     */
    constructor(tickMarkFormatter) {
        super("timeScaleApplyOptions", (timeScale, params) => {
            tickMarkFormatter.register(params, (paramsWithPlugin) => {
                timeScale.applyOptions(paramsWithPlugin.options);
            });
        })
    }
}

class ScrollToRealTime extends TimeScaleMethod {
    constructor() {
        super("scrollToRealTime", (timeScale, params) => {
            timeScale.scrollToRealTime();
        });
    }
}

class GetVisibleRange extends TimeScaleMethodWithReturn {
    constructor() {
        super("getVisibleRange", (timeScale, params) => {
            return timeScale.getVisibleRange();
        });
    }
}

class SetVisibleRange extends TimeScaleMethod {
    constructor() {
        super("setVisibleRange", (timeScale, params) => {
            timeScale.setVisibleRange(params.range);
        });
    }
}

class ResetTimeScale extends TimeScaleMethod {
    constructor() {
        super("resetTimeScale", (timeScale, params) => {
            timeScale.resetTimeScale();
        });
    }
}

class FitContent extends TimeScaleMethod {
    constructor() {
        super("fitContent", (timeScale, params) => {
            timeScale.fitContent();
        });
    }
}

class TimeToCoordinate extends TimeScaleMethodWithReturn {
    constructor() {
        super("timeToCoordinate", (timeScale, params) => {
            return timeScale.timeToCoordinate(params.time);
        });
    }
}

class CoordinateToTime extends TimeScaleMethodWithReturn {
    constructor() {
        super("coordinateToTime", (timeScale, params) => {
            return timeScale.coordinateToTime(params.x);
        });
    }
}

class LogicalToCoordinate extends TimeScaleMethodWithReturn {
    constructor() {
        super("logicalToCoordinate", (timeScale, params) => {
            return timeScale.logicalToCoordinate(params.logical);
        });
    }
}

class CoordinateToLogical extends TimeScaleMethodWithReturn {
    constructor() {
        super("coordinateToLogical", (timeScale, params) => {
            return timeScale.coordinateToLogical(params.x);
        });
    }
}

class Width extends TimeScaleMethodWithReturn {
    constructor() {
        super("timeScaleWidth", (timeScale, params) => {
            return timeScale.width();
        });
    }
}

class Height extends TimeScaleMethodWithReturn {
    constructor() {
        super("timeScaleHeight", (timeScale, params) => {
            return timeScale.height();
        });
    }
}
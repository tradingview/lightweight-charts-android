import FunctionManager from "../function-manager.js";
import PriceScaleCache from "./price-scale-cache.js";

export default class PriceScaleInstanceService {

    constructor(locator) {
        this.chart = locator.resolve("chart");
        this.priceScaleCache = locator.resolve(PriceScaleCache.name);
        this.functionManager = locator.resolve(FunctionManager.name);
    }

    register() {
        this._priceScaleInstanceMethods().forEach((method) => {
            this.functionManager.registerFunction(method.name, (input, resolve) => {
                const scale = this.priceScaleCache.get(input.params.caller)
                if (scale === undefined) {
                    this.functionManager.throwFatalError(new Error(`PriceScale with uuid:${input.params.caller} is not found`), input)
                } else {
                    method.invoke(scale, input.params, resolve)
                }
            });
        });

    }

    _priceScaleInstanceMethods() {
        return [
            new PriceScaleOptions(),
            new ApplyOptions(),
            new PriceScaleWidth(),
            new SetVisibleRange(),
            new GetVisibleRange(),
            new SetAutoScale(),
        ];
    }

    _priceLineMethods() {
        return [
            new PriceLineOptions(),
            new PriceLineApplyOptions()
        ];
    }

}

/**
 * ==============================================================
 * Methods of price-scale instance
 * ==============================================================
 */
class PriceScaleInstanceMethod {
    constructor(name, invoke) {
        this.name = name;
        this.invoke = invoke;
    }
}

class ApplyOptions extends PriceScaleInstanceMethod {
    constructor() {
        super("priceScaleApplyOptions", function (scale, params, resolve) {
            scale.applyOptions(params.options)
            resolve()
        });
    }
}

class PriceScaleOptions extends PriceScaleInstanceMethod {
    constructor() {
        super("priceScaleOptions", function (scale, params, resolve) {
            resolve(scale.options());
        });
    }
}


class PriceScaleWidth extends PriceScaleInstanceMethod {
    constructor() {
        super("priceScaleWidth", function (scale, params, resolve) {
            resolve(scale.width())
        });
    }
}

class SetVisibleRange extends PriceScaleInstanceMethod {
    constructor() {
        super("priceScaleSetVisibleRange", function (scale, params, resolve) {
            scale.setVisibleRange(params.range);
            resolve();
        });
    }
}

class GetVisibleRange extends PriceScaleInstanceMethod {
    constructor() {
        super("priceScaleGetVisibleRange", function (scale, params, resolve) {
            resolve(scale.getVisibleRange());
        });
    }
}

class SetAutoScale extends PriceScaleInstanceMethod {
    constructor() {
        super("priceScaleSetAutoScale", function (scale, params, resolve) {
            scale.setAutoScale(params.on);
            resolve();
        });
    }
}

import FunctionManager from "../function-manager";
import PluginManager from "../plugin-manager";
import LineCache from "../series/line-cache";
import LineService from "../series/line-service";
import PriceFormatterService from "../series/price-formatter";
import SeriesCache from "../series/series-cache";
import SeriesCreationService from "../series/series-creation";
import SeriesFunctionManager from "../series/series-function-manager";
import SeriesInstanceService from "../series/series-instance";
import TickMarkFormatterService from "../time-scale/tick-mark-formatter";
import TimeScaleFunctionManager from "../time-scale/time-scale-function-manager";
import TimeScaleInstanceService from "../time-scale/time-scale-instance";
import { Locator } from "./locator";

export function initLocator(functionManager, pluginManager, chart) {
    new LocatorComponent().init(functionManager, pluginManager, chart);
}

class LocatorComponent {
    
    init(functionManager, pluginManager, chart) {
        Locator.register(FunctionManager.name, () => functionManager);
        Locator.register(PluginManager.name, () => pluginManager);
        Locator.register("chart", () => chart);

        
        this.registerLineCache();
        this.registerLineService();
        this.registerSeriesCache();

        this.registerSeriesFunctionManager();

        this.registerSeriesCreationService();
        this.registerSeriesInstanceService();
    
        this.registerPriceFormatterService();

        this.registerTimeScaleFunctionManager();
        this.registerTimeScaleInstanceService();
        this.registerTickMarkFormatterService();
    }

    registerLineCache() {
        Locator.register(LineCache.name, () => new LineCache());
    }

    registerLineService() {
        Locator.register(LineService.name, () => new LineService(Locator));
    }

    registerSeriesCache() {
        Locator.register(SeriesCache.name, () => new SeriesCache());
    }

    registerSeriesFunctionManager() {
        Locator.register(SeriesFunctionManager.name, () => new SeriesFunctionManager(Locator));
    }

    registerSeriesCreationService() {
        Locator.register(SeriesCreationService.name, () => new SeriesCreationService(Locator));
    }

    registerSeriesInstanceService() {
        Locator.register(SeriesInstanceService.name, () => new SeriesInstanceService(Locator));
    }
    
    registerPriceFormatterService() {
        Locator.register(PriceFormatterService.name, () => new PriceFormatterService(Locator));
    }

    registerTimeScaleFunctionManager() {
        Locator.register(TimeScaleFunctionManager.name,  () => new TimeScaleFunctionManager(Locator));
    }

    registerTimeScaleInstanceService() {
        Locator.register(TimeScaleInstanceService.name, () => new TimeScaleInstanceService(Locator));
    }

    registerTickMarkFormatterService() {
        Locator.register(TickMarkFormatterService.name, () => new TickMarkFormatterService(Locator));
    }
}
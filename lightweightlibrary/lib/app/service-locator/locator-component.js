import FunctionManager from "../function-manager";
import PluginManager from "../plugin-manager";
import LineCache from "../series/line-cache";
import LineService from "../series/line-service";
import PriceFormatterService from "../series/price-formatter";
import SeriesCache from "../series/series-cache";
import SeriesCreationService from "../series/series-creation";
import SeriesFunctionManager from "../series/series-function-manager";
import SeriesInstanceService from "../series/series-instance";
import MarkersCache from "../series/markers-cache";
import PaneCache from "../pane/pane-cache";
import TickMarkFormatterService from "../time-scale/tick-mark-formatter";
import TimeScaleFunctionManager from "../time-scale/time-scale-function-manager";
import TimeScaleInstanceService from "../time-scale/time-scale-instance";
import PriceScaleCache from "../price-scale/price-scale-cache";
import PriceScaleCreationService from "../price-scale/price-scale-creation";
import PriceScaleInstanceService from "../price-scale/price-scale-instance";
import PriceScaleFunctionManager from "../price-scale/price-scale-function-manager";
import WatermarkCache from "../watermark/watermark-cache";
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
        this.registerMarkersCache();
        this.registerPaneCache();
        this.registerWatermarkCache();

        this.registerSeriesFunctionManager();

        this.registerSeriesCreationService();
        this.registerSeriesInstanceService();
    
        this.registerPriceFormatterService();

        this.registerTimeScaleFunctionManager();
        this.registerTimeScaleInstanceService();

        this.registerPriceScaleCache();
        this.registerPriceScaleCreationService();
        this.registerPriceScaleInstanceService();
        this.registerPriceScaleFunctionManager();


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

    registerMarkersCache() {
        Locator.register(MarkersCache.name, () => new MarkersCache());
    }

    registerPaneCache() {
        Locator.register(PaneCache.name, () => new PaneCache());
    }

    registerPriceScaleCache() {
        Locator.register(PriceScaleCache.name, () => new PriceScaleCache());
    }

    registerWatermarkCache() {
        Locator.register(WatermarkCache.name, () => new WatermarkCache());
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

    registerPriceScaleCreationService(){
        Locator.register(PriceScaleCreationService.name, () => new PriceScaleCreationService(Locator));
    }

    registerPriceScaleInstanceService(){
        Locator.register(PriceScaleInstanceService.name, () => new PriceScaleInstanceService(Locator));
    }

    registerPriceScaleFunctionManager(){
        Locator.register(PriceScaleFunctionManager.name, () => new PriceScaleFunctionManager(Locator));
    }

    registerTickMarkFormatterService() {
        Locator.register(TickMarkFormatterService.name, () => new TickMarkFormatterService(Locator));
    }
}

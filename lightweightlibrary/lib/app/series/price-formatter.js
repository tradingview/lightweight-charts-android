import PluginManager from "../plugin-manager";

export default class PriceFormatterService {

    /**
     * 
     * @param {ServiceLocator} locator 
     */
    constructor(locator) {
        this.pluginManager = locator.resolve(PluginManager.name);
    }

    register(params, callback) {
        if (!params.options.priceFormat) {
            callback(params);
            return;
        }

        this._registerPriceFormatPlugin(params, "formatter", () => {
            this._registerPriceFormatPlugin(params, "tickmarksFormatter", () => callback(params));
        })
    }

    _registerPriceFormatPlugin(params, key, callback) {
        if (!params.options.priceFormat[key]) {
            callback();
            return;
        }

        const plugin = params.options.priceFormat[key];
        this.pluginManager.register(plugin, (fun) => {
            params.options.priceFormat[key] = fun;
            callback();
        });
    }
}

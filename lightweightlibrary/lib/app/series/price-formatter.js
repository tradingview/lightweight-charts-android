export default class PriceFormatterService {
    constructor(pluginManager) {
        this.pluginManager = pluginManager;
    }

    register(params, callback) {
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
}
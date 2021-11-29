import PluginManager from "../plugin-manager";
import ServiceLocator from "../service-locator/locator";

export default class TickMarkFormatterService {

    /**
     * 
     * @param {ServiceLocator} locator 
     */
    constructor(locator) {
        this.pluginManager = locator.resolve(PluginManager.name);
    }

    register(params, onSuccess) {
        if (!params.options.tickMarkFormatter) {
            onSuccess(params);
            return;
        }

        const plugin = params.options.tickMarkFormatter;
        this.pluginManager.register(plugin, (fun) => {
            params.options.tickMarkFormatter = fun;
            onSuccess(params);
        });
    }
}
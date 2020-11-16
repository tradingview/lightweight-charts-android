import { createChart } from 'lightweight-charts';
import FunctionManager from './function-manager.js';
import ChartRegistrationFunctionsController from './chart-registration-functions-controller.js';
import PluginManager from './plugin-manager.js';

onmessage = function (message) {
    console.debug("received message", message)
    const connectionMessage = JSON.parse(message.data)

    if (connectionMessage.messageType !== "Message::Connection") {
        console.error("Connection message has wrong")
        return
    }

    const debug = connectionMessage.data.debug

    const port = message.ports[0]
    const functionManager = new FunctionManager(port)

    const pluginManager = new PluginManager()
    window['pluginManager'] = pluginManager

    const functionsController = new ChartRegistrationFunctionsController(
        window['chart'],
        functionManager,
        pluginManager
    )
    functionsController.registerFunctions()
    window['functionsController'] = functionsController

    console.debug("Connection has been established")
    port.onmessage = function (event) {
        const nativeMessage = JSON.parse(event.data)

        if (debug) {
            if (nativeMessage.data.fn) {
                console.debug("function", nativeMessage.data.fn)
            }
            console.debug("data", JSON.stringify(nativeMessage.data))
        }

        switch (nativeMessage.messageType) {
            case 'Message::Function':
                functionManager.call(nativeMessage.data)
                break;
            case 'Message::Subscription':
                functionManager.subscribe(nativeMessage.data)
                break;
            case 'Message::SubscriptionCancellation':
                functionManager.unsubscribe(nativeMessage.data)
                break;
        }
    }
}

window.onresize = () => {
    window["chart"].resize(window.innerWidth, window.innerHeight)
}

onload = () => {
    window["chart"] = createChart(document.body, { width: window.innerWidth, height: window.innerHeight });
}

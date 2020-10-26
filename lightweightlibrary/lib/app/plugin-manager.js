export default class PluginManager {
    constructor() {
        this.plugins = []
    }

    register(plugin, consumer) {
        console.log("registration of plugin", plugin)
        const name = plugin.name
        const fileLink = plugin.file
        console.log("addScript", window[plugin.functionName])
        
        const foundPlugin = this.plugins.find((item) => item.name == name)
        if (foundPlugin) {
            consumer(foundPlugin.functionName)
        } else {
            this.addScript(fileLink, () => {
                const mainFun = window[plugin.functionName]
                delete window[plugin.functionName]
                this.plugins.push({
                    name: name, 
                    file: fileLink,
                    functionName: mainFun
                })
                consumer(mainFun)
            })
        }
    }

    addScript(file, onLoad) {
        var script = document.createElement('script')
        script.type = 'text/javascript'
        script.src = file
        script.async = false
        script.onload = onLoad
        document.getElementsByTagName('head')[0].appendChild(script)
    }
}
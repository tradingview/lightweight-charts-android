export default class FunctionManager {
    constructor(port) {
        this.functions = []
        this.port = port
        this.subscriberCallbacksMap = new Map()
    }

    registerFunction(functionName, functionRef) {
        if (!this.hasDuplicates(functionName)) {
            this.functions.push({name: functionName, functionRef: functionRef})
        } else {
            console.error(`Function:${functionName} already registered`)
        }
    }

    registerSubscription(subscriptionName, subscribe, unsubscribe) {
        if (!this.hasDuplicates(subscriptionName)) {
            this.functions.push({name: subscriptionName, subscribe: subscribe, unsubscribe: unsubscribe })
        } else {
            console.error(`Subscription:${subscriptionName} already registered`)
        }
    }

    call(data) {
        const fn = this.functions.find((value) => { return value.name === data.fn })

        if (fn === undefined) {
            this.throwFatalError(`${data.fn} is not found`, data)
            return
        }

        try {
            fn.functionRef(data, (result) => {
                this.port.postMessage(JSON.stringify({
                    messageType: "Message::FunctionResult",
                    data: {
                        fn: data.fn,
                        uuid: data.uuid,
                        result: result
                    }
                }))
            })
        } catch (e) {
            this.throwFatalError(e.message, data)
        }
    }

    subscribe(data) {
        const fn = this.functions.find((value) => { return value.name === data.fn })

        if (fn === undefined) {
            this.throwFatalError(`${data.fn} is not found`, data)
            return
        }

        let callback = (result) => {
            console.debug('subscription result', result)
            this.port.postMessage(JSON.stringify({
                messageType: "Message::SubscriptionResult",
                data: {
                    fn: data.fn,
                    uuid: data.uuid,
                    result: result
                }
            }))
        }
        this.subscriberCallbacksMap.set(data.uuid, callback)
        fn.subscribe(data, callback)
    }

    unsubscribe(data) {
        const fn = this.functions.find((value) => { return value.name === data.fn })

        if (fn === undefined) {
            this.throwFatalError(`Function:${data.fn} is not found`, data)
            return
        }

        const subscriberCallback = this.subscriberCallbacksMap.get(data.uuid)

        if (subscriberCallback === undefined) {
            this.throwFatalError(`Subscriber:${data.fn} with uuid:${data.uuid} is not found`, data)
            return
        }

        fn.unsubscribe(subscriberCallback)
        this.subscriberCallbacksMap.delete(data.uuid)
        this.port.postMessage(JSON.stringify({
            messageType: "Message::SubscriptionCancellationResult",
            data: {
                fn: data.fn,
                uuid: data.uuid
            }
        }))
    }

    throwFatalError(message, data) {
        console.error(message)

        if (data === undefined) {
            data = {}
        }

        if (!data.hasOwnProperty("message")) {
            data.message = message
        }

        this.port.postMessage(JSON.stringify({
            messageType: "Message::FatalError",
            data: data
        }))
    }

    hasDuplicates(functionName) {
        let duplicatedValue = this.functions.find((value) => { return value.name === functionName })
        return undefined !== duplicatedValue
    }
}


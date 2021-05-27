window.tickMarkFormatter = (pluginParams) => {
    return (time, tickMarkType, locale) => {
        const year = LightweightCharts.isBusinessDay(time) ? time.year : new Date(time * 1000).getUTCFullYear()
        return String(year)
    }
}
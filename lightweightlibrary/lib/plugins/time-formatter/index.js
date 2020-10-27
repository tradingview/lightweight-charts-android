window.timeFormatter = (formatterParams) => {
    return (businessDayOrTimestamp) => {
        console.log(businessDayOrTimestamp, formatterParams)
        const locale = formatterParams.locale
        if (typeof businessDayOrTimestamp == 'number') {
            return new Date(businessDayOrTimestamp * 1000).toLocaleDateString(locale)
        }

        const date = `${businessDayOrTimestamp.year}-${businessDayOrTimestamp.month}-${businessDayOrTimestamp.day}`
        return new Date(Date.parse(date)).toLocaleDateString(locale)
    }
}
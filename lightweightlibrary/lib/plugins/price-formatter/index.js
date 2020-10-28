window.priceFormatter = (formatterParams) => {
    return (price) => {
        console.log(price, formatterParams)
        return formatterParams.template.replace(new RegExp("\\{price\\}", "g"), price)
    }
}
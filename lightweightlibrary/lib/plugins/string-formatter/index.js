window.stringFormatter = (formatterParams) => {
    return (...params) => {
        console.log(params, formatterParams)
        var template = formatterParams.template
        params.forEach((param, index) => {
            template = template.replace(new RegExp("\\{" + index + "\\}", "g"), param)
        })
        return template
    }
}
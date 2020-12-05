window.autoscaleInfoProvider = (pluginParams) => {
    return (original) => {
        var res = original();
        if (res.priceRange !== null) {
            res.priceRange.minValue -= 10;
            res.priceRange.maxValue += 10;
        }
        return res;
    }
}
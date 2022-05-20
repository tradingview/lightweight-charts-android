const path = require('path');

const config = {
    mode: "production",
    optimization: {
        minimize: false
    },
    resolve: {
        alias: {
            lightweight$: path.resolve(__dirname, './node_modules/newton-lightweight-charts/dist/lightweight-charts.esm.development.js'),
        },
    },
};

module.exports = () => {
    return config;
};

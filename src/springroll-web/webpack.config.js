const packageJSON = require('./package.json');
const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: "./src/main/webapp/main.js",
    output: {
		path: path.join(__dirname, 'src/main/webapp/'),
        filename: "springroll.js"
    },
    module: {
        loaders: [
            { test: /\.css$/, loader: "style!css" },
            //{ test: /bootstrap\/js\//, loader: 'imports?jQuery=jquery' }
            {
                test: /org\/cometd/,
                loader: 'imports?this=>window',
            },
            {
                test: /jquery\.cometd\.js$/,
                loader: 'imports?this=>window,define=>false,jQuery=jquery',
            }
        ]
    },
    plugins: [
        new webpack.ProvidePlugin({
            $: 'jquery',
            _: 'underscore',
            jQuery:"jquery",
        })
    ]
};

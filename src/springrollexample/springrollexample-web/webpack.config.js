const packageJSON = require('./package.json');
const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: "./target/assets/main.js",
    output: {
		path: path.join(__dirname, 'src/main/webapp/'),
        filename: "springroll.js"
    },
    module: {
        loaders: [
            { test: /\.css$/, loader: 'style-loader!css-loader' },
            { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file" },
            { test: /\.(woff|woff2)$/, loader:"url?prefix=font/&limit=5000" },
            { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=application/octet-stream" },
            { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=image/svg+xml" }
        ]
    },
    plugins: [
        new webpack.ProvidePlugin({
            $: 'jquery',
            _: 'underscore',
            jQuery:"jquery",
        })
    ],
    resolve: {
        root: path.resolve(__dirname),
        alias: {
            Application$: 'target/assets/lib/framework/Application.js',
            AllSolutionModules: 'src/main/webapp/assets/lib/modules/AllSolutionModules.js'
        },
        extensions: ['', '.js', '.jsx']
    },
};

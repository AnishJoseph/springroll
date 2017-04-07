const packageJSON = require('./package.json');
const path = require('path');
const webpack = require('webpack');

var config = {
    entry: 'index.js',

    output: {
        path: path.join(__dirname, 'src/main/webapp/assets/generated'),
        filename: "index.js",
        publicPath: "assets/generated/"
    },

    module: {
        loaders: [
            { test: /\.scss$/, loader: "style!css!sass" },
            { test: /\.css$/, loader: 'style!css' },
            { test: /\.(woff|woff2)$/, loader:"url?prefix=font/&limit=5000" },
            { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file" },
            { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=application/octet-stream" },
            {
                test: /\.jsx?$/,
                exclude: /node_modules/,
                loader: 'babel',

                query: {
                    presets: ['babel-preset-es2015', 'babel-preset-react'].map(require.resolve)
                }
            },
            { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=image/svg+xml" },
            {
                test: /\.(jpe?g|png|gif|svg)$/i,
                loaders: [
                    'file?hash=sha512&digest=hex&name=[hash].[ext]',
                    'image-webpack?bypassOnDebug&optimizationLevel=7&interlaced=false'
                ]
            }

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
        root: [
            path.resolve(__dirname),
            path.resolve('./node_modules'),
            path.resolve('../../springroll-web/src/main/webapp/assets/lib/framework'),
            path.resolve('../../springroll-web/src/main/webapp/assets/lib/modules'),
            path.resolve('../../springroll-web/src/main/webapp/assets/vendor/cometd/'),
            path.resolve('../../springroll-web/src/main/webapp/assets/css/'),
            path.resolve('src/main/webapp/assets/lib/modules')
        ],
        alias: {
            "org/cometd" : 'target/assets/org/cometd.js',
        },
        extensions: ['', '.js', '.jsx']
    },

    resolveLoader : {
        root : path.join(__dirname, 'node_modules')
    },
}

module.exports = config;
const packageJSON = require('./package.json');
const path = require('path');
const webpack = require('webpack');

var config = {
    entry: 'index.js',

    output: {
        path: path.join(__dirname, 'src/main/webapp/assets/generated'),
        filename: "index.js",
        publicPath: "http://localhost:9080/assets/generated/"
    },

    module: {
        loaders: [
            { test: /\.scss$/, loader: "style-loader!css-loader!sass-loader" },
            { test: /\.css$/, loader: 'style-loader!css-loader' },
            { test: /\.(woff|woff2)$/, loader:"url-loader?prefix=font/&limit=5000" },
            { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file-loader" },
            { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: "url-loader?limit=10000&mimetype=application/octet-stream" },
            {
                test: /\.jsx?$/,
                exclude: /node_modules/,
                loader: 'babel-loader',

                query: {
                    presets: ['babel-preset-es2015', 'babel-preset-react'].map(require.resolve)
                }
            },
            { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: "url-loader?limit=10000&mimetype=image/svg+xml" },
            {
                test: /\.(jpe?g|png|gif|svg)$/i,
                loaders: [
                    'file-loader?hash=sha512&digest=hex&name=[hash].[ext]',
                    'image-webpack-loader?bypassOnDebug&optimizationLevel=7&interlaced=false'
                ]
            }

        ]
    },
    resolve: {
        modules: [
            path.resolve(__dirname),
            path.resolve('./node_modules'),
            path.resolve('../../springroll-web/src/main/webapp/assets/lib/framework'),
            path.resolve('../../springroll-web/src/main/webapp/assets/lib/modules'),
            path.resolve('../../springroll-web/src/main/webapp/assets/lib/components'),
            path.resolve('../../springroll-web/src/main/webapp/assets/css/'),
            path.resolve('src/main/webapp/assets/lib/modules')
        ],
        extensions: ['.js', '.jsx']
    },

    resolveLoader : {
        modules : [path.join(__dirname, 'node_modules')],
    },
}

module.exports = config;
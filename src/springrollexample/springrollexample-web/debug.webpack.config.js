const packageJSON = require('./package.json');
const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: "./debug.main.js",
    output: {
		path: path.join(__dirname, 'src/main/webapp/'),
        filename: "springroll.js"
    },
    module: {
        loaders: [
            //FIXME - we changed comet.js
            //{ test: /org\/cometd\.js$/, loader: 'imports?org=>{}'},
            { test: /\.css$/, loader: 'style-loader!css-loader' },
            { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file" },
            { test: /\.(woff|woff2)$/, loader:"url?prefix=font/&limit=5000" },
            { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=application/octet-stream" },
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
            path.resolve('../../springroll-web/src/main/webapp/assets'),
            path.resolve('../../springroll-web/src/main/webapp/assets/lib/framework/'),
            path.resolve('../../springroll-web/src/main/webapp/assets/lib/modules/'),
            path.resolve('../../springroll-web/src/main/webapp/assets/vendor/cometd/'),
            path.resolve('src/main/webapp/assets/lib/modules/'),
        ],
        alias: {
            "org/cometd" : 'target/assets/org/cometd.js'
        },
        extensions: ['', '.js', '.jsx']
    },
};
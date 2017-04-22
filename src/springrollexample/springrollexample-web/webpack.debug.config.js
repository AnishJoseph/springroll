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
            { test: /\.scss$/,
                use: [{
                    loader: "style-loader" // creates style nodes from JS strings
                }, {
                    loader: "css-loader" // translates CSS into CommonJS
                }, {
                    loader: "sass-loader" // compiles Sass to CSS
                }]
            },
            {
                test: /\.css$/,
                use: [{
                    loader: "style-loader" // creates style nodes from JS strings
                }, {
                    loader: "css-loader" // translates CSS into CommonJS
                }]
            },
            {
                test: /\.(woff|woff2)$/,
                use: [
                    {
                        loader: 'url-loader',
                        options: {
                            prefix : 'font',
                            limit : 5000
                        }
                    }],
            },
            {   test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
                use: [
                    {
                        loader : "file-loader"
                    }
                ]
            },
            {
                test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
                use: [
                    {
                        loader: 'url-loader',
                        options: {
                            limit : 10000,
                            mimetype : 'application/octet-stream'
                        }
                    }],
            },
            {
                test: /\.jsx?$/,
                exclude: /node_modules/,
                loader: 'babel-loader',

                query: {
                    presets: ['babel-preset-es2015', 'babel-preset-react'].map(require.resolve)
                }
            },
            {
                test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
                use: [
                    {
                        loader: 'url-loader',
                        options: {
                            limit : 10000,
                            mimetype : 'image/svg+xml'
                        }
                    }],
            },
            {
                test: /\.(jpe?g|png|gif|svg)$/i,
                use: [
                    {
                        loader: 'file-loader',
                        options: {
                            hash : 'sha512',
                            digest : 'hex',
                            name : '[hash].[ext]'
                        }
                    },
                    {
                        loader: 'image-webpack-loader',
                        options: {
                            query: {
                                mozjpeg: {
                                    progressive: true,
                                },
                                gifsicle: {
                                    interlaced: true,
                                },
                                optipng: {
                                    optimizationLevel: 7,
                                }
                            }
                        }
                    }
                ],
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
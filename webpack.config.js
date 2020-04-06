const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const TerserPlugin = require('terser-webpack-plugin');

const env = process.env.NODE_ENV
const isDev = env !== 'production'

module.exports = {
    mode: env || 'development',
    entry: {
        app: './src/main/assets/js/pages/index.tsx',
        login: './src/main/assets/js/pages/login.tsx',
    },
    devtool: 'sourcemaps',
    cache: true,
    output: {
        filename: '[name].js',
        path: __dirname + '/src/main/resources/static/built/',
    },
    resolve: {
        extensions: ['.ts', '.tsx', '.js']
    },
    optimization: {
        minimizer: !isDev ? [
            new TerserPlugin({ /* additional options here */ })
        ] : [],
    },
    plugins: [
        new MiniCssExtractPlugin({
            filename: '[name].css',
            chunkFilename: '[id].css'
        })
    ],
    module: {
        rules: [
            {
                test: /\.module\.s(a|c)ss$/,
                loader: [
                    isDev ? 'style-loader' : MiniCssExtractPlugin.loader,
                    {
                        loader: 'css-loader',
                        options: {
                            modules: true,
                            sourceMap: isDev
                        }
                    },
                    {
                        loader: 'sass-loader',
                        options: {
                            sourceMap: isDev
                        }
                    }
                ]
            },
            {
                test: /\.s(a|c)ss$/,
                exclude: /\.module.(s(a|c)ss)$/,
                loader: [
                    isDev ? 'style-loader' : MiniCssExtractPlugin.loader,
                    'css-loader',
                    {
                        loader: 'sass-loader',
                        options: {
                            sourceMap: isDev
                        }
                    }
                ]
            },
            {
                test: /\.ts(x?)$/,
                exclude: /node_modules/,
                use: [
                    {
                        loader: "ts-loader"
                    }
                ]
            }
        ]
    }
};
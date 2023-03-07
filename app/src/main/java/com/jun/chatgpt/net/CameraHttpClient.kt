package com.jun.chatgpt.net

import android.util.Log
import com.jun.template.common.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class MyHttpClient {
    private val mClient: OkHttpClient by lazy {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message: String -> Log.d("http", "http log: $message") }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().connectTimeout(Constants.NETWORK_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor).addInterceptor(GptErrorInterceptor()).build()
    }

    fun getClient(): OkHttpClient {
        return mClient
    }
}
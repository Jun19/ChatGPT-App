package com.jun.template.common.net

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * 网络请求
 *
 * @author Jun
 * @time 2021/7/14
 */
object Network {
    private val mClient: OkHttpClient by lazy {
//        val cacheDir = File(getProjectCachePath(App.instance), "okhttp")
//        val mCache = Cache(cacheDir, 8 * 1024 * 1024)
//        val httpLoggingInterceptor =
//            HttpLoggingInterceptor { message: String -> Log.d("Network", message) }
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .connectTimeout(99999, TimeUnit.SECONDS)
            // 给所有的请求添加一个拦截器
//            .addInterceptor(Interceptor { chain -> // 拿到我们的请求
//                val original = chain.request()
//                // 重新进行build
//                val builder: Request.Builder = original.newBuilder()
//                //  builder.addHeader("Content-Type", "application/json")
//                val newRequest: Request = builder.build()
//                // 返回
//                chain.proceed(newRequest)
//            })
//            .addInterceptor(httpLoggingInterceptor)
//            .cache(mCache)
            .build()

    }

    fun getClient(): OkHttpClient {
        return mClient
    }
}